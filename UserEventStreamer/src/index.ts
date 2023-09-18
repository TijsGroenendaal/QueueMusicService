import * as amqp from "amqplib";
import { WebSocket, WebSocketServer } from "ws";
import { Connection, credentials } from "amqplib";
import { UserEventTask } from "./interfaces";
import { createLogger, format, transports } from "winston";

const logger = createLogger({
    transports: [new transports.Console()],
    format: format.combine(
        format.colorize(),
        format.timestamp(),
        format.printf(({ timestamp, level, message}) => {
            return `[${timestamp}] ${level}: ${message}`
        })
    )
})

const amqp_opt = {
    credentials: credentials.plain(
        process.env.RABBITMQ_USERNAME,
        process.env.RABBITMQ_PASSWORD
    )
}
let amqpConnection: Connection;

const wss = new WebSocketServer({ port: 80 })
const wsConnections = new Map<string, WebSocket[]>()

async function setupAMQPConnection() {
    amqpConnection = await amqp.connect(`amqp://localhost:${process.env.RABBITMQ_PORT}`, amqp_opt);
    const channel = await amqpConnection.createChannel()

    const exchange = 'user_event';
    const queue = `user_event_streamer`;

    await channel.assertExchange(exchange, 'fanout', { durable: false })
    await channel.assertQueue(queue, { durable: false })

    await channel.consume(queue, (msg) => {
        try {
            const message = JSON.parse(msg.content.toString()) as UserEventTask

            (wsConnections.get(message.sessionId) ?? [])
                .forEach((socket) => {
                    if (socket.readyState === WebSocket.OPEN) socket.send(msg.content.toString())
                })
        } catch(err) {
            logger.error(err)
        } finally {
            channel.ack(msg)
        }
    })
}

wss.on('connection', async (ws, request) => {
    try {
        const sessionId: string | undefined = new URL(request.headers.host + request.url).searchParams.get('session')

        if (sessionId == undefined) {
            logger.warn("No Session Provided")
            ws.close(1007, "No Session Provided")
            return
        }
        if (!amqpConnection) await setupAMQPConnection()

        const sockets : WebSocket[] | undefined = wsConnections.get(sessionId)

        if (sockets == undefined) wsConnections.set(sessionId, [ws])
        else sockets.push(ws)

        logger.info(`WebSocket client connected for session - ${sessionId}`)

        ws.on('close', () => {
            try {
                const sessionConnections = wsConnections.get(sessionId)
                const wsIndex = sessionConnections.indexOf(ws)
                if (wsIndex > -1) sessionConnections.splice(wsIndex, 1)
                if (sessionConnections.length == 0) wsConnections.delete(sessionId)
            } catch(err) {
                logger.error(err)
            } finally {
                logger.info(`WebSocket client disconnected for session - ${sessionId}`)
            }
        })
    } catch (err) {
        logger.error(err)
        ws.close(1011)
    }
})
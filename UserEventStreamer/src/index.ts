import * as amqp from "amqplib";
import { WebSocket, WebSocketServer } from "ws";
import { credentials } from "amqplib";
import { UserEventTask } from "./interfaces";
import { createLogger, format, transports } from "winston";
import * as crypto from "crypto";

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

setupAMQPConnection().then()

const wss = new WebSocketServer({ port: 8094 })
const wsConnections = new Map<string, WebSocket[]>()

async function setupAMQPConnection() {
    const amqpConnection = await amqp.connect(process.env.RABBITMQ_USER_EVENT_URL, amqp_opt);
    const channel = await amqpConnection.createChannel()

    const exchange = 'user_event';
    const queue = `user_event_streamer-` + crypto.randomUUID().slice(0, 8);

    await channel.assertExchange(exchange, 'fanout', { durable: false })
    await channel.assertQueue(queue, { durable: false })

    await channel.bindQueue(queue, exchange, "")

    await channel.consume(queue, (msg) => {
        try {
            const message = JSON.parse(msg.content.toString()) as UserEventTask;

            const connections: WebSocket[] = (wsConnections.get(message.sessionId) ?? []);
            connections.forEach((socket) => {
                if (socket.readyState === WebSocket.OPEN) socket.send(msg.content.toString())
            })

            logger.info(`Processed ${message.type} for session ${message.sessionId} for ${connections.length} connections`);
        } catch(err) {
            logger.error(err)
        } finally {
            channel.ack(msg)
        }
    })
}

wss.on('connection', async (ws, request) => {
    try {
        const sessionId: string | undefined = new URL(request.url, `https://${request.headers.host}`).searchParams.get('session')

        if (!await verifyToken((request.headers["authentication"] as string).slice(7))) {
            logger.warn("Invalid Authentication")
            ws.close(1011, "Invalid Authentication")
            return
        }

        if (sessionId == undefined) {
            logger.warn("No Session Provided")
            ws.close(1007, "No Session Provided")
            return
        }

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
        ws.close(1011, "Internal Server Error")
    }
})

async function verifyToken(token: string): Promise<boolean> {
    const response = await fetch(`http://localhost:8082/v1/secure/verify-jwt`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })

    return response.ok;
}
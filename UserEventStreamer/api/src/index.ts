import * as amqp from "amqplib";
import { WebSocket, WebSocketServer } from "ws";
import { credentials } from "amqplib";
import { UserEventTask } from "./interfaces";
import { createLogger, format, transports } from "winston";
import * as crypto from "crypto";
import minimist from "minimist";
import { Props } from "./properties";

const args: {[key: string]: string} = minimist(process.argv.slice(2))
const properties = Props[args['env'] ?? 'prd'] ?? Props.prd

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

void setupAMQPConnection()

const wss = new WebSocketServer({ port: properties.ws.port })
const wsConnections = new Map<string, WebSocket[]>()

async function setupAMQPConnection() {
    const amqpConnection = await amqp.connect(properties.amqp.url, amqp_opt);
    const channel = await amqpConnection.createChannel()

    const exchange = properties.amqp.exchange;
    const queue = properties.amqp.queue + crypto.randomUUID().slice(0, 8);

    await channel.assertExchange(exchange, 'fanout', { durable: true })
    await channel.assertQueue(queue, { durable: false, exclusive: true })

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
            logger.warn("Connection - Failed Invalid Authentication")
            ws.close(1011, "Invalid Authentication")
            return
        }

        if (sessionId == undefined) {
            logger.warn("Connection - Failed No Session Provided")
            ws.close(1007, "No Session Provided")
            return
        }

        const sockets : WebSocket[] | undefined = wsConnections.get(sessionId)

        if (sockets == undefined) wsConnections.set(sessionId, [ws])
        else sockets.push(ws)

        logger.info(`Connection - Connected WebSocket client For Session - '${sessionId}'`)

        ws.on('close', () => {
            try {
                const sessionConnections = wsConnections.get(sessionId)
                const wsIndex = sessionConnections.indexOf(ws)
                if (wsIndex > -1) sessionConnections.splice(wsIndex, 1)
                if (sessionConnections.length == 0) wsConnections.delete(sessionId)
            } catch(err) {
                logger.error(err)
            } finally {
                logger.info(`Connection - Disconnected WebSocket Client For Session - '${sessionId}'`)
            }
        })
    } catch (err) {
        logger.error(err)
        ws.close(1011, "Internal Server Error")
    }
})

// Close connection after 60 seconds if no PING received
let checking: Array<WebSocket> = []

setInterval(() => {
    logger.info(`HealthCheck - Terminating '${checking.length}' WebSockets`)
    checking.forEach((ws) => {
        ws.terminate()
    })

    checking = Array.from(wss.clients).filter((ws) => {
        ws.once('message', () => {
            checking.splice(checking.indexOf(ws), 1)
        })

        return ws.readyState === WebSocket.OPEN
    })

    wss.on('pong', (ws: WebSocket) => {
        checking.splice(checking.indexOf(ws), 1)
    })

    logger.info(`HealthCheck - Sending PING to '${checking.length}' WebSockets`)
    checking.forEach((ws) => {
        ws.ping()
    })
}, 10000)

async function verifyToken(token: string): Promise<boolean> {
    const response = await fetch(`${properties.idp.url}/v1/secure/verify-jwt`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })

    return response.ok;
}
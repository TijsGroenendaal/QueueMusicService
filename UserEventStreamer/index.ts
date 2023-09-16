import * as amqp from "amqplib";
import { WebSocket, WebSocketServer } from "ws";
import { Connection, credentials } from "amqplib";
import { UserEventTask } from "./interfaces";

const amqp_opt = {
    credentials: credentials.plain(
        process.env.RABBITMQ_USERNAME,
        process.env.RABBITMQ_PASSWORD
    )
}
let amqpConnection: Connection;

const wss = new WebSocketServer({ port: 8010 })
const wsConnections = new Map<string, WebSocket[]>()

async function setupAMQPConnection() {
    amqpConnection = await amqp.connect(`amqp://localhost:${process.env.RABBITMQ_PORT}`, amqp_opt);
    const channel = await amqpConnection.createChannel()

    const exchange = 'user_event';
    const queue = `user_event_streamer`;

    await channel.assertExchange(exchange, 'fanout', { durable: false })
    await channel.assertQueue(queue, { durable: false })

    await channel.consume(queue, (msg) => {
        const message = JSON.parse(msg.content.toString()) as UserEventTask

        (wsConnections.get(message.sessionId) ?? [])
            .forEach((socket) => {
                if (socket.readyState === WebSocket.OPEN) socket.send(msg.content.toString())
            })

        channel.ack(msg)
    })
}

wss.on('connection', async (ws, request) => {
    const sessionId: string | undefined = new URL(request.headers.host + request.url).searchParams.get('sessionId')

    if (sessionId == undefined) {
        ws.close(404, "No SessionId Provided")
        return
    }
    if (!amqpConnection) await setupAMQPConnection()

    const sockets : WebSocket[] | undefined = wsConnections.get(sessionId)

    if (sockets == undefined) wsConnections.set(sessionId, [ws])
    else sockets.push(ws)

    console.info(`WebSocket client connected for session - ${sessionId}`)

    ws.on('close', () => {
        const sessionConnections = wsConnections.get(sessionId)
        const wsIndex = sessionConnections.indexOf(ws)
        if (wsIndex > -1) sessionConnections.splice(wsIndex, 1)
        if (sessionConnections.length == 0) wsConnections.delete(sessionId)

        console.info(`WebSocket client disconnected for session - ${sessionId}`)
    })
})
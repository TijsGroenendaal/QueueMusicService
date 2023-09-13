import * as amqp from "amqplib";
import { WebSocket, WebSocketServer } from "ws";
import { Connection, credentials } from "amqplib";
import { UserEvent } from "./interfaces";

const wss = new WebSocketServer({ port: 8010 })
const amqp_opt = {
    credentials: credentials.plain(
        process.env.RABBITMQ_USERNAME,
        process.env.RABBITMQ_PASSWORD
    )
}

let amqpConnection: Connection;

const wsConnections = new Map<string, WebSocket[]>()

async function setupAMQPConnection() {
    amqpConnection = await amqp.connect(`amqp://localhost:${process.env.RABBITMQ_PORT}`, amqp_opt);
    const channel = await amqpConnection.createChannel()

    const exchange = 'user_event';
    const queue = 'user_event_streamer';
    const routes = ['song.remove', 'song.vote', 'song.add']

    await channel.assertExchange(exchange, 'fanout', { durable: false })
    await channel.assertQueue(queue, { durable: false })

    routes.forEach((route) => channel.bindQueue(queue, exchange, route))

    await channel.consume(queue, (msg) => {
        const message = JSON.parse(msg.content.toString()) as UserEvent

        (wsConnections.get(message.sessionId) ?? [])
            .filter((socket) => socket.readyState === WebSocket.OPEN)
            .forEach((socket) => socket.send(msg.content.toString()))

        channel.ack(msg)
    })
}

wss.on('connection', async (ws, request) => {
    const sessionId: string | undefined = new URL(request.headers.host + request.url).searchParams.get('sessionId')
    if (sessionId == undefined) {
        ws.close(404, "No SessionId Provided")
    }

    if (!amqpConnection) {
        await setupAMQPConnection()
    }

    const sockets : WebSocket[] | undefined = wsConnections.get(sessionId)
    if (sockets == undefined) {
        wsConnections.set(sessionId, [ws])
    } else {
        sockets.push(ws)
    }
    console.log(Array.from(wsConnections.entries()).map((value) => `${value[0]} - ${value[1].length}`))

    console.log(`WebSocket client connected for session - ${sessionId}`)

    ws.on('message', (event) => {
        console.log(event)
    })

    ws.on('close', () => {
        const sessionConnections = wsConnections.get(sessionId)
        const wsIndex = sessionConnections.indexOf(ws)
        if (wsIndex > -1) {
            sessionConnections.splice(wsIndex, 1)
        }
        if (sessionConnections.length == 0) {
            wsConnections.delete(sessionId)
        }
        console.log(Array.from(wsConnections.entries()).map((value) => `${value[0]} - ${value[1].length}`))
    })
})
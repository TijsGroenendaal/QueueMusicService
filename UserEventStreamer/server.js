#!/usr/bin/env node

const amqp = require("amqplib");
const WebSocket = require("ws");

const wss = new WebSocket.WebSocketServer({port: 8010});

const amqp_opt = {
    credentials: require('amqplib').credentials.plain(
        process.env.RABBITMQ_USERNAME,
        process.env.RABBITMQ_PASSWORD
    )
};

let amqpConnection;

async function setupAMQPConnection() {
    try {
        amqpConnection = await amqp.connect(`amqp://${process.env.RABBITMQ_HOST}:${process.env.RABBITMQ_PORT}`, amqp_opt);
        const channel = await amqpConnection.createChannel();

        const exchange = "queuemusic";
        const queue = "user_event_streamer";
        const routes = ["song.remove", "song.vote", "song.add"];

        await channel.assertExchange(exchange, "direct", { durable: false });
        await channel.assertQueue(queue, { durable: false });

        routes.forEach((route) => channel.bindQueue(queue, exchange, route));

        channel.consume(queue, (msg) => {
            console.log('Received message from AMQP:', msg.content.toString());

            wss.clients.forEach((client) => {
                if (client.readyState === WebSocket.OPEN) {
                    client.send(msg.content.toString());
                }
            });

            channel.ack(msg);
        });
    } catch (error) {
        console.error('Error setting up AMQP connection:', error);
    }
}

wss.on('connection', (ws) => {
    console.log('WebSocket client connected.');

    if (!amqpConnection) {
        setupAMQPConnection();
    }

    ws.on('message', (message) => {
        console.log(message.toString())
        // TODO can probably be removed. Create, Update, Delete is done through Http API
    });

    ws.on('close', () => {
        console.log('WebSocket client disconnected.');
    });
});
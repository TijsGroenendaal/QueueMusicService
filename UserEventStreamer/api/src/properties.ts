interface Properties {
    ws: { port: number },
    amqp: {
        url: string,
        exchange: string
        queue: string
    },
    idp: { url: string  }
}

export const Props: {[key: string]: Properties} = {
    local: {
        ws: { port: 8090 },
        amqp: {
            url: "amqp://localhost:5672",
            exchange: 'user-event',
            queue: 'user-event-streamer-'
        },
        idp: { url: 'http://localhost:8082' }
    },
    prd: {
        ws: { port: 8080 },
        amqp: {
            url: `amqp://${process.env.RABBITMQ_HOST}:5672`,
            exchange: 'user-event',
            queue: 'user-event-streamer-'
        },
        idp: { url: `http://${process.env.IDP_SERVICE_URL}` }
    }
}
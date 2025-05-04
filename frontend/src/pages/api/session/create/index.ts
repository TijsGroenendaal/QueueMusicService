import { NextApiRequest, NextApiResponse } from "next";
import { authFetch } from "@/helper/auth-fetch.helper";

interface CreateSession {
    id: string;
    createdAt: string;
    endAt: string;
    duration: number;
    code: string;
    manualEnded: boolean;
}

export default async function POST(req: NextApiRequest, res: NextApiResponse) {
    const fetch = authFetch(req, res);
    const response = await fetch(
        "https://k8s.tijsgroenendaal.nl/queuemusic/v1/sessions",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(req.body),
        }
    );

    res.status(response.status).json((await response.json()));
    return res.end();
}

import {NextApiRequest, NextApiResponse} from "next";
import querystring from "querystring";
import { stringifyCookie } from "next/dist/compiled/@edge-runtime/cookies";

export default async function POST(req: NextApiRequest, res: NextApiResponse) {
    const { code } = req.query

    if (code == null) {
        res.status(400).end()
        return
    }

    const query = querystring.stringify({
        code: code,
        redirect_uri: 'http://localhost:3000/oauth/callback'
    })

    const response = await fetch(
        "https://k8s.tijsgroenendaal.nl/idp/v1/auth/login?" + query,
        {
            method: 'POST'
        }
    )

    if (!response.ok) {
        res.status(response.status).json(await response.json())
        res.end()
        return
    }

    const json = await response.json()

    res.setHeader("Set-Cookie", [
        stringifyCookie({
            name: 'AT',
            value: json.token,
            sameSite: "strict",
            httpOnly: true,
            path: "/",
            secure: true,
            maxAge: json.expires_in
        }),
        stringifyCookie({
            name: 'RT',
            value: json.refresh_token,
            expires: new Date().setDate(new Date().getDate() + 90),
            sameSite: "strict",
            httpOnly: true,
            path: "/",
            secure: true
        }),
    ])

    res.status(response.status)
    res.end()
}
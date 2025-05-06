import { NextApiRequest, NextApiResponse } from "next";
import { authFetch } from "@/helper/auth-fetch.helper";

export class GetSessionResponse {
  constructor(
    public id: string,
    public createdAt: string,
    public endAt: string,
    public duration: number,
    public code: string,
    public manualEnded: boolean,
  ) {}
}

export default async function GET(req: NextApiRequest, res: NextApiResponse) {
  const fetch = authFetch(req, res);
  const response = await fetch(
    "https://k8s.tijsgroenendaal.nl/queuemusic/v1/sessions/" + req.query.id,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  res.status(response.status).json(await response.json());
}

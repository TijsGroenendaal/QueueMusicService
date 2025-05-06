import { NextApiRequest, NextApiResponse } from "next";
import { authFetch } from "@/helper/auth-fetch.helper";

interface GetSpotifyMe {
  id: string;
  display_name: string;
  email: string;
  images: GetSpotifyMeImage[];
}

interface GetSpotifyMeImage {
  url: string;
  height: number;
  width: number;
}

export default async function GET(req: NextApiRequest, res: NextApiResponse) {
  const fetch = authFetch(req, res);
  const response = await fetch("https://k8s.tijsgroenendaal.nl/idp/v1/user/me");

  if (!response.ok) {
    res.json(await response.json());
    return res.status(response.status).end();
  }

  res.json((await response.json()) as GetSpotifyMe);
  return res.end();
}

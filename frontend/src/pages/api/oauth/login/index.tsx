import { NextApiRequest, NextApiResponse } from "next";
import querystring from "querystring";
import { setCookies } from "@/helper/cookies.helper";

export default async function POST(req: NextApiRequest, res: NextApiResponse) {
  const { code } = req.query;

  if (code == null) {
    res.status(400).end();
    return;
  }

  const query = querystring.stringify({
    code: code,
    redirect_uri: process.env.AUTH_REDIRECT_URI,
  });

  const response = await fetch(
    "https://k8s.tijsgroenendaal.nl/idp/v1/auth/login?" + query,
    {
      method: "POST",
    },
  );

  if (!response.ok) {
    res.status(response.status).json(await response.json());
    res.end();
    return;
  }

  const json = await response.json();

  setCookies(res, json);

  res.status(response.status);
  res.end();
}

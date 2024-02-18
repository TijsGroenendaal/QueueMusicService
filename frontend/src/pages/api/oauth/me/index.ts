import { NextApiRequest, NextApiResponse } from "next";
import { jwtDecode, JwtPayload } from "jwt-decode";

interface QueueMusicPayload extends JwtPayload {
  scope: string[];
}

export default function GET(req: NextApiRequest, res: NextApiResponse) {
  const { AT } = req.cookies;

  if (!AT) {
    res.status(401).end();
    return;
  }

  const decoded = jwtDecode<QueueMusicPayload>(AT);

  res.json({
    isAuthenticated: true,
    roles: decoded.scope,
    user: {
      id: decoded.sub,
    },
  });
}

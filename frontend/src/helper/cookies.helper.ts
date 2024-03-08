import { NextApiResponse } from "next";
import { stringifyCookie } from "next/dist/compiled/@edge-runtime/cookies";

export interface LoginResponse {
  token: string;
  refresh_token: string;
  expires_in: number;
}

export const setCookies = (res: NextApiResponse, json: LoginResponse) => {
  res.setHeader("Set-Cookie", [
    stringifyCookie({
      name: "AT",
      value: json.token,
      sameSite: "strict",
      httpOnly: true,
      path: "/",
      secure: true,
      maxAge: json.expires_in,
    }),
    stringifyCookie({
      name: "RT",
      value: json.refresh_token,
      expires: new Date().setDate(new Date().getDate() + 90),
      sameSite: "strict",
      httpOnly: true,
      path: "/",
      secure: true,
    }),
  ]);
};

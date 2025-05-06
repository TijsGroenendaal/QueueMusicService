import { NextApiRequest, NextApiResponse } from "next";
import { createJsonResponse } from "@/helper/response.helper";
import { refreshTokenFetch } from "@/helper/refresh-token-fetch";

export type AuthFetch = (
  req: NextApiRequest,
  res: NextApiResponse,
) => (input: RequestInfo, init?: RequestInit) => Promise<Response>;

export const authFetch: AuthFetch = (req, res) => {
  return async (input: RequestInfo, init?: RequestInit): Promise<Response> => {
    const { AT, RT } = req.cookies;

    if (!AT && !RT) {
      return createJsonResponse({}, { status: 401 });
    }

    if (!AT) {
      return refreshTokenFetch(input, init, RT, res);
    }

    const response = await fetch(input, {
      ...init,
      headers: {
        ...init?.headers,
        "Content-Type": "application/json",
        Authorization: `Bearer ${AT}`,
      },
    });
    if (response.status == 401) {
      return refreshTokenFetch(input, init, RT, res);
    }

    return response;
  };
};

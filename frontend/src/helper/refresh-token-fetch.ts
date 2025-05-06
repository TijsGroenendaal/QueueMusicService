import { LoginResponse, setCookies } from "@/helper/cookies.helper";
import { NextApiResponse } from "next";
import { createJsonResponse } from "@/helper/response.helper";

export async function refreshTokenFetch(
  input: RequestInfo,
  init: RequestInit | undefined,
  RT: string | undefined,
  res: NextApiResponse,
): Promise<Response> {
  if (!RT) {
    return createJsonResponse({}, { status: 401 });
  }

  const refreshResponse = await fetch(
    "https://k8s.tijsgroenendaal.nl/idp/v1/auth/refresh",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${RT}`,
      },
    },
  );

  if (!refreshResponse.ok) {
    return createJsonResponse({}, { status: 401 });
  }

  const refreshJson = (await refreshResponse.json()) as LoginResponse;

  init = {
    ...init,
    headers: {
      ...init?.headers,
      "Content-Type": "application/json",
      Authorization: `Bearer ${refreshJson.token}`,
    },
  };

  const response = await fetch(input, init);

  if (response.ok) {
    setCookies(res, refreshJson);
  }

  return response;
}

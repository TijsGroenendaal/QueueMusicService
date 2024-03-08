import { NextResponse } from "next/server";

export const createJsonResponse = (
  body: object,
  responseInit: ResponseInit,
): NextResponse => {
  return new NextResponse(JSON.stringify(body), {
    ...responseInit,
    headers: {
      "Content-Type": "application/json",
      ...responseInit.headers,
    },
  });
};

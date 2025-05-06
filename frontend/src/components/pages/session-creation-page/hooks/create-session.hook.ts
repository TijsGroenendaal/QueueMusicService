import {
  CreateSessionRequest,
  CreateSessionRequestType,
  CreateSessionResponse,
} from "@/pages/api/session/create";
import { ApiError } from "@/interfaces/error";
import { useRouter } from "next/router";
import { z } from "zod";

interface UseCreateSession {
  setErrors: (e: string | null) => void;
}

export const useCreateSession = ({ setErrors }: UseCreateSession) => {
  const router = useRouter();

  const zodCreateSession = (data: CreateSessionRequestType) =>
    CreateSessionRequest.extend({
      duration: z.number().min(60).max(240),
      maxUsers: z.number().max(50).min(10),
      autoPlay: z.optional(
        z.object({
          acceptance: z.number().min(2).max(data.maxUsers),
        }),
      ),
    });

  const createSession = async (data: CreateSessionRequestType) => {
    const parseResult = zodCreateSession(data).safeParse(data);

    if (!parseResult.success) {
      return;
    }

    await doCreateSession(parseResult.data);
  };

  const doCreateSession = async (data: CreateSessionRequestType) => {
    fetch("/api/session/create", {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
      },
    }).then(async (response) => {
      if (response.ok) {
        const json = (await response.json()) as CreateSessionResponse;
        await router.push("/session/" + encodeURIComponent(json.id));
      } else {
        const json = (await response.json()) as ApiError;
        setErrors(json.message);
      }
    });
  };

  return {
    createSession,
  };
};

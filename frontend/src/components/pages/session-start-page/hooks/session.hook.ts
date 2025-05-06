import { GetSessionResponse } from "@/pages/api/session/[id]";
import { ApiError, ApiResponse } from "@/interfaces/error";
import { useRouter } from "next/router";
import { useCallback, useEffect, useState } from "react";

export const useSession = () => {
  const [session, setSession] =
    useState<ApiResponse<GetSessionResponse> | null>(null);

  const router = useRouter();
  const { id } = router.query;

  const getSession = useCallback(
    async (id: string): Promise<ApiResponse<GetSessionResponse>> => {
      const response = await fetch(`/api/session/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        const error = await response.json();
        return new ApiError(error.message, response.status, error.code);
      }
      const data = await response.json();
      return new GetSessionResponse(
        data.id,
        data.createdAt,
        data.endAt,
        data.duration,
        data.code,
        data.manualEnded,
      );
    },
    [],
  );

  useEffect(() => {
    if (!id) {
      return;
    }

    getSession(id as string).then((session) => {
      setSession(session);
    });
  }, [id, getSession]);

  return {
    session,
  };
};

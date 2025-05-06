import { useSession } from "@/components/pages/session-start-page/hooks/session.hook";
import { ApiError } from "@/interfaces/error";
import { GetSessionResponse } from "@/pages/api/session/[id]";

export const SessionStartPage = () => {
  const { session } = useSession();

  return (
    <>
      {session && session instanceof ApiError && <p>{session.message}</p>}
      {session && session instanceof GetSessionResponse && (
        <div>{session.id}</div>
      )}
      {!session && <p>Loading...</p>}
    </>
  );
};

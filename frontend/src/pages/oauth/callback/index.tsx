import { useRouter } from "next/router";
import querystring from "querystring";
import { useContext, useEffect } from "react";
import { useMe } from "@/hooks/me-hook";
import { AuthContext } from "@/context/auth-context";

export default function Index() {
  const { query, isReady, push } = useRouter();
  const { code } = query;

  const { getMe } = useMe();
  const authContext = useContext(AuthContext);

  useEffect(() => {
    if (!isReady) {
      return;
    }

    if (isReady && code == null) {
      void push("/oauth/error");
      return;
    }

    const authQuery = querystring.stringify({
      code: code,
    });

    fetch("/api/oauth/login?" + authQuery, {
      method: "POST",
    }).then((_) => {
      getMe().then((me) => {
        authContext.setAuthContext({
          ...authContext,
          user: me,
        });
      });
      void push("/");
    });
  }, [code]);

  return (
    <>
      <div className="flex justify-center items-center h-screen">
        <h1>Logging into Spotify . . .</h1>
      </div>
    </>
  );
}

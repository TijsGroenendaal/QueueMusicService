import { AppProps } from "next/app";
import "../app/globals.css";
import { useLoginAnonymous } from "@/hooks/login-anonymous-hook";
import { useEffect, useState } from "react";
import { AuthContext } from "@/context/auth-context";
import { useMe } from "@/hooks/me-hook";

export default function App({ Component, pageProps }: AppProps) {
  const [authContext, setAuthContext] = useState<AuthContext>({
    user: {
      isAuthenticated: false,
      user: null,
      roles: [],
    },
    setAuthContext: () => {},
  });

  const { login } = useLoginAnonymous();
  const { getMe } = useMe();

  useEffect(() => {
    void login();
    getMe().then((me) => {
      setAuthContext({
        ...authContext,
        user: me,
      });
    });
  }, []);

  return (
    <AuthContext.Provider
      value={{
        ...authContext,
        setAuthContext: setAuthContext,
      }}
    >
      <Component {...pageProps} />
    </AuthContext.Provider>
  );
}

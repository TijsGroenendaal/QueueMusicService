import { QueueMusicContext } from "@/context/auth-context";

interface MeHook {
  getMe: () => QueueMusicContext;
}

export const useMe = () => {
  const getMe = async (): Promise<QueueMusicContext> => {
    const res = await fetch("/api/oauth/me", {
      method: "GET",
    });

    if (res.status === 401) {
      return {
        isAuthenticated: false,
        roles: [],
        user: null,
      };
    }

    return res.json();
  };

  return {
    getMe,
  };
};

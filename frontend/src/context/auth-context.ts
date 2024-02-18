import { createContext } from "react";

export type roles = "SPOTIFY" | string;

export interface AuthContext {
  setAuthContext: (me: AuthContext) => void;
  user: QueueMusicContext;
}

export interface QueueMusicContext {
  isAuthenticated: boolean;
  user: {
    id: string;
  } | null;
  roles: roles[];
}

export const AuthContext = createContext<AuthContext>({
  user: {
    isAuthenticated: false,
    user: null,
    roles: [],
  },
  setAuthContext: () => {},
});

export const isSpotified = (context: AuthContext): boolean => {
  return context.user.roles.includes("SPOTIFY");
};

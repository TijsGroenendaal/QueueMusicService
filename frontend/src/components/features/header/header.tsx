import { useRouter } from "next/router";
import querystring from "querystring";
import { useContext } from "react";
import { AuthContext, isSpotified } from "@/context/auth-context";

export const Header = () => {
  const router = useRouter();
  const authContext = useContext(AuthContext);

  const handleLogin = async () => {
    const query = querystring.stringify({
      response_type: "code",
      client_id: "6fc4af4cd47c4469a54683368d50d48e",
      scope:
        "user-read-playback-state app-remote-control user-modify-playback-state playlist-read-private user-follow-modify playlist-read-collaborative user-follow-read user-read-currently-playing user-read-playback-position user-library-modify playlist-modify-private playlist-modify-public user-read-email user-top-read streaming user-read-recently-played user-read-private user-library-read",
      redirect_uri: `${window.origin}/oauth/callback`,
    });

    await router.push("https://accounts.spotify.com/authorize?" + query);
  };

  return (
    <>
      <header className="flex justify-between">
        <div className="flex items-center">
          <h1 className="text-2xl font-bold text-indigo">QueueMusic</h1>
        </div>
        <div>
          {!isSpotified(authContext) && (
            <button
              className="rounded-md bg-thistle py-2 px-4 w-full text-center text-white font-bold"
              onClick={handleLogin}
            >
              Login
            </button>
          )}
        </div>
      </header>
    </>
  );
};

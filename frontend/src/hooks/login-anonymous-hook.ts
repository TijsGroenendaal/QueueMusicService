import querystring from "querystring";

export interface AnonymousLoginHook {
  login: () => Promise<void>;
}

export const useLoginAnonymous = (): AnonymousLoginHook => {
  const login = async () => {
    const code = generate_code();

    await fetch(
      "/api/oauth/login/anonymous?" + querystring.stringify({ code }),
      {
        method: "POST",
      },
    );
  };

  const generate_code = () => {
    if (window.localStorage.getItem("DVI")) {
      return window.localStorage.getItem("DVI");
    }

    const code =
      Math.random().toString(36).substring(2, 15) +
      Math.random().toString(36).substring(2, 15);
    window.localStorage.setItem("DVI", code);
    return code;
  };

  return {
    login,
  };
};

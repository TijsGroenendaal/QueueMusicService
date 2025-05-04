import { DefaultButton } from "@/components/actions/buttons/default-button";
import Page from "@/components/features/page/page";
import { AuthContext, isSpotified } from "@/context/auth-context";
import { useContext } from "react";
import {useRouter} from "next/router";

export default function Home() {
  const context = useContext(AuthContext);
  const router = useRouter();

  return (
    <>
      <Page>
        <div className="flex justify-evenly items-center mt-2 flex-grow w-full">
          {isSpotified(context) && (
            <DefaultButton
              onClick={() => {
                router.push("/session/create")
              }}
            >
              <h1>Create Session</h1>
            </DefaultButton>
          )}

          <DefaultButton
            onClick={() => {
              console.log("click");
            }}
          >
            <h1>Join Session</h1>
          </DefaultButton>
        </div>
      </Page>
    </>
  );
}

import Page from "@/components/features/page/page";
import { useRouter } from "next/router";
import { SessionStartPage } from "@/components/pages/session-start-page/session-start-page";

export default function Index() {
  return (
    <>
      <Page>
        <SessionStartPage />
      </Page>
    </>
  );
}

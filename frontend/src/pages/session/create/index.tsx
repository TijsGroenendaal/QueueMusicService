import Page from "@/components/features/page/page";
import { SessionCreationPage } from "@/components/pages/session-creation-page/session-creation-page";

export default function Index() {
  return (
    <>
      <Page>
        <SessionCreationPage />
      </Page>
    </>
  );
}

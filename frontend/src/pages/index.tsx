import { DefaultButton } from "@/components/actions/buttons/default-button";
import Page from "@/components/features/page/page";

export default function Home() {
    return (
        <>
            <Page>
                <div className="flex justify-evenly items-center mt-2 flex-grow w-full">
                    <DefaultButton onClick={() => {
                        console.log("Click")
                    }}>
                        <h1>Create Session</h1>
                    </DefaultButton>

                    <DefaultButton onClick={() => {
                        console.log("click")
                    }}>
                        <h1>Join Session</h1>
                    </DefaultButton>
                </div>
            </Page>
        </>
    )
}
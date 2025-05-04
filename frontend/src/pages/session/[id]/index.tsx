import Page from "@/components/features/page/page";
import {useRouter} from "next/router";

export default function Index() {
    const router = useRouter()

    return (
        <>
            <Page>
                <h1>{router.query.id}</h1>
            </Page>
        </>
    );
}
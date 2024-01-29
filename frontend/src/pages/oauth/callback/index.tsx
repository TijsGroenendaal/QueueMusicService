import { useRouter } from "next/router";
import querystring from "querystring";
import { useEffect } from "react";

export default function Index() {
    const { query, isReady, push } = useRouter()
    const { code } = query

    useEffect(() => {
        if (!isReady) {
            return;
        }

        if (isReady && code == null) {
            void push('/oauth/error')
            return;
        }

        const authQuery = querystring.stringify({
            code: code
        })

        fetch(
            "/api/oauth/login?" + authQuery,
            {
                method: 'POST'
            }
        ).then(_ => {
            void push('/')
        })
    }, [code]);

    return (
        <>
            <div className="flex justify-center items-center h-screen">
                <h1>Logging into Spotify . . .</h1>
            </div>
        </>
    )
}


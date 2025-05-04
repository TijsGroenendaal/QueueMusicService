import Page from "@/components/features/page/page";
import {z} from "zod";
import Form from "next/form";
import css from "./index.module.scss"
import {useState} from "react";
import {DefaultButton} from "@/components/actions/buttons/default-button";
import {useRouter} from "next/router";

export default function Index() {
    const router = useRouter();

    const [duration, setDuration] = useState("120")
    const [maxUsers, setMaxUsers] = useState("10")
    const [autoPlay, setAutoPlay] = useState(false)
    const [acceptance, setAcceptance] = useState("5")

    const zod = z.object({
        duration: z.number().min(60).max(240),
        maxUsers: z.number().max(50).min(10),
        autoPlay: z.optional(z.object({
            acceptance: z.number().min(2).max(parseInt(maxUsers))
        }))
    })

    const onSubmit = () => {
        const body = zod.parse({
            duration: parseInt(duration),
            maxUsers: parseInt(maxUsers),
            autoPlay: autoPlay ? {
                acceptance: parseInt(acceptance)
            } : undefined
        })

        fetch("/api/session/create", {
            method: "POST",
            body: JSON.stringify(body),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(async (response) => {
            if (response.ok) {
                const json = await response.json();
                router.push("/session/" + json.id);
            }
        })
    }

    return (
        <>
            <Page>
                <Form action="" className="flex flex-col gap-10">
                    <div>
                        <input type="range" min="60" max="240" value={duration} className={css.slider} id="duration"
                           onChange={(e) => {
                               setDuration(e.target.value)
                           }}
                        />
                        <label htmlFor="duration">Duration: {duration} min</label>
                    </div>
                    <div>
                        <input type="range" min="10" max="50" value={maxUsers} className={css.slider} id="maxUsers"
                           onChange={(e) => {
                              setMaxUsers(e.target.value);
                              if (parseInt(e.target.value) < parseInt(acceptance)) {
                                  setAcceptance(e.target.value);
                              }
                           }}
                        />
                        <label htmlFor="maxUsers">Max Users: {maxUsers}</label>
                    </div>
                    <div>
                        <input type="checkbox" id="autoPlay"
                            onChange={(e)  => {
                                setAutoPlay(e.target.checked);
                            }}
                        />
                        <label htmlFor="autoPlay">Auto Play</label>
                        <div className={!autoPlay ? css.disabled : ''}>
                            <input type="range" min="2" max={maxUsers} value={acceptance} className={css.slider} id="acceptance" disabled={!autoPlay}
                                   onChange={(e) => {
                                       setAcceptance(e.target.value)
                                   }}
                            />
                            <label htmlFor="acceptance">Acceptance: {acceptance}</label>
                        </div>
                    </div>
                    <div className="w-full">
                        <DefaultButton onClick={onSubmit}>
                            <h1>Create Session</h1>
                        </DefaultButton>
                    </div>
                </Form>
            </Page>
        </>
    );
}
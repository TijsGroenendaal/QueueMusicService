import { useContext, useState } from "react";
import { useCreateSession } from "@/components/pages/session-creation-page/hooks/create-session.hook";
import { AuthContext, isSpotified } from "@/context/auth-context";
import Form from "next/form";
import css from "@/components/pages/session-creation-page/session-creation-page.module.scss";
import { DefaultButton } from "@/components/actions/buttons/default-button";

export const SessionCreationPage = () => {
  const [duration, setDuration] = useState("120");
  const [maxUsers, setMaxUsers] = useState("10");
  const [autoPlay, setAutoPlay] = useState(false);
  const [acceptance, setAcceptance] = useState("5");

  const [errors, setErrors] = useState<string | null>(null);

  const { createSession } = useCreateSession({
    setErrors,
  });
  const authContext = useContext(AuthContext);

  const onSubmit = async () => {
    await createSession({
      duration: parseInt(duration),
      maxUsers: parseInt(maxUsers),
      autoPlay: autoPlay
        ? {
            acceptance: parseInt(acceptance),
          }
        : undefined,
    });
  };

  return (
    <>
      <Form action="" className="flex flex-col gap-10">
        <div>
          <input
            type="range"
            min="60"
            max="240"
            value={duration}
            className={css.slider}
            id="duration"
            onChange={(e) => {
              setDuration(e.target.value);
            }}
          />
          <label htmlFor="duration">Duration: {duration} min</label>
        </div>
        <div>
          <input
            type="range"
            min="10"
            max="50"
            value={maxUsers}
            className={css.slider}
            id="maxUsers"
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
          <input
            type="checkbox"
            id="autoPlay"
            onChange={(e) => {
              setAutoPlay(e.target.checked);
            }}
          />
          <label htmlFor="autoPlay">Auto Play</label>
          <div className={!autoPlay ? css.disabled : ""}>
            <input
              type="range"
              min="2"
              max={maxUsers}
              value={acceptance}
              className={css.slider}
              id="acceptance"
              disabled={!autoPlay}
              onChange={(e) => {
                setAcceptance(e.target.value);
              }}
            />
            <label htmlFor="acceptance">Acceptance: {acceptance}</label>
          </div>
        </div>
        <div className="flex flex-row gap-5">
          <DefaultButton
            onClick={onSubmit}
            disabled={!isSpotified(authContext)}
          >
            <h1>Create Session</h1>
          </DefaultButton>
          {errors && <p className="text-red-500 leading-10">{errors}</p>}
        </div>
      </Form>
    </>
  );
};

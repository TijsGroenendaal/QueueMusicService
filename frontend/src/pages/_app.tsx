import {AppProps} from "next/app";
import "../app/globals.css";
import { Comfortaa } from 'next/font/google'


export default function App({Component, pageProps}: AppProps) {
    return (
        <Component {...pageProps}/>
    )
}
import type { Metadata } from "next";
import { Comfortaa } from "next/font/google";
import "./globals.css";

export const metadata: Metadata = {
  title: "QueueMusic",
  description: "Vote for the next song",
};

const comfortaa = Comfortaa({
  subsets: ["latin"],
  display: "swap",
  variable: "--font-comfortaa",
});

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={comfortaa.className}>{children}</body>
    </html>
  );
}

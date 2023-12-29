import type { Metadata } from 'next'
import { Comfortaa } from 'next/font/google'
import './globals.css'

const inter = Comfortaa({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'QueueMusic',
  description: 'Vote for the next song',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>{children}</body>
    </html>
  )
}

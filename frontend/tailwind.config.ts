import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        aquamarine: "#40DDAE",
        indigo: "#8385C3",
        thistle: "#DABECB"
      }
    },
  },
  plugins: [],
}
export default config

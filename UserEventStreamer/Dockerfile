FROM node:18
WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install --omit=dev
COPY . .
RUN npm run build

EXPOSE 80:80
ENTRYPOINT npm run start
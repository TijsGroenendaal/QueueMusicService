FROM node:18
WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install --omit=dev
COPY . .
RUN npm run build

COPY entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh

CMD /opt/entrypoint.sh
FROM node:10.15.3 AS build-env

# package.json and package-lock.json
COPY package*.json /build/

WORKDIR /build

RUN npm install --production

FROM node:10.15.3-alpine AS runtime-env

RUN mkdir /app

COPY --from=build-env --chown=node:node /build /app

USER node

WORKDIR /app

EXPOSE 8050

CMD [ "node", "“index.js" ]

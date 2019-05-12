FROM node:10.15.3 AS build-env

# package.json and package-lock.json
COPY package*.json ./

RUN npm install --production

FROM node:10.15.3-alpine AS runtime-env

ARG NODE_ENV_ARG=kandy_eu_prp
ENV NODE_ENV=$NODE_ENV_ARG

RUN mkdir /app

COPY --from=build-env ./ /app

RUN chown -R node:node /app

USER node

EXPOSE 8050

CMD [ "node", "“index.js" ]

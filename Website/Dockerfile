FROM node:lts-alpine as build-stage
WORKDIR /application

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:stable-alpine as production-stage

RUN mkdir /view
COPY --from=build-stage /application/dist /view
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

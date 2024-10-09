FROM alpine:3.20.1 as builder

#RUN apk --no-check-certificate update && apk --no-check-certificate upgrade
RUN apk add --update --no-check-certificate nodejs npm

WORKDIR /app
ADD . .

RUN npm install -g @angular/cli http-server
RUN npm install
RUN ng build -c production

RUN npm install @ngx-translate/core @ngx-translate/http-loader
RUN npm install @fortawesome/fontawesome-free
RUN npm install primeng primeicons
RUN npm install ngx-cookie-service --save


WORKDIR /app/dist/frontend/browser

CMD ["http-server", "--cors"]

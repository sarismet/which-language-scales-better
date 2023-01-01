FROM golang:latest

ENV GO111MODULE=auto

EXPOSE 7005

RUN mkdir -p /go/src/github.com/mock-golang-notification-sender

WORKDIR /go/src/github.com/mock-golang-notification-sender

COPY . /go/src/github.com/mock-golang-notification-sender

RUN chmod a+x /go/src/github.com/mock-golang-notification-sender

RUN go get github.com/labstack/echo

CMD ["go","run","/go/src/github.com/mock-golang-notification-sender/main.go"]
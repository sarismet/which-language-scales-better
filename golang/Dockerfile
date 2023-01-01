FROM golang:latest

ENV GO111MODULE=auto

EXPOSE 7002

RUN mkdir -p /go/src/github.com/iso-scale

WORKDIR /go/src/github.com/iso-scale

COPY . /go/src/github.com/iso-scale

RUN chmod a+x /go/src/github.com/iso-scale

RUN go get github.com/labstack/echo

CMD ["go","run","/go/src/github.com/iso-scale/main.go"]
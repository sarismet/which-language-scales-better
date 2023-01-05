FROM golang:latest

ENV GO111MODULE=auto

RUN mkdir -p /go/src/github.com/iso-scale

WORKDIR /go/src/github.com/iso-scale

COPY . /go/src/github.com/iso-scale

RUN chmod a+x /go/src/github.com/iso-scale

RUN go get github.com/labstack/echo

EXPOSE 7002

CMD ["go","run","/go/src/github.com/iso-scale/main.go"]
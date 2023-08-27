package main

import (
	"fmt"
	"net/http"
	"os"
	"time"

	"github.com/labstack/echo"
)

type SendNotificationRequest struct {
	Device_id string `json:"deviceId"`
}

type Result struct {
	Success bool `json:"success"`
}

var sleep_time int64

func main() {
	if os.Getenv("server.sleepTime") == "" {
		sleep_time = 0
	} else {
		fmt.Sscan(os.Getenv("server.sleepTime"), &sleep_time)
	}

	fmt.Println("Sleep time is:", sleep_time)

	echo_server := echo.New()

	echo_server.POST("/send/", send_notification)
	echo_server.Start("0.0.0.0:7101")
}

func send_notification(c echo.Context) error {
	notificaiton_channel := make(chan bool)

	go func() {
		defer close(notificaiton_channel)

		time.Sleep(time.Duration(sleep_time) * time.Millisecond)

		notificaiton_channel <- true
	}()

	result := <-notificaiton_channel

	return c.JSON(http.StatusOK, &Result{
		Success: result,
	})
}

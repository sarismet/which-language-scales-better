package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"time"

	"github.com/labstack/echo"
)

var send_notification_url = "http://mock-notification-sender:7004/send/"

type SendNotificationRequest struct {
	Device_id string `json:"deviceId"`
}

type Result struct {
	Success bool `json:"success"`
}

var sleep_time int64

func main() {
	fmt.Sscan(os.Getenv("server.sleepTime"), &sleep_time)
	fmt.Println("Sleep time is:", sleep_time)

	echo_server := echo.New()

	echo_server.POST("/send/", send_notification)
	echo_server.Start(":7002")
}

func send_notification(c echo.Context) error {
	send_notification_request := &SendNotificationRequest{}
	defer c.Request().Body.Close()
	err := c.Bind(send_notification_request)

	if err != nil {
		return c.String(http.StatusInternalServerError, err.Error())
	}

	values := map[string]string{"deviceId": send_notification_request.Device_id}
	json_data, err := json.Marshal(values)

	if err != nil {
		return c.String(http.StatusInternalServerError, err.Error())
	}

	notification_to_server_result := <-send_notification_to_server(json_data)

	return c.JSON(http.StatusOK, &Result{
		Success: notification_to_server_result,
	})
}

func send_notification_to_server(json_data []byte) <-chan bool {
	notificaiton_channel := make(chan bool)

	go func() {
		defer close(notificaiton_channel)

		time.Sleep(time.Duration(sleep_time) * time.Millisecond)

		_, err := http.Post(send_notification_url, "application/json", bytes.NewBuffer(json_data))

		if err != nil {
			fmt.Println(err)
			notificaiton_channel <- false
		}
		notificaiton_channel <- true
	}()

	return notificaiton_channel
}

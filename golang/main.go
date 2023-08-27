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

var send_java8_notification_url string
var send_golang_notification_url string

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

	if os.Getenv("notification.sender-server.java8.url") == "" {
		send_java8_notification_url = "http://localhost:7004/send/"
	} else {
		fmt.Sscan(os.Getenv("notification.sender-server.java8.url"), &send_java8_notification_url)
	}

	if os.Getenv("notification.sender-server.golang.url") == "" {
		send_golang_notification_url = "http://localhost:7005/send/"
	} else {
		fmt.Sscan(os.Getenv("notification.sender-server.java8.url"), &send_golang_notification_url)
	}

	echo_server := echo.New()

	echo_server.POST("/send/itself/", send_notification_itself)
	echo_server.POST("/send/java/", send_notification_to_java)
	echo_server.POST("/send/golang/", send_notification_to_golang)
	echo_server.Start("0.0.0.0:7002")
}

func send_notification_itself(c echo.Context) error {
	time.Sleep(time.Duration(sleep_time) * time.Millisecond)

	return c.JSON(http.StatusOK, Result{Success: true})
}

func send_notification_to_java(c echo.Context) error {
	send_notification_request := &SendNotificationRequest{}
	defer c.Request().Body.Close()
	err := c.Bind(send_notification_request)

	if err != nil {
		return c.String(http.StatusInternalServerError, err.Error())
	}

	notification_to_server_result, _ := send_notification_to_server(send_notification_request, send_java8_notification_url)

	return c.JSON(http.StatusOK, notification_to_server_result)
}

func send_notification_to_golang(c echo.Context) error {
	send_notification_request := &SendNotificationRequest{}
	defer c.Request().Body.Close()
	err := c.Bind(send_notification_request)

	if err != nil {
		return c.String(http.StatusInternalServerError, err.Error())
	}

	notification_to_server_result, _ := send_notification_to_server(send_notification_request, send_golang_notification_url)

	return c.JSON(http.StatusOK, notification_to_server_result)
}

func send_notification_to_server(send_notification_request *SendNotificationRequest, send_notification_url string) (*Result, error) {
	values := map[string]string{"deviceId": send_notification_request.Device_id}
	json_data, err := json.Marshal(values)

	if err != nil {
		return nil, err
	}

	notification_to_server_result := <-send_notification(json_data, send_notification_url)

	return &notification_to_server_result, nil
}

func send_notification(json_data []byte, send_notification_url string) <-chan Result {
	notificaiton_channel := make(chan Result)

	go func() {
		defer close(notificaiton_channel)

		response, err := http.Post(send_notification_url, "application/json", bytes.NewBuffer(json_data))
		if err != nil {
			notificaiton_channel <- Result{Success: false}
		}

		var result Result

		json.NewDecoder(response.Body).Decode(&result)

		notificaiton_channel <- result
	}()

	return notificaiton_channel
}

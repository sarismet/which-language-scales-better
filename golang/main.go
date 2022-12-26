package main

import (
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"time"
	"github.com/labstack/echo"
)

var send_notification_url = "http://localhost:8084/send/"

type SendNotificationRequest struct {
	Device_id string `json:"deviceId"`
}

func main() {

	echo_server := echo.New()

	echo_server.POST("/send/", send_notification)
	echo_server.Start(":8083")

}

func send_notification(c echo.Context) error {

	send_notification_request := &SendNotificationRequest{}
	defer c.Request().Body.Close()
	err := c.Bind(send_notification_request)

	if err != nil {
		return c.String(http.StatusInternalServerError, err.Error())
	}

	go func() {
		values := map[string]string{"deviceId": send_notification_request.Device_id}
		json_data, err := json.Marshal(values)

		time.Sleep(100 * time.Millisecond)

		if err != nil {
			log.Fatal(err)
		}

		_, err = http.Post(send_notification_url, "application/json", bytes.NewBuffer(json_data))

		if err != nil {
			log.Printf("Push notification service is unavailable")
		}
	}()

	return nil
}

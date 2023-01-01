package main

import (
	"net/http"

	"github.com/labstack/echo"
)

type SendNotificationRequest struct {
	Device_id string `json:"deviceId"`
}

type Result struct {
	Success bool `json:"success"`
}

func main() {
	echo_server := echo.New()

	echo_server.POST("/send/", send_notification)
	echo_server.Start(":7005")
}

func send_notification(c echo.Context) error {
	return c.JSON(http.StatusOK, &Result{
		Success: true,
	})
}

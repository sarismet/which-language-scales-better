import time

from utils import send_notification_to_server


HOST = "http://localhost"
PORT = "7002"

URL = HOST + ":" + PORT + "/send/"


def test_golang_notification_to_server():
    send_notification_to_server("Golang", URL, 1000)


if __name__ == "__main__":
    test_golang_notification_to_server()
import time

from utils import send_notification_to_server


HOST = "http://localhost"
PORT = "7002"

URL_TO_JAVA8_SERVER = HOST + ":" + PORT + "/send/"
URL_TO_GOLANG_SERVER = HOST + ":" + PORT + "/send/golang/"


def test_golang_notification_to_server():
    send_notification_to_server("Golang", URL_TO_JAVA8_SERVER, 5000)
    time.sleep(5)
    send_notification_to_server("Golang", URL_TO_GOLANG_SERVER, 5000)


if __name__ == "__main__":
    test_golang_notification_to_server()
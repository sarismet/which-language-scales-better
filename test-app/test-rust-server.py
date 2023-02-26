import time

from utils import send_notification_to_server


HOST = "http://localhost"
PORT = "7004"

URL_TO_JAVA8_SERVER = HOST + ":" + PORT + "/send/"
URL_TO_GOLANG_SERVER = HOST + ":" + PORT + "/send/golang/"


def test_rust_notification_to_server():
    send_notification_to_server("Rust", URL_TO_GOLANG_SERVER, 59)


if __name__ == "__main__":
    test_rust_notification_to_server()

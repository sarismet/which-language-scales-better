import time

from utils import send_notification_to_server


HOST = "http://localhost"
PORT = "7004"

URL_TO_ITSELF = HOST + ":" + PORT + "/send/itself/"
URL_TO_JAVA8_SERVER = HOST + ":" + PORT + "/send/java/"
URL_TO_GOLANG_SERVER = HOST + ":" + PORT + "/send/golang/"


def test_rust_notification_to_itself():
    send_notification_to_server("Rust", URL_TO_ITSELF, 1000)
    time.sleep(10)
    send_notification_to_server("Rust", URL_TO_JAVA8_SERVER, 1000)
    time.sleep(10)
    send_notification_to_server("Rust", URL_TO_GOLANG_SERVER, 1000)


if __name__ == "__main__":
    test_rust_notification_to_server()

import time

from utils import send_notification_to_server

HOST = "http://localhost"

JAVA8_PORT = "7000"

JAVA8_URL_TO_ITSELF_V3 = HOST + ":" + JAVA8_PORT + "/send/itself/"

JAVA19_PORT = "7001"

JAVA19_URL_TO_ITSELF_V3 = HOST + ":" + JAVA19_PORT + "/send/itself/"

RUST_PORT = "7004"

RUST_JAVA19_URL_TO_ITSELF = HOST + ":" + RUST_PORT + "/send/itself/"


def test_java19_notification_to_server():
    send_notification_to_server("Java 8", JAVA8_URL_TO_ITSELF_V3, 1000)

    time.sleep(5)

    send_notification_to_server("Java 19", JAVA19_URL_TO_ITSELF_V3, 50000)

    time.sleep(5)

    send_notification_to_server("Rust", RUST_JAVA19_URL_TO_ITSELF, 50000)


if __name__ == "__main__":
    test_java19_notification_to_server()

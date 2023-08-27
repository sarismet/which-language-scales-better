import time

from utils import send_notification_to_server

HOST = "http://localhost"

JAVA19_PORT = "7001"

JAVA19_URL_TO_ITSELF_V3 = HOST + ":" + JAVA19_PORT + "/send/itself/"
JAVA19_URL_TO_JAVA8_SERVER_V3 = HOST + ":" + JAVA19_PORT + "/send/java/"
JAVA19_URL_TO_GOLANG_SERVER_V3 = HOST + ":" + JAVA19_PORT + "/send/golang/"


RUST_PORT = "7004"

RUST_JAVA19_URL_TO_ITSELF = HOST + ":" + RUST_PORT + "/send/itself/"
RUST_JAVA19_URL_TO_JAVA8_SERVER = HOST + ":" + RUST_PORT + "/send/java/"
RUST_JAVA19_URL_TO_GOLANG_SERVER = HOST + ":" + RUST_PORT + "/send/golang/"


def test_java19_notification_to_server():
    send_notification_to_server("Java 19", JAVA19_URL_TO_ITSELF_V3, 50000)
    time.sleep(5)

    send_notification_to_server("Java 19", JAVA19_URL_TO_JAVA8_SERVER_V3, 30000)
    time.sleep(5)

    send_notification_to_server("Java 19", JAVA19_URL_TO_GOLANG_SERVER_V3, 30000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_JAVA19_URL_TO_ITSELF, 50000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_JAVA19_URL_TO_JAVA8_SERVER, 30000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_JAVA19_URL_TO_GOLANG_SERVER, 30000)
    time.sleep(5)


if __name__ == "__main__":
    test_java19_notification_to_server()

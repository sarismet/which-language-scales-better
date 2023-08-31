import time

from utils import send_notification_to_server

HOST = "http://localhost"

JAVA19_PORT = "7001"

JAVA19_URL_TO_ITSELF_V3 = HOST + ":" + JAVA19_PORT + "/send/itself/v3/"
JAVA19_URL_TO_JAVA8_SERVER_V3 = HOST + ":" + JAVA19_PORT + "/send/java/v3/"
JAVA19_URL_TO_GOLANG_SERVER_V3 = HOST + ":" + JAVA19_PORT + "/send/golang/v3/"


RUST_PORT = "7004"

RUST_URL_TO_ITSELF = HOST + ":" + RUST_PORT + "/send/itself/v3/"
RUST_URL_TO_JAVA8_SERVER = HOST + ":" + RUST_PORT + "/send/java/v3/"
RUST_URL_TO_GOLANG_SERVER = HOST + ":" + RUST_PORT + "/send/golang/v3/"


def test_java19_notification_to_server():
    send_notification_to_server("Java 19", JAVA19_URL_TO_ITSELF_V3, 50000)
    time.sleep(5)

    send_notification_to_server("Java 19", JAVA19_URL_TO_JAVA8_SERVER_V3, 30000)
    time.sleep(5)

    send_notification_to_server("Java 19", JAVA19_URL_TO_GOLANG_SERVER_V3, 30000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_URL_TO_ITSELF, 50000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_URL_TO_JAVA8_SERVER, 30000)
    time.sleep(5)

    send_notification_to_server("Rust", RUST_URL_TO_GOLANG_SERVER, 30000)
    time.sleep(5)


if __name__ == "__main__":
    test_java19_notification_to_server()

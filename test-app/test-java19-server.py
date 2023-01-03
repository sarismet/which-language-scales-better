import time

from utils import send_notification_to_server

HOST = "http://localhost"
PORT = "7001"

URL_TO_JAVA8_SERVER_V1 = HOST + ":" + PORT + "/send/"
URL_TO_GOLANG_SERVER_V1 = HOST + ":" + PORT + "/send/golang/"

URL_TO_JAVA8_SERVER_V2 = HOST + ":" + PORT + "/send/v2/"
URL_TO_GOLANG_SERVER_V2 = HOST + ":" + PORT + "/send/v2/golang/"

URL_TO_JAVA8_SERVER_V3 = HOST + ":" + PORT + "/send/v3/"
URL_TO_GOLANG_SERVER_V3 = HOST + ":" + PORT + "/send/v3/golang/"


def test_java19_notification_to_server():
    send_notification_to_server("Java 19", URL_TO_JAVA8_SERVER_V1, 5)
    time.sleep(5)

    send_notification_to_server("Java 19", URL_TO_GOLANG_SERVER_V1, 5)
    time.sleep(10)

    send_notification_to_server("Java 19", URL_TO_JAVA8_SERVER_V2, 5)
    time.sleep(5)

    send_notification_to_server("Java 19", URL_TO_GOLANG_SERVER_V2, 5)
    time.sleep(10)

    send_notification_to_server("Java 19", URL_TO_JAVA8_SERVER_V3, 5)
    time.sleep(5)

    send_notification_to_server("Java 8", URL_TO_GOLANG_SERVER_V3, 5000)
    time.sleep(10)


if __name__ == "__main__":
    test_java19_notification_to_server()

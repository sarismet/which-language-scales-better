import time

from utils import send_notification_to_server
from utils import errors

HOST = "http://localhost"
PORT = "7000"

URL_TO_JAVA8_SERVER_V1 = HOST + ":" + PORT + "/send/"
URL_TO_GOLANG_SERVER_V1 = HOST + ":" + PORT + "/send/golang/"

URL_TO_JAVA8_SERVER_V2 = HOST + ":" + PORT + "/send/v2/"
URL_TO_GOLANG_SERVER_V2 = HOST + ":" + PORT + "/send/v2/golang/"

URL_TO_JAVA8_SERVER_V3 = HOST + ":" + PORT + "/send/v3/"
URL_TO_GOLANG_SERVER_V3 = HOST + ":" + PORT + "/send/v3/golang/"


def test_java8_notification_to_server():
    send_notification_to_server("Java 8", URL_TO_JAVA8_SERVER_V1, 5000)
    time.sleep(5)

    send_notification_to_server("Java 8", URL_TO_GOLANG_SERVER_V1, 5000)
    time.sleep(10)

    send_notification_to_server("Java 8", URL_TO_JAVA8_SERVER_V2, 5000)
    time.sleep(5)

    send_notification_to_server("Java 8", URL_TO_GOLANG_SERVER_V2, 5000)
    time.sleep(10)

    send_notification_to_server("Java 8", URL_TO_JAVA8_SERVER_V3, 5000)
    time.sleep(5)

    send_notification_to_server("Java 8", URL_TO_GOLANG_SERVER_V3, 5000)
    time.sleep(10)


if __name__ == "__main__":

    test_java8_notification_to_server()

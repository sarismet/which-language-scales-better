import time

from utils import send_notification_to_server
from utils import errors

URL_V1 = "http://localhost:7000/send/"
URL_V2 = "http://localhost:7000/send/v2/"
URL_V3 = "http://localhost:7000/send/v3/"

def test_java8_notification_to_server():
    send_notification_to_server("Java 8", URL_V1, 500)
    send_notification_to_server("Java 8", URL_V2, 500)
    send_notification_to_server("Java 8", URL_V3, 500)


if __name__ == "__main__":

    test_java8_notification_to_server()

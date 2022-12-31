import time

from utils import send_notification_to_server


HOST = "http://localhost"
PORT = "7002"

URL_V1 = HOST + ":" + PORT + "/send/"
URL_V2 = HOST + ":" + PORT + "/send/v2/"
URL_V3 = HOST + ":" + PORT + "/send/v3/"

def test_golang_notification_to_server():
    send_notification_to_server("Java 19", URL_V1, 500)
    send_notification_to_server("Java 19", URL_V2, 500)
    send_notification_to_server("Java 19", URL_V3, 500)


if __name__ == "__main__":
    test_golang_notification_to_server()
import time

from utils import send_notification_to_server

HOST = "http://localhost"
PORT = "7001"

URL_V1 = HOST + ":" + PORT + "/send/"
URL_V2 = HOST + ":" + PORT + "/send/v2/"
URL_V3 = HOST + ":" + PORT + "/send/v3/"

def test_java19_notification_to_server():
    send_notification_to_server("Java 19", URL_V1, 11)
    time.sleep(15)
    send_notification_to_server("Java 19", URL_V2, 11)
    time.sleep(15)
    send_notification_to_server("Java 19", URL_V3, 11)


if __name__ == "__main__":
    test_java19_notification_to_server()

import time

from utils import send_notification_to_server
from utils import errors

HOST = "http://localhost"
PORT = "7000"

URL_V1 = HOST + ":" + PORT + "/send/"
URL_V2 = HOST + ":" + PORT + "/send/v2/"
URL_V3 = HOST + ":" + PORT + "/send/v3/"

def test_java8_notification_to_server():
    send_notification_to_server("Java 8", URL_V1, 11)
    time.sleep(15)
    send_notification_to_server("Java 8", URL_V2, 11)
    time.sleep(15)
    send_notification_to_server("Java 8", URL_V3, 11)


if __name__ == "__main__":

    test_java8_notification_to_server()

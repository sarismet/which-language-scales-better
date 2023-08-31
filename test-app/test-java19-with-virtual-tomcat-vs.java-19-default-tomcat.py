import time

from utils import send_notification_to_server

HOST = "http://localhost"

JAVA_19_WITH_VIRTUAL_ITSELF_PORT = "7005"

URL_TO_JAVA_19_WITH_VIRTUAL_ITSELF_V3 = HOST + ":" + JAVA_19_WITH_VIRTUAL_ITSELF_PORT + "/send/itself/v3/"

JAVA_19_WITH_NORMAL_ITSELF_PORT = "7001"

URL_TO_JAVA_19_WITH_NORMAL_ITSELF_V3 = HOST + ":" + JAVA_19_WITH_NORMAL_ITSELF_PORT + "/send/itself/v3/"


def test_java19_notification_to_server():
    send_notification_to_server("Java 19 with virtual tomcat", URL_TO_JAVA_19_WITH_VIRTUAL_ITSELF_V3, 10000)
    time.sleep(5)

    send_notification_to_server("Java 19 with normal tomcat", URL_TO_JAVA_19_WITH_NORMAL_ITSELF_V3, 10000)


if __name__ == "__main__":
    test_java19_notification_to_server()

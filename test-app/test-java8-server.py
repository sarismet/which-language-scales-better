import time

from utils import send_notification_to_server


def test_java8_notification_to_server():
    start = time.time()
    send_notification_to_server("Java 8", "http://localhost:8081/send/", 15000)
    end = time.time()

    print(
        "Total execution time for server name: {} is : {}".format(
            "Java 8", (end - start)
        )
    )


if __name__ == "__main__":

    test_java8_notification_to_server()
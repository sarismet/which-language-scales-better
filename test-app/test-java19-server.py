import time

from utils import send_notification_to_server


def test_java19_notification_to_server():
    start = time.time()
    send_notification_to_server("Java 19", "http://localhost:8082/send/", 15000)
    end = time.time()

    print(
        "Total execution time for server name: {} is : {}".format(
            "Java 19", (end - start)
        )
    )


if __name__ == "__main__":
    test_java19_notification_to_server()
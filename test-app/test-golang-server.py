import time

from utils import send_notification_to_server


def test_golang_notification_to_server():
    start = time.time()
    send_notification_to_server("Golang", "http://localhost:8083/send/", 15000)
    end = time.time()

    print(
        "Total execution time for server name: {} is : {}".format(
            "Golang", (end - start)
        )
    )


if __name__ == "__main__":
    test_golang_notification_to_server()
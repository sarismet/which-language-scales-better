import concurrent.futures
import json
import time

import requests


def log_error(server_url: str, notification_count: int) -> None:
    print(
        "Error occurred for server url : {} at notification count: ".format(server_url),
        notification_count,
    )


def send_notification_to_related_server(
        server_url: str, notification_count: int
) -> bool:
    try:
        if notification_count % 100 == 0:
            print("keep going", notification_count)

        response = requests.post(
            url=server_url,
            json={"deviceId": "a7a753cb-1092-45c1-a54a-c594e6f3e558"},
            timeout=1,
        )
        if response.status_code == 200:
            json_payload = json.loads(response.text)
            if not json_payload["success"]:
                log_error(server_url, notification_count)

                return False
        elif response.status_code != 200:
            log_error(server_url, notification_count)

            return False
    except Exception as ex:
        print(
            "Error occurred for server url : {} at notification count: ".format(
                server_url
            ),
            notification_count,
            ex,
        )

        return False


def send_notification_to_server(
        server_name: str, server_url: str, total_notification_count: int
) -> None:
    print(
        "Sending {} notification to server: {} with url: {}".format(
            server_name, server_url, total_notification_count
        )
    )

    start_process = time.time()

    futures = []
    with concurrent.futures.ThreadPoolExecutor(max_workers=100) as executor:
        start_submit = time.time()
        for notification_count in range(total_notification_count):
            future = executor.submit(
                send_notification_to_related_server, server_url, notification_count
            )

            futures.append(future)
        end_submit = time.time()

        print(
            "The number of {} notification sender tasks for server name: {} are initialized in {} seconds".format(
                total_notification_count, server_name, (end_submit - start_submit)
            )
        )

    end_process = time.time()

    error_count = 0
    for future in futures:
        if not future.result():
            error_count = error_count + 1

    print(
        "Total execution time for {} server with endpoint {} is : {}. Error count is : {}\n\n".format(
            server_name, server_url, (end_process - start_process), len(error_count)
        )
    )

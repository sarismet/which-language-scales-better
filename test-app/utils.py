import requests
import concurrent.futures
import time

errors = list()

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
        if response.status_code != 200:
            errors.append(notification_count)
            print(
                "Error occurred for server url : {} at notification count: ".format(
                    server_url
                ),
                notification_count,
            )
    except Exception as ex:
        errors.append(notification_count)
        print(
            "Error occurred for server url : {} at notification count: ".format(
                server_url
            ),
            notification_count,
            ex,
        )


def send_notification_to_server(
    server_name: str, server_url: str, total_notification_count: int
):

    print(
        "Sending {} notification to server: {} with url: {}".format(
            server_name, server_url, total_notification_count
        )
    )

    start_process = time.time()

    with concurrent.futures.ThreadPoolExecutor(max_workers=34) as executor:
        start_submit = time.time()
        for notification_count in range(total_notification_count):
            executor.submit(
                send_notification_to_related_server, server_url, notification_count
            )
        end_submit = time.time()

        print(
            "The number of {} notification sender tasks for server name: {} are initialized in {} seconds".format(
                total_notification_count, server_name, (end_submit - start_submit)
            )
        )

    end_process = time.time()

    print(
        "{} server successfuly handled {} notification request".format(
            server_name, total_notification_count
        )
    )

    global errors
    
    print(
        "Total execution time for {} server with endpoint {} is : {}. Error count: {}\n\n".format(
            server_name, server_url, (end_process - start_process), len(errors)
        )
    )

    errors = list()

import requests
import concurrent.futures
import time


def send_notification_to_related_server(server_url: str) -> bool:
    try:
        response = requests.post(url=server_url, json={"deviceId": "a7a753cb-1092-45c1-a54a-c594e6f3e558"}, timeout=1)
        if response.status_code == 200:
            response_json = response.json()

            return response_json["success"]
    except:
        return False


def send_notification_to_server(server_name: str, server_url: str, total_notification_count: int):
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        tasks = list[concurrent.futures._base.Future]()
        for notification_count in range(total_notification_count):
            task = executor.submit(send_notification_to_related_server, server_url)
            tasks.append(task)

        for notification_count, task in enumerate(tasks):
            if (not task.result()):
                print("{} server return error response at notification count: ".format(server_name), notification_count)
                return
        
    print("{} server successfuly handled {} notification request".format(server_name, total_notification_count))

def test_java8_notification_to_server():
    send_notification_to_server("Java 8", "http://localhost:8081/send/", 15000)

def test_java19_notification_to_server():
    send_notification_to_server("Java 19", "http://localhost:8082/send/", 15000)

if __name__ == "__main__":
    test_java8_notification_to_server()

    time.sleep(15)

    test_java19_notification_to_server()
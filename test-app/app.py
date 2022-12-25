import requests
import concurrent.futures
import time


def send_notification_to_related_server(server_url: str, notification_count: int) -> bool:
    try:
        response = requests.post(url=server_url, json={"deviceId": "a7a753cb-1092-45c1-a54a-c594e6f3e558"}, timeout=1)
        if response.status_code != 200:
            print("Error occurred for server url : {} at notification count: ".format(server_url), notification_count)
    except Exception as ex:
        print("Error occurred for server url : {} at notification count: ".format(server_url), notification_count, ex)


def send_notification_to_server(server_name: str, server_url: str, total_notification_count: int):
    print("Sending {} notification to server: {} with url: {}", format(server_name, server_url, total_notification_count))

    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        for notification_count in range(total_notification_count):
            executor.submit(send_notification_to_related_server, server_url, notification_count)
        
    print("{} server successfuly handled {} notification request".format(server_name, total_notification_count))

def test_java8_notification_to_server():
    start = time.time()
    send_notification_to_server("Java 8", "http://localhost:8081/send/", 15000)
    end = time.time()

    print("Total execution time for server name: {} is : {}".format("Java 8", (end - start)))

def test_java19_notification_to_server():
    start = time.time()
    send_notification_to_server("Java 19", "http://localhost:8082/send/", 15000)
    end = time.time()

    print("Total execution time for server name: {} is : {}".format("Java 19", (end - start)))

if __name__ == "__main__":
    
    test_java8_notification_to_server()
    
    time.sleep(15)

    test_java19_notification_to_server()
from fastapi import FastAPI
from pydantic import BaseModel, Field
import requests
import asyncio
import os

app = FastAPI()

MOCK_JAVA8_NOTIFICATION_SERVER_URL = os.getenv("notification.sender-server.java8.url", "http://localhost:7004/send/")
MOCK_GOLANG_NOTIFICATION_SERVER_URL = os.getenv("notification.sender-server.golang.url", "http://localhost:7005/send/")


SLEEP_TIME = int(os.getenv("server.sleepTime", 200)) / 1000


class NotificationSendRequestModel(BaseModel):
    device_id: str = Field(..., alias="deviceId")


class NotificationSendResponseModel(BaseModel):
    success: bool


async def send_notification(device_id: str, send_notification_url: str) -> dict:
    await asyncio.sleep(SLEEP_TIME)
    payload = {"deviceId": device_id}

    response = requests.post(send_notification_url, json=payload)

    if response.status_code == 200:
        return {"success": True}

    return {"success": False}


@app.post("/send/", response_model=NotificationSendResponseModel)
async def read_results(notification_send_request_model: NotificationSendRequestModel):
    send_notification_task = asyncio.create_task(
        send_notification(
            notification_send_request_model.device_id,
            MOCK_JAVA8_NOTIFICATION_SERVER_URL,
        )
    )

    return await send_notification_task


@app.post("/send/golang/", response_model=NotificationSendResponseModel)
async def read_results(notification_send_request_model: NotificationSendRequestModel):
    send_notification_task = asyncio.create_task(
        send_notification(
            notification_send_request_model.device_id,
            MOCK_GOLANG_NOTIFICATION_SERVER_URL,
        )
    )

    return await send_notification_task

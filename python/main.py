from fastapi import FastAPI
from pydantic import BaseModel, Field
import requests
import asyncio

app = FastAPI()

MOCK_NOTIFICATION_SERVER_URL = "http://mock-notification-sender:7004/send/"


class NotificationSendRequestModel(BaseModel):
    device_id: str = Field(..., alias="deviceId")


class NotificationSendResponseModel(BaseModel):
    success: bool


async def send_notification(device_id: str) -> dict:
    await asyncio.sleep(0.2)
    payload = {"deviceId": device_id}

    response = requests.post(MOCK_NOTIFICATION_SERVER_URL, json=payload)

    if response.status_code == 200:
        return {"success": True}

    return {"success": False}


@app.post("/send/", response_model=NotificationSendResponseModel)
async def read_results(notification_send_request_model: NotificationSendRequestModel):
    send_notification_task = asyncio.create_task(
        send_notification(notification_send_request_model.device_id)
    )

    return await send_notification_task

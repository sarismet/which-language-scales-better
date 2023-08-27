use axum::{http::StatusCode, response::IntoResponse, routing::post, Json, Router};
use lazy_static::lazy_static;
use serde::{Deserialize, Serialize};
use std::env;

lazy_static! {
    static ref SLEEP_TIME: u64 = {
        let sleep_time_var_name = "server.sleepTime";
        env::var(sleep_time_var_name)
            .unwrap_or("200".to_string())
            .parse::<u64>()
            .unwrap()
    };
}

#[derive(Debug, Serialize, Deserialize)]
struct SendNotificationRequest {
    #[serde(rename = "deviceId")]
    device_id: String,
}

#[derive(Debug, Serialize, Deserialize)]
struct NotificationResponse {
    success: bool,
}

#[tokio::main]
async fn main() {
    let app = Router::new()
        .route("/send/itself/", post(send_notification_to_itself))
        .route("/send/java/", post(send_notification_to_java))
        .route("/send/golang/", post(send_notification_to_golang));

    println!("axum server is running at port 7004");

    axum::Server::bind(&"0.0.0.0:7004".parse().unwrap())
        .serve(app.into_make_service())
        .await
        .unwrap();
}

async fn send_notification_to_itself() -> impl IntoResponse {
    let _send_java8_notification_url: std::string::String =
        std::env::var("notification.sender-server.java8.url")
            .unwrap_or("http://localhost:7100/send/".to_string());

    match send_notification_to_server_itself().await {
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            return (
                StatusCode::INTERNAL_SERVER_ERROR,
                Json(NotificationResponse { success: false }),
            );
        }
        Ok(notification_result) => return (StatusCode::OK, Json(notification_result)),
    }
}

async fn send_notification_to_java(
    Json(_send_notification_request): Json<SendNotificationRequest>,
) -> impl IntoResponse {
    let _send_java8_notification_url: std::string::String =
        std::env::var("notification.sender-server.java8.url")
            .unwrap_or("http://localhost:7100/send/".to_string());

    match send_notification_to_server_tokio(
        _send_notification_request,
        _send_java8_notification_url,
    )
    .await
    {
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            return (
                StatusCode::INTERNAL_SERVER_ERROR,
                Json(NotificationResponse { success: false }),
            );
        }
        Ok(notification_result) => return (StatusCode::OK, Json(notification_result)),
    }
}

async fn send_notification_to_golang(
    Json(_send_notification_request): Json<SendNotificationRequest>,
) -> impl IntoResponse {
    let _send_golang_notification_url: std::string::String =
        std::env::var("notification.sender-server.java8.url")
            .unwrap_or("http://localhost:7101/send/".to_string());

    match send_notification_to_server_tokio(
        _send_notification_request,
        _send_golang_notification_url,
    )
    .await
    {
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            return (
                StatusCode::INTERNAL_SERVER_ERROR,
                Json(NotificationResponse { success: false }),
            );
        }
        Ok(notification_result) => return (StatusCode::OK, Json(notification_result)),
    }
}

async fn send_notification_to_server_tokio(
    _send_notification_request: SendNotificationRequest,
    _send_notification_url: std::string::String,
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {
    let _res = tokio::spawn({
        reqwest::Client::new()
            .post(&_send_notification_url)
            .json(&_send_notification_request)
            .send()
    });

    let response_result = _res.await.unwrap();

    let result = match response_result {
        Ok(notification_result) => {
            println!("notification_result {:?}", notification_result);

            notification_result.json::<NotificationResponse>().await?
        }
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            NotificationResponse { success: false }
        }
    };

    return Ok(result);
}

async fn send_notification_to_server_itself(
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {
    let _res = tokio::spawn({
        let sleep_future: tokio::time::Sleep =
            tokio::time::sleep(tokio::time::Duration::from_millis(*SLEEP_TIME));

        sleep_future
    });

    let _ = _res.await.unwrap();

    return Ok(NotificationResponse { success: true });
}

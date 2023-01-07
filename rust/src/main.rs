use axum::{
    routing::post,
    http::StatusCode,
    response::IntoResponse,
    Json, Router,
};
use std::net::SocketAddr;
use serde::{Deserialize, Serialize};
use std::env;

#[derive(Debug, Serialize, Deserialize)]
struct SendNotificationRequest {
    #[serde(rename = "deviceId")]
    device_id: String
}

#[derive(Debug, Serialize, Deserialize)]
struct NotificationResponse {
    success: bool
}

#[tokio::main]
async fn main() {
    let app = Router::new()
                .route("/send/", post(send_notification_to_java))
                .route("/send/golang/", post(send_notification_to_golang));
    let addr = SocketAddr::from(([127, 0, 0, 1], 7005));

    println!("axum server is running at port 7005");

    axum::Server::bind(&addr)
        .serve(app.into_make_service())
        .await
        .unwrap();
}

async fn send_notification_to_java(
    Json(_send_notification_request): Json<SendNotificationRequest>,
) -> impl IntoResponse {
    
    //let SEND_JAVA8_NOTIFICATION_URL: &'static std::string::String = &std::env::var("notification.sender-server.java8.url").unwrap_or("http://localhost:7100/send/".to_string());

    let SEND_JAVA8_NOTIFICATION_URL: &'static str = "http://localhost:7100/send/";

    match send_notification_to_server(_send_notification_request, SEND_JAVA8_NOTIFICATION_URL).await {
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            return (StatusCode::INTERNAL_SERVER_ERROR, Json(NotificationResponse { 
                success: false
            })) 
        }
        ,
        Ok(notification_result) => return (StatusCode::CREATED, Json(notification_result)) 
    } 
}

async fn send_notification_to_golang(
    Json(_send_notification_request): Json<SendNotificationRequest>,
) -> impl IntoResponse {

    //let SEND_GOLANG_NOTIFICATION_URL: &'static std::string::String = &std::env::var("notification.sender-server.golang.url").unwrap_or("http://localhost:7101/send/".to_string());

    let SEND_GOLANG_NOTIFICATION_URL: &'static str = "http://localhost:7101/send/";

    match send_notification_to_server(_send_notification_request, SEND_GOLANG_NOTIFICATION_URL).await {
        Err(error) => {
            println!("Error occurred while sending notificatiton {:?}", error);

            return (StatusCode::INTERNAL_SERVER_ERROR, Json(NotificationResponse { 
                success: false
            })) 
        }
        ,
        Ok(notification_result) => return (StatusCode::CREATED, Json(notification_result)) 
    } 
}

async fn send_notification_to_server(
    _send_notification_request: SendNotificationRequest, _send_notification_url: &str
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {

    let _res = reqwest::Client::new()
        .post(_send_notification_url)
        .json(&_send_notification_request)
        .send()
        .await?;

    let _notification_response = _res
        .json::<NotificationResponse>()
        .await?;

    Ok(_notification_response)
}
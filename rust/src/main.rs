use axum::{http::StatusCode, response::IntoResponse, routing::post, Json, Router};
use serde::{Deserialize, Serialize};

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
        .route("/send/", post(send_notification_to_java))
        .route("/send/golang/", post(send_notification_to_golang));

    println!("axum server is running at port 7004");

    axum::Server::bind(&"127.0.0.1:7004".parse().unwrap())
        .serve(app.into_make_service())
        .await
        .unwrap();
}

async fn send_notification_to_java(
    Json(_send_notification_request): Json<SendNotificationRequest>,
) -> impl IntoResponse {
    let _send_java8_notification_url: std::string::String =
        std::env::var("notification.sender-server.java8.url")
            .unwrap_or("http://localhost:7100/send/".to_string());

    println!(
        "_send_java8_notification_url {:?}",
        _send_java8_notification_url
    );

    match send_notification_to_server_tokio_join(
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

    println!(
        "_send_golang_notification_url {:?}",
        _send_golang_notification_url
    );

    match send_notification_to_server_tokio_join(
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

async fn send_notification_to_server(
    _send_notification_request: SendNotificationRequest,
    _send_notification_url: &std::string::String,
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {
    let sleep_time_env: std::string::String =
        std::env::var("server.sleepTime").unwrap_or("200".to_string());
    let sleep_time = sleep_time_env.parse::<u64>().unwrap();
    let sleep_time_duration = std::time::Duration::from_millis(sleep_time);

    std::thread::sleep(sleep_time_duration);

    let _res = reqwest::Client::new()
        .post(_send_notification_url)
        .json(&_send_notification_request)
        .send()
        .await?;

    let _notification_response = _res.json::<NotificationResponse>().await?;

    Ok(_notification_response)
}

async fn send_notification_to_server_tokio(
    _send_notification_request: SendNotificationRequest,
    _send_notification_url: std::string::String,
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {
    let _res = tokio::spawn({
        println!("I am in the tokio spawn");

        tokio::time::sleep(tokio::time::Duration::from_millis(20000)).await;

        println!("sleeping is over");

        println!("_send_notification_url {:?}", &_send_notification_url);

        println!(
            "_send_notification_request {:?}",
            &_send_notification_request
        );

        reqwest::Client::new()
            .post(&_send_notification_url)
            .json(&_send_notification_request)
            .send()
    });

    if _res.is_finished() {
        print!("Bittiii");
    }

    let s = _res.await.unwrap();

    let result = match s {
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

async fn send_notification_to_server_tokio_join(
    _send_notification_request: SendNotificationRequest,
    _send_notification_url: std::string::String,
) -> Result<NotificationResponse, Box<dyn std::error::Error>> {
    let my_task = tokio::join!({
        println!("I am in the tokio spawn");

        tokio::time::sleep(tokio::time::Duration::from_millis(200)).await;

        println!("sleeping is over");

        println!("_send_notification_url {:?}", &_send_notification_url);

        println!(
            "_send_notification_request {:?}",
            &_send_notification_request
        );

        reqwest::Client::new()
            .post(&_send_notification_url)
            .json(&_send_notification_request)
            .send()
    });

    let result = match my_task.0 {
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

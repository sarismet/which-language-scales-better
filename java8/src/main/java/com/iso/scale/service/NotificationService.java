package com.iso.scale.service;

import java.util.concurrent.CompletableFuture;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NotificationService {

    private static final String SEND_NOTIFICATION_URL = "http://mock-notification-sender:7004/send/";

    private final RestTemplate restTemplate;

    private final TaskExecutor taskExecutor;

    public NotificationService(final RestTemplate restTemplate, final TaskExecutor taskExecutor) {
        this.restTemplate = restTemplate;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<NotificationResponse> sendAsyncNotification(
        final SendNotificationRequest sendNotificationRequest) {

        return CompletableFuture.supplyAsync(() -> sendSyncNotification(sendNotificationRequest), taskExecutor);
    }

    public NotificationResponse sendSyncNotification(final SendNotificationRequest sendNotificationRequest) {
        log.trace("Sending push to device with id: {}", sendNotificationRequest.getDeviceId());

        final HttpEntity<SendNotificationRequest> request = new HttpEntity<>(sendNotificationRequest);
        final ResponseEntity<Boolean> response =
            restTemplate.postForEntity(SEND_NOTIFICATION_URL, request, Boolean.class);

        if (Boolean.TRUE.equals(response.getBody())) {
            return new NotificationResponse();
        } else {
            throw new RuntimeException("Push notification service is unavailable");
        }
    }

}

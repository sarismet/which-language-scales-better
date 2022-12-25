package com.iso.isoscale.service;

import com.iso.isoscale.model.NotificationResponse;
import com.iso.isoscale.model.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationService {

    private static final String SEND_NOTIFICATION_URL = "http://localhost:8084/send/";

    private final RestTemplate restTemplate;

    private final TaskExecutor taskExecutor;

    public NotificationService(final RestTemplate restTemplate, final TaskExecutor taskExecutor) {
        this.restTemplate = restTemplate;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<NotificationResponse> sendNotification(
            final SendNotificationRequest sendNotificationRequest) {

        return CompletableFuture.supplyAsync(() -> {
            log.trace("Sending push to device with id: {}", sendNotificationRequest.getDeviceId());

            final HttpEntity<SendNotificationRequest> request = new HttpEntity<>(sendNotificationRequest);
            final ResponseEntity<Boolean> response =
                    restTemplate.postForEntity(SEND_NOTIFICATION_URL, request, Boolean.class);

            if (Boolean.TRUE.equals(response.getBody())) {
                return new NotificationResponse();
            } else {
                throw new RuntimeException("Push notification service is unavailable");
            }
        }, taskExecutor);

    }

}

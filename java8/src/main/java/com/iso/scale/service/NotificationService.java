package com.iso.scale.service;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    private final TaskExecutor taskExecutor;

    @Value("${server.sleepTime}")
    private long sleepTime;

    public NotificationService(final RestTemplate restTemplate, final TaskExecutor taskExecutor) {
        this.restTemplate = restTemplate;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<NotificationResponse> sendAsyncNotification(
            final String serverUrl, final SendNotificationRequest sendNotificationRequest) {

        return CompletableFuture.supplyAsync(
                () -> sendSyncNotification(serverUrl, sendNotificationRequest), taskExecutor
        );
    }

    public NotificationResponse sendSyncNotification(final String serverUrl,
                                                     final SendNotificationRequest sendNotificationRequest) {
        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();

            log.error("Error occurred while sleeping in thread");
        }

        log.trace("Sending push to device with id: {}", sendNotificationRequest.getDeviceId());

        final HttpEntity<SendNotificationRequest> request = new HttpEntity<>(sendNotificationRequest);
        final ResponseEntity<Boolean> response = restTemplate.postForEntity(serverUrl, request, Boolean.class);

        if (Boolean.TRUE.equals(response.getBody())) {
            return new NotificationResponse();
        } else {
            throw new RuntimeException("Push notification service is unavailable");
        }
    }

}

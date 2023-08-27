package com.iso.scale.service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
                () -> sendSyncNotification(serverUrl, sendNotificationRequest), taskExecutor);
    }

    public CompletableFuture<NotificationResponse> sendAsyncNotificationItself() {

        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();

                        log.error("Error occurred while sleeping in thread");
                    }

                    final NotificationResponse notificationResponse = new NotificationResponse();
                    notificationResponse.setSuccess(true);

                    return notificationResponse;
                }, taskExecutor);
    }

    public NotificationResponse sendSyncNotification(final String serverUrl,
            final SendNotificationRequest sendNotificationRequest) {

        log.trace("Sending push to device with id: {}", sendNotificationRequest.getDeviceId());

        final HttpEntity<SendNotificationRequest> request = new HttpEntity<>(sendNotificationRequest);
        final ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(serverUrl, request,
                NotificationResponse.class);

        final NotificationResponse notificationResponse = response.getBody();

        if (Objects.nonNull(response.getBody()) && response.getBody().isSuccess()) {
            return notificationResponse;
        } else {
            throw new RuntimeException("Push notification service is unavailable");
        }
    }

}

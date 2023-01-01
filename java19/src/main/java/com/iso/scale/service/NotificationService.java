package com.iso.scale.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NotificationService {

    private static final String SEND_NOTIFICATION_URL = "http://mock-java8-notification-sender:7004/send/";

    private final RestTemplate restTemplate;

    @Value("${server.sleepTime}")
    private long sleepTime;

    public NotificationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture<NotificationResponse> sendAsyncNotification(
        final SendNotificationRequest sendNotificationRequest) {

        final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        return CompletableFuture.supplyAsync(() -> sendSyncNotification(sendNotificationRequest), executor);

    }

    public NotificationResponse sendSyncNotification(final SendNotificationRequest sendNotificationRequest) {
        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();

            log.error("Error occurred while sleeping in thread");
        }

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

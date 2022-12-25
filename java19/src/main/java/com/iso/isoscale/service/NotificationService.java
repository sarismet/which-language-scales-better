package com.iso.isoscale.service;

import com.iso.isoscale.model.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NotificationService {

    private final RestTemplate restTemplate;


    public NotificationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture<NotificationResponse> sendNotification(final String deviceId) {
        final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        return CompletableFuture.supplyAsync(() -> {
            log.trace("Sending push to device: {}", deviceId);

            return new NotificationResponse();
        }, executor);

    }

}

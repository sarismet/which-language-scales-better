package com.iso.isoscale.service;

import com.iso.isoscale.model.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    private final TaskExecutor taskExecutor;

    public NotificationService(final RestTemplate restTemplate, final TaskExecutor taskExecutor) {
        this.restTemplate = restTemplate;
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<NotificationResponse> sendNotification(final String deviceId) {

        return CompletableFuture.supplyAsync(() -> {
            log.trace("Sending push");
            return new NotificationResponse();
        }, taskExecutor);

    }

}

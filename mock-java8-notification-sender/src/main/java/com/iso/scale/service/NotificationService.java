package com.iso.scale.service;

import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

import com.iso.scale.model.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final TaskExecutor taskExecutor;

    @Value("${server.sleepTime}")
    private long sleepTime;

    public NotificationService(final TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<NotificationResponse> sendNotification(@NotNull final String deviceId) {
        return CompletableFuture.supplyAsync(
            () -> {
                try {
                    Thread.sleep(sleepTime);
                } catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();

                    log.error("Error occurred while sleeping in thread");
                }

                log.trace("Sending push to device with id: {}", deviceId);
                final NotificationResponse notificationResponse = new NotificationResponse();

                notificationResponse.setSuccess(true);

                return notificationResponse;
            }, taskExecutor
        );
    }

}

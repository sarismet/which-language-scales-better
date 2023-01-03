package com.iso.scale.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationService {

    private final TaskExecutor taskExecutor;

    @Value("${server.sleepTime}")
    private long sleepTime;

    public NotificationService(final TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public CompletableFuture<Boolean> sendNotification(@NotNull final String deviceId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();

                        log.error("Error occurred while sleeping in thread");
                    }

                    log.trace("Sending push to device with id: {}", deviceId);

                    return true;
                }, taskExecutor
        );
    }

}

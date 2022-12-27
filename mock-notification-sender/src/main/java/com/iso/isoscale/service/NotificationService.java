package com.iso.isoscale.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationService {

    private final TaskExecutor taskExecutor;


    public NotificationService(final TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public boolean sendNotification(@NotNull  final String deviceId) {


        try {
            Thread.sleep(500);
        } catch (final InterruptedException ex) {
            log.error("Error occurred while sleeping in thread");
        }

            log.trace("Sending push to device with id: {}", deviceId);

            return true;

    }

}

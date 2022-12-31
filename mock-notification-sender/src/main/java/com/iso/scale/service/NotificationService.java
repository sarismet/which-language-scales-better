package com.iso.scale.service;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Value("${server.sleepTime}")
    private long sleepTime;

    public boolean sendNotification(@NotNull final String deviceId) {

        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Error occurred while sleeping in thread");
        }
        log.trace("Sending push to device with id: {}", deviceId);

        return true;
    }

}

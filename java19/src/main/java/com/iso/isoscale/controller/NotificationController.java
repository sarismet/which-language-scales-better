package com.iso.isoscale.controller;

import com.iso.isoscale.model.NotificationResponse;
import com.iso.isoscale.service.NotificationService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Objects;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
    this.notificationService = notificationService;
    }

    @PostMapping("/send/{deviceId}")
    public DeferredResult<NotificationResponse> sendNotification(
            @PathVariable("deviceId") @Nonnull final String deviceId) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendNotification(deviceId).whenComplete((result, ex) -> {
           if (Objects.nonNull(ex)) {
               log.error("Error occurred while saving an audit log", ex);

               final NotificationResponse errorResponse = new NotificationResponse();
               errorResponse.setSuccess(false);
               deferredResult.setErrorResult(errorResponse);
           }

           deferredResult.setResult(result);
        });

        return deferredResult;
    }
}

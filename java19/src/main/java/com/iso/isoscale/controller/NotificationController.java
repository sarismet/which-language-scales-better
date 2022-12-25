package com.iso.isoscale.controller;

import com.iso.isoscale.model.NotificationResponse;
import com.iso.isoscale.model.SendNotificationRequest;
import com.iso.isoscale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


import java.util.Objects;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send/")
    public DeferredResult<NotificationResponse> sendNotification(
             @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendNotification(sendNotificationRequest).whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error("Error occurred while sending push notification", ex);

                final NotificationResponse errorResponse = new NotificationResponse();
                errorResponse.setSuccess(false);
                deferredResult.setErrorResult(errorResponse);
            }

            deferredResult.setResult(result);
        });

        return deferredResult;
    }
}

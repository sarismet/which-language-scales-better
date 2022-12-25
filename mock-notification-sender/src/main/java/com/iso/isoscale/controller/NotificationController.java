package com.iso.isoscale.controller;

import com.iso.isoscale.model.SendNotificationRequest;
import com.iso.isoscale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send/")
    public DeferredResult<Boolean> sendNotification(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<Boolean> deferredResult = new DeferredResult<>();

        this.notificationService.sendNotification(sendNotificationRequest.getDeviceId()).whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error("Error occurred while sending push notification", ex);

                deferredResult.setErrorResult(new RuntimeException(ex.getMessage()));
            }

            deferredResult.setResult(result);
        });

        return deferredResult;
    }
}

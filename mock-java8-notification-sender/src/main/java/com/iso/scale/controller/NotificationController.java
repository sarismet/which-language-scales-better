package com.iso.scale.controller;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import com.iso.scale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public DeferredResult<NotificationResponse> sendNotification(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendNotification(
                sendNotificationRequest.getDeviceId()
        ).whenComplete((result, ex) -> {
            if (Objects.nonNull(ex)) {
                log.error("Error occurred while sending push notification", ex);

                deferredResult.setErrorResult(ex);
            }

            deferredResult.setResult(result);
        });

        return deferredResult;
    }

}

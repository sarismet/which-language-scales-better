package com.iso.scale.controller;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import com.iso.scale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send/")
    public NotificationResponse sendNotification(@RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService.sendAsyncNotification(sendNotificationRequest).join();
    }

    @PostMapping("/send/v2/")
    public DeferredResult<NotificationResponse> sendNotificationV2(
        @RequestBody final SendNotificationRequest sendNotificationRequest) {
        final DeferredResult<NotificationResponse> result = new DeferredResult<>();

        final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        CompletableFuture
            .runAsync(() -> {
                final NotificationResponse notificationResponse =
                    this.notificationService.sendSyncNotification(sendNotificationRequest);

                result.setResult(notificationResponse);
            }, executor)
            .exceptionally(ex -> {
                result.setErrorResult(ex.getCause());

                return null;
            });

        return result;
    }

    @PostMapping("/send/v3/")
    public DeferredResult<NotificationResponse> sendNotificationV3(
        @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendAsyncNotification(sendNotificationRequest).whenComplete((result, ex) -> {
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

package com.iso.scale.controller;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import com.iso.scale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    private final TaskExecutor taskExecutor;

    public NotificationController(final NotificationService notificationService, final TaskExecutor taskExecutor) {
        this.notificationService = notificationService;
        this.taskExecutor = taskExecutor;
    }

    @PostMapping("/send/")
    public NotificationResponse sendNotification(
        @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService.sendAsyncNotification(sendNotificationRequest).join();
    }

    @PostMapping("/send/v2/")
    public DeferredResult<NotificationResponse> sendNotificationV2(
        @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {
        final DeferredResult<NotificationResponse> result = new DeferredResult<>();

        CompletableFuture
            .runAsync(() -> {
                final NotificationResponse notificationResponse =
                    this.notificationService.sendSyncNotification(sendNotificationRequest);

                result.setResult(notificationResponse);
            }, taskExecutor)
            .exceptionally(ex -> {
                result.setErrorResult(ex.getCause());

                return null;
            });

        return result;
    }

    @PostMapping("/send/v3/")
    public DeferredResult<NotificationResponse> sendNotificationV3(
        @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

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

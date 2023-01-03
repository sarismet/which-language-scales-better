package com.iso.scale.controller;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import com.iso.scale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class NotificationController {

    private static final String SEND_NOTIFICATION_TO_JAVA8_SERVER_URL = "http://mock-java8-notification-sender:7004/send/";

    private static final String SEND_NOTIFICATION_TO_GOLANG_SERVER_URL = "http://mock-golang-notification-sender:7005/send/";

    private final NotificationService notificationService;

    private final TaskExecutor taskExecutor;

    public NotificationController(final NotificationService notificationService, final TaskExecutor taskExecutor) {
        this.notificationService = notificationService;
        this.taskExecutor = taskExecutor;
    }

    @PostMapping("/send/")
    public NotificationResponse sendNotificationToJava8Server(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService
                .sendAsyncNotification(SEND_NOTIFICATION_TO_JAVA8_SERVER_URL, sendNotificationRequest)
                .join();
    }

    @PostMapping("/send/golang/")
    public NotificationResponse sendNotificationToGolangServer(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService
                .sendAsyncNotification(SEND_NOTIFICATION_TO_GOLANG_SERVER_URL, sendNotificationRequest)
                .join();
    }

    @PostMapping("/send/v2/")
    public DeferredResult<NotificationResponse> sendNotificationToJava8ServerV2(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {
        final DeferredResult<NotificationResponse> result = new DeferredResult<>();

        CompletableFuture
                .runAsync(() -> {
                    final NotificationResponse notificationResponse =
                            this.notificationService.sendSyncNotification(SEND_NOTIFICATION_TO_JAVA8_SERVER_URL,
                                    sendNotificationRequest
                            );

                    result.setResult(notificationResponse);
                }, taskExecutor)
                .exceptionally(ex -> {
                    result.setErrorResult(ex.getCause());

                    return null;
                });

        return result;
    }

    @PostMapping("/send/golang/v2/")
    public DeferredResult<NotificationResponse> sendNotificationToGolangServerV2(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {
        final DeferredResult<NotificationResponse> result = new DeferredResult<>();

        CompletableFuture
                .runAsync(() -> {
                    final NotificationResponse notificationResponse =
                            this.notificationService.sendSyncNotification(
                                    SEND_NOTIFICATION_TO_GOLANG_SERVER_URL, sendNotificationRequest
                            );

                    result.setResult(notificationResponse);
                }, taskExecutor)
                .exceptionally(ex -> {
                    result.setErrorResult(ex.getCause());

                    return null;
                });

        return result;
    }

    @PostMapping("/send/v3/")
    public DeferredResult<NotificationResponse> sendNotificationToJava8ServerV3(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendAsyncNotification(
                SEND_NOTIFICATION_TO_JAVA8_SERVER_URL, sendNotificationRequest
        ).whenComplete((result, ex) -> {
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

    @PostMapping("/send/golang/v3/")
    public DeferredResult<NotificationResponse> sendNotificationToGolangServerV3(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendAsyncNotification(
                SEND_NOTIFICATION_TO_GOLANG_SERVER_URL, sendNotificationRequest
        ).whenComplete((result, ex) -> {
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

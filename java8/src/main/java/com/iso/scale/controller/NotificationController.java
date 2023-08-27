package com.iso.scale.controller;

import com.iso.scale.model.NotificationResponse;
import com.iso.scale.model.SendNotificationRequest;
import com.iso.scale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final NotificationService notificationService;

    private final TaskExecutor taskExecutor;

    @Value("${notification.sender-server.java8.url}")
    private String sendNotificationToJava8ServerUrl;

    @Value("${notification.sender-server.golang.url}")
    private String getSendNotificationToGolangServerUrl;

    public NotificationController(final NotificationService notificationService, final TaskExecutor taskExecutor) {
        this.notificationService = notificationService;
        this.taskExecutor = taskExecutor;
    }

    @PostMapping("/send/itself/")
    public NotificationResponse sendNotificationItself(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService
                .sendAsyncNotificationItself()
                .join();
    }

    @PostMapping("/send/java/")
    public NotificationResponse sendNotificationToJava8Server(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService
                .sendAsyncNotification(sendNotificationToJava8ServerUrl, sendNotificationRequest)
                .join();
    }

    @PostMapping("/send/golang/")
    public NotificationResponse sendNotificationToGolangServer(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService
                .sendAsyncNotification(getSendNotificationToGolangServerUrl, sendNotificationRequest)
                .join();
    }

    @PostMapping("/send/java/v2/")
    public DeferredResult<NotificationResponse> sendNotificationToJava8ServerV2(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {
        final DeferredResult<NotificationResponse> result = new DeferredResult<>();

        CompletableFuture
                .runAsync(() -> {
                    final NotificationResponse notificationResponse = this.notificationService.sendSyncNotification(
                            sendNotificationToJava8ServerUrl, sendNotificationRequest);

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
                    final NotificationResponse notificationResponse = this.notificationService.sendSyncNotification(
                            getSendNotificationToGolangServerUrl, sendNotificationRequest);

                    result.setResult(notificationResponse);
                }, taskExecutor)
                .exceptionally(ex -> {
                    result.setErrorResult(ex.getCause());

                    return null;
                });

        return result;
    }

    @PostMapping("/send/java/v3/")
    public DeferredResult<NotificationResponse> sendNotificationToJava8ServerV3(
            @Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        final DeferredResult<NotificationResponse> deferredResult = new DeferredResult<>();

        this.notificationService.sendAsyncNotification(
                sendNotificationToJava8ServerUrl, sendNotificationRequest).whenComplete((result, ex) -> {
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
                getSendNotificationToGolangServerUrl, sendNotificationRequest).whenComplete((result, ex) -> {
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

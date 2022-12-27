package com.iso.isoscale.controller;

import javax.validation.Valid;

import com.iso.isoscale.model.SendNotificationRequest;
import com.iso.isoscale.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send/")
    public boolean sendNotification(@Valid @RequestBody final SendNotificationRequest sendNotificationRequest) {

        return this.notificationService.sendNotification(sendNotificationRequest.getDeviceId());
    }

}

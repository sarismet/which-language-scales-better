package com.iso.scale.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SendNotificationRequest {

    @NotNull
    private String deviceId;
}

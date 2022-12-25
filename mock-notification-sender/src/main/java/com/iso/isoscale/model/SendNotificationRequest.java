package com.iso.isoscale.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SendNotificationRequest {

    @NotNull
    private String deviceId;
}

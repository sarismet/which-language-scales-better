package com.iso.isoscale.model;

import jakarta.annotation.Nonnull;
import lombok.Data;


@Data
public class SendNotificationRequest {

    @Nonnull
    private String deviceId;
}

package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDeliveryRequest {
    @NotNull
    private Long orderId;
    @NotBlank
    private String securityPin;
}

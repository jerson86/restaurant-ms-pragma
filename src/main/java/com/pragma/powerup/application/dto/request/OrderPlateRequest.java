package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlateRequest {
    @NotNull
    private Long plateId;
    @Min(1)
    private Integer quantity;
}

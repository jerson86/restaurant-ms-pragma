package com.pragma.powerup.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull
    @Valid
    @Size(min = 1, message = "El pedido debe contener al menos un plato.")
    private List<OrderPlateRequest> plates;
}

package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UpdatePlateRequest {

    @NotNull(message = "El ID del plato es obligatorio.")
    private Long id;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser un valor positivo.")
    private BigDecimal price;

    @NotBlank(message = "La descripción del plato es obligatoria.")
    private String description;
}
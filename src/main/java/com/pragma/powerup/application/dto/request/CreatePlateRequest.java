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
public class CreatePlateRequest {

    @NotBlank(message = "El nombre del plato es obligatorio.")
    private String name;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    private Long categoryId;

    @NotBlank(message = "La descripción del plato es obligatoria.")
    private String description;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser un valor positivo.")
    private BigDecimal price;

    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String imageUrl;

    @NotNull(message = "El ID del restaurante asociado es obligatorio.")
    private Long restaurantId;
}

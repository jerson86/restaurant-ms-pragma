package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class CreateRestaurantRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "NIT debe ser numérico")
    private String nit;

    @NotBlank
    private String direccion;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = "El telefono debe tener máximo 13 dígitos y puede iniciar con +")
    private String telefono;

    @NotBlank
    private String urlLogo;

    @NotNull
    private Long userId;
}

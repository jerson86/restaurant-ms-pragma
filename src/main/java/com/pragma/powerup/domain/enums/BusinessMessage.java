package com.pragma.powerup.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessMessage {
    RESTAURANT_NIT_ALREADY_EXISTS("El NIT ya se encuentra registrado"),
    RESTAURANT_USER_ID_NOT_EXISTS("El usuario no existe"),
    RESTAURANT_ID_NOT_EXISTS("El restaurante no existe"),
    PLATE_PRICE_MUST_BE_POSITIVE("El precio del plato debe ser positivo"),
    PLATE_NOT_FOUND("El plato no existe"),
    PLATE_IN_PROCESS("El cliente ya tiene un pedido en proceso (Pendiente, En Preparación o Listo)."),
    ORDER_NOT_FOUND("El pedido solicitado no existe."),
    ORDER_NOT_ACCESS_RESTAURANT("No tienes permiso para asignar un pedido de otro restaurante."),
    ORDER_IS_ONLY_STATUS_PENDING("Solo se pueden asignar pedidos que estén en estado PENDIENTE.");

    private final String message;
}

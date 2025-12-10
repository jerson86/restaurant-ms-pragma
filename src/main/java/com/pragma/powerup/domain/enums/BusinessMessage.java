package com.pragma.powerup.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessMessage {
    RESTAURANT_NIT_ALREADY_EXISTS("El NIT ya se encuentra registrado"),
    RESTAURANT_USER_ID_NOT_EXISTS("El usuario no existe");

    private final String message;
}

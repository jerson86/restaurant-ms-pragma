package com.pragma.powerup.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessMessage {
    RESTAURANT_NIT_ALREADY_EXISTS("El NIT ya se encuentra registrado");

    private final String message;
}

package com.pragma.powerup.domain.util;

import com.pragma.powerup.domain.enums.UserRole;
import com.pragma.powerup.domain.exception.CustomAccessDeniedException;
import com.pragma.powerup.domain.model.AuthenticatedUserModel;
import com.pragma.powerup.domain.spi.IUserRestPort;

public final class AuthValidator {
    private AuthValidator() {}

    public static Long validateUser(IUserRestPort userRestPort, String bearerToken) {
        AuthenticatedUserModel authenticatedUser = userRestPort.getAuthenticatedUser(bearerToken);

        if (!UserRole.ADMIN.name().equals(authenticatedUser.getRole())) {
            throw new CustomAccessDeniedException("Solo un usuario ADMINISTRADOR puede crear restaurantes.");
        }

        return authenticatedUser.getId();
    }
}

package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.AuthenticatedUserModel;
import com.pragma.powerup.domain.model.UserModel;

public interface IUserRestPort {
    UserModel getUserById(Long id);
    AuthenticatedUserModel getAuthenticatedUser(String bearerToken);
}

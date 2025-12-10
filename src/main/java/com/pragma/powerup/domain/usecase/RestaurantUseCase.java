package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserRestPort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort userPersistencePort;
    private final IUserRestPort userRestPort;

    public RestaurantUseCase(IRestaurantPersistencePort userPersistencePort, IUserRestPort userRestPort) {
        this.userPersistencePort = userPersistencePort;
        this.userRestPort = userRestPort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        if (userRestPort.getUserById(restaurantModel.getUserId()) == null) {
            throw new DomainException(BusinessMessage.RESTAURANT_USER_ID_NOT_EXISTS);
        }

        if (userPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new DomainException(BusinessMessage.RESTAURANT_NIT_ALREADY_EXISTS);
        }

        userPersistencePort.saveRestaurant(restaurantModel);
    }
}
package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort userPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {

        if (userPersistencePort.existsByNit(restaurantModel.getNit())) {
            throw new DomainException(BusinessMessage.RESTAURANT_NIT_ALREADY_EXISTS);
        }

        userPersistencePort.saveRestaurant(restaurantModel);
    }
}
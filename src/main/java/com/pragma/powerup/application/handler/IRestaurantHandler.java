package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;

public interface IRestaurantHandler {
    void saveRestaurant(CreateRestaurantRequest createRestaurantRequest);
}
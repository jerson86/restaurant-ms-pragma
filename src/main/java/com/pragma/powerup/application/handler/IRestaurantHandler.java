package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.dto.response.RestaurantClientResponse;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(CreateRestaurantRequest createRestaurantRequest, String bearerToken);
    List<RestaurantClientResponse> getAllRestaurants(int page, int size, String bearerToken);
}
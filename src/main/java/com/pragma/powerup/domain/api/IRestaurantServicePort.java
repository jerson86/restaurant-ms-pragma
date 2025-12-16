package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(RestaurantModel restaurantModel, String bearerToken);
    List<RestaurantModel> getAllRestaurants(int page, int size, String bearerToken);
}
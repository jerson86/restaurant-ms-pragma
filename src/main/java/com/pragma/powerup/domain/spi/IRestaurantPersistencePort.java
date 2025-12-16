package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantPersistencePort {
    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);
    boolean existsByNit(String nit);
    RestaurantModel findRestaurantById(Long restaurantId);
    List<RestaurantModel> getAllRestaurants(int offset, int limit);
}
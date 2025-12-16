package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.dto.request.RestaurantClientResponse;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper objectRequestMapper;

    @Override
    public void saveRestaurant(CreateRestaurantRequest createRestaurantRequest, String bearerToken) {
        RestaurantModel restaurantModel = objectRequestMapper.toObject(createRestaurantRequest);
        restaurantServicePort.saveRestaurant(restaurantModel, bearerToken);
    }

    @Override
    public List<RestaurantClientResponse> getAllRestaurants(int page, int size, String bearerToken) {
        List<RestaurantModel> restaurantList = restaurantServicePort.getAllRestaurants(page, size, bearerToken);

        return restaurantList.stream()
                .map(objectRequestMapper::toClientResponse)
                .toList();
    }
}
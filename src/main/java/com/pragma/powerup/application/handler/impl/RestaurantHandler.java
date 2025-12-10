package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort userServicePort;
    private final IRestaurantRequestMapper objectRequestMapper;

    @Override
    public void saveRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        RestaurantModel restaurantModel = objectRequestMapper.toObject(createRestaurantRequest);
        userServicePort.saveRestaurant(restaurantModel);
    }
}
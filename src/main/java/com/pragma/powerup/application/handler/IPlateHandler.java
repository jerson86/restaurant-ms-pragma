package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.PlateClientResponse;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;

import java.util.List;

public interface IPlateHandler {
    void savePlate(CreatePlateRequest createPlateRequest, String bearerToken);
    void updatePlate(UpdatePlateRequest request, String bearerToken);
    void enableDisablePlate(Long dishId, boolean enabled, String bearerToken);
    List<PlateClientResponse> getAllPlatesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String bearerToken);
}

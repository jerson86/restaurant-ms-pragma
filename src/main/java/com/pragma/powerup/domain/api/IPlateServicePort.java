package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PlateModel;

import java.util.List;

public interface IPlateServicePort {
    void savePlate(PlateModel plateModel, String bearerToken);
    void updatePlate(PlateModel plateModel, String bearerToken);
    void enableDisablePlate(Long plateId, boolean enabled, String bearerToken);
    List<PlateModel> getAllPlatesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String bearerToken);
}

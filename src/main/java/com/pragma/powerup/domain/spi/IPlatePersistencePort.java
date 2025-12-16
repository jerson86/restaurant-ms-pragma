package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PlateModel;

import java.util.List;

public interface IPlatePersistencePort {
    void savePlate(PlateModel plateModel);
    PlateModel getPlateById(Long plateId);
    void updatePlate(PlateModel plateModel);
    List<PlateModel> getPlateListByRestaurant(Long restaurantId, Long categoryId, int offset, int limit);
}

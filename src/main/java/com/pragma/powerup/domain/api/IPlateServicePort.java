package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PlateModel;

public interface IPlateServicePort {
    void savePlate(PlateModel plateModel);
    void updatePlate(PlateModel plateModel);
}

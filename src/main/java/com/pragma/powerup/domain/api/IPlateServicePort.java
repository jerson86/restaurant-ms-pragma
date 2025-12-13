package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PlateModel;

public interface IPlateServicePort {
    void savePlate(PlateModel plateModel, String bearerToken);
    void updatePlate(PlateModel plateModel, String bearerToken);
    void enableDisablePlate(Long plateId, boolean enabled, String bearerToken);
}

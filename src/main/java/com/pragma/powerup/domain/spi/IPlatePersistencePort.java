package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PlateModel;

public interface IPlatePersistencePort {
    void savePlate(PlateModel plateModel);
    PlateModel getPlateById(Long plateId);
}

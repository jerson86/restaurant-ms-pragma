package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

import java.math.BigDecimal;

public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void savePlate(PlateModel plateModel) {
        if (!restaurantPersistencePort.existsRestaurantById(plateModel.getRestaurantId())) {
            throw new DomainException(BusinessMessage.RESTAURANT_ID_NOT_EXISTS);
        }

        if (plateModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE);
        }

        plateModel.setActive(true);
        platePersistencePort.savePlate(plateModel);
    }

    @Override
    public void updatePlate(PlateModel plateModel) {
        PlateModel existingPlate = platePersistencePort.getPlateById(plateModel.getId());

        if (existingPlate == null) {
            throw new DomainException(BusinessMessage.PLATE_NOT_FOUND);
        }

        if (plateModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE);
        }

        existingPlate.setPrice(plateModel.getPrice());
        existingPlate.setDescription(plateModel.getDescription());

        platePersistencePort.savePlate(existingPlate);
    }
}
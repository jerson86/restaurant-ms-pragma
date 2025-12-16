package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.CustomAccessDeniedException;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserRestPort;
import com.pragma.powerup.domain.util.AuthValidator;

import java.math.BigDecimal;
import java.util.List;

public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserRestPort userRestPort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IUserRestPort userRestPort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userRestPort = userRestPort;
    }

    @Override
    public void savePlate(PlateModel plateModel, String bearerToken) {
        AuthValidator.validateUser(userRestPort, bearerToken);

        RestaurantModel restaurantModel = restaurantPersistencePort.findRestaurantById(plateModel.getRestaurantId());

        if (restaurantModel == null) {
            throw new DomainException(BusinessMessage.RESTAURANT_ID_NOT_EXISTS);
        }

        if (plateModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE);
        }

        plateModel.setActive(true);
        platePersistencePort.savePlate(plateModel);
    }

    @Override
    public void updatePlate(PlateModel plateModel, String bearerToken) {
        AuthValidator.validateUser(userRestPort, bearerToken);
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

    @Override
    public void enableDisablePlate(Long plateId, boolean enabled, String bearerToken) {
        Long userId = AuthValidator.validateUser(userRestPort, bearerToken);

        PlateModel existingPlate = platePersistencePort.getPlateById(plateId);

        if (existingPlate == null) {
            throw new DomainException(BusinessMessage.PLATE_NOT_FOUND);
        }

        Long restaurantId = existingPlate.getRestaurantId();
        RestaurantModel restaurantModel = restaurantPersistencePort.findRestaurantById(restaurantId);

        if (restaurantModel == null) {
            throw new DomainException(BusinessMessage.RESTAURANT_ID_NOT_EXISTS);
        }

        if (!userId.equals(restaurantModel.getUserId())) {
            throw new CustomAccessDeniedException("No eres el propietario del restaurante asociado a este plato.");
        }

        existingPlate.setActive(enabled);
        platePersistencePort.updatePlate(existingPlate);
    }

    @Override
    public List<PlateModel> getAllPlatesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String bearerToken) {
        AuthValidator.validateUser(userRestPort, bearerToken);
        int offset = page * size;
        return platePersistencePort.getPlateListByRestaurant(restaurantId, categoryId, offset, size);
    }
}
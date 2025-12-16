package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IUserRestPort;
import com.pragma.powerup.domain.util.AuthValidator;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderExternalServicePort;
    private final IPlatePersistencePort platePersistencePort;
    private final IUserRestPort userRestPort;

    public OrderUseCase(IOrderPersistencePort orderExternalServicePort, IPlatePersistencePort platePersistencePort, IUserRestPort userRestPort) {
        this.orderExternalServicePort = orderExternalServicePort;
        this.platePersistencePort = platePersistencePort;
        this.userRestPort = userRestPort;
    }

    @Override
    public void createOrder(OrderModel orderModel, String bearerToken) {
        Long userId = AuthValidator.validateUser(userRestPort, bearerToken);

        if (orderExternalServicePort.hasActiveOrder(userId)) {
            throw new DomainException(BusinessMessage.PLATE_IN_PROCESS);
        }

        Long restaurantId = platePersistencePort
                .getPlateById(orderModel.getPlates().getFirst().getPlateId())
                .getRestaurantId();

        orderModel.setClientId(userId);
        orderModel.setRestaurantId(restaurantId);

        orderExternalServicePort.createOrder(orderModel);
    }
}

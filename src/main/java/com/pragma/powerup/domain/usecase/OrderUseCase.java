package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.AuthenticatedUserModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserRestPort;
import com.pragma.powerup.domain.util.AuthValidator;

import java.util.List;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IPlatePersistencePort platePersistencePort;
    private final IUserRestPort userRestPort;
    private final IRestaurantPersistencePort userPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IPlatePersistencePort platePersistencePort, IUserRestPort userRestPort, IRestaurantPersistencePort userPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.platePersistencePort = platePersistencePort;
        this.userRestPort = userRestPort;
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void createOrder(OrderModel orderModel, String bearerToken) {
        Long userId = AuthValidator.validateUser(userRestPort, bearerToken);

        if (orderPersistencePort.hasActiveOrder(userId)) {
            throw new DomainException(BusinessMessage.PLATE_IN_PROCESS);
        }

        Long restaurantId = platePersistencePort
                .getPlateById(orderModel.getPlates().getFirst().getPlateId())
                .getRestaurantId();

        orderModel.setClientId(userId);
        orderModel.setRestaurantId(restaurantId);

        orderPersistencePort.createOrder(orderModel);
    }

    @Override
    public List<OrderModel> getOrdersByStatus(Long restaurantId, String status, int page, int size, String bearerToken) {
        AuthValidator.validateUser(userRestPort, bearerToken);
        int offset = page * size;

        RestaurantModel restaurantModel = userPersistencePort.findRestaurantById(restaurantId);

        if(restaurantModel == null) {
            throw new DomainException(BusinessMessage.RESTAURANT_ID_NOT_EXISTS);
        }

        return orderPersistencePort.getOrdersByStatusAndRestaurant(restaurantId, status, offset, size);
    }

    @Override
    public void assignOrder(Long orderId, String bearerToken) {
        AuthenticatedUserModel authenticatedUser = userRestPort.getAuthenticatedUser(bearerToken);

        OrderModel orderModel = orderPersistencePort.getOrderById(orderId);

        if (orderModel == null) {
            throw new DomainException(BusinessMessage.ORDER_NOT_FOUND);
        }

        if (!orderModel.getRestaurantId().equals(authenticatedUser.getRestaurantId())) {
            throw new DomainException(BusinessMessage.ORDER_NOT_ACCESS_RESTAURANT);
        }

        if (!orderModel.getStatus().equals("PENDIENTE")) {
            throw new DomainException(BusinessMessage.ORDER_IS_ONLY_STATUS_PENDING);
        }

        orderModel.setEmployeeId(authenticatedUser.getId());
        orderModel.setStatus("EN_PREPARACION");

        orderPersistencePort.saveOrder(orderModel);
    }
}

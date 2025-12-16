package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderPersistencePort {
    boolean hasActiveOrder(Long clientId);
    void createOrder(OrderModel orderModel);
    List<OrderModel> getOrdersByStatusAndRestaurant(Long restaurantId, String status, int offset, int limit);
}

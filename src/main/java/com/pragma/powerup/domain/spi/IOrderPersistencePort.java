package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderModel;

public interface IOrderPersistencePort {
    boolean hasActiveOrder(Long clientId);
    void createOrder(OrderModel orderModel);
}

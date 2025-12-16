package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;

import java.util.List;

public interface IOrderServicePort {
    void createOrder(OrderModel orderModel, String bearerToken);
    List<OrderModel> getOrdersByStatus(Long restaurantId, String status, int page, int size, String bearerToken);
}

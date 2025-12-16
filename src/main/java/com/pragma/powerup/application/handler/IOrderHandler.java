package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.application.dto.response.OrderResponse;

import java.util.List;

public interface IOrderHandler {
    void createOrder(OrderRequest request, String bearerToken);
    List<OrderResponse> getOrdersByStatus(Long restaurantId, String status, int page, int size, String bearerToken);
}

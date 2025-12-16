package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequest;

public interface IOrderHandler {
    void createOrder(OrderRequest request, String token);
}

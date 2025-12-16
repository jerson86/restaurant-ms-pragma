package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.application.dto.response.OrderResponse;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.OrderModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void createOrder(OrderRequest request, String bearerToken) {
        OrderModel orderModel = orderRequestMapper.toModel(request);

        orderServicePort.createOrder(orderModel, bearerToken);
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(Long restaurantId, String status, int page, int size, String bearerToken) {
        List<OrderModel> orders = orderServicePort.getOrdersByStatus(restaurantId, status, page, size, bearerToken);

        return orderRequestMapper.toResponseList(orders);
    }
}

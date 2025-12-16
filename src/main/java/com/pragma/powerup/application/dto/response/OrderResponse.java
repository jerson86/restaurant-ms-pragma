package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long clientId;
    private Long restaurantId;
    private String status;
    private List<OrderPlateResponse> plates;
}

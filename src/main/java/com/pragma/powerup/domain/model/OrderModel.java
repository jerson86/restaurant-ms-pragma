package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private Long restaurantId;
    private Long clientId;
    private List<OrderPlateModel> plates;
    private String status;
    Long employeeId;
    String SecurityPin;
}

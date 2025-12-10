package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlateModel {

    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean active;
    private Long restaurantId;
}

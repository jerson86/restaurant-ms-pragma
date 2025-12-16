package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlateClientResponse {
    private String name;
    private BigDecimal price;
    private String imageUrl;
}

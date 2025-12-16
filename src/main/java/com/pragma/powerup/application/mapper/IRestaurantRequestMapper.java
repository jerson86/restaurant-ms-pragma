package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.dto.request.RestaurantClientResponse;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {
    RestaurantModel toObject(CreateRestaurantRequest createRestaurantRequest);
    RestaurantClientResponse toClientResponse(RestaurantModel restaurantModel);
}

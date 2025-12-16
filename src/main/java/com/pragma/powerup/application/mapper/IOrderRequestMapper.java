package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {
    OrderModel toModel(OrderRequest orderRequest);
}

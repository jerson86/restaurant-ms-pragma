package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.application.dto.response.OrderResponse;
import com.pragma.powerup.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {
    OrderModel toModel(OrderRequest orderRequest);
    List<OrderResponse> toResponseList(List<OrderModel> orderListRequest);
}

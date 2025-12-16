package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.response.PlateClientResponse;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.model.PlateModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlateHandler implements IPlateHandler {

    private final IPlateServicePort plateServicePort;
    private final IPlateRequestMapper plateRequestMapper;

    @Override
    public void savePlate(CreatePlateRequest createPlateRequest, String bearerToken) {
        PlateModel plateModel = plateRequestMapper.toModel(createPlateRequest);
        plateServicePort.savePlate(plateModel, bearerToken);
    }

    @Override
    public void updatePlate(UpdatePlateRequest updatePlateRequest, String bearerToken) {
        PlateModel plateModel = plateRequestMapper.toModel(updatePlateRequest);
        plateServicePort.updatePlate(plateModel, bearerToken);
    }

    @Override
    public void enableDisablePlate(Long plateId, boolean enabled, String bearerToken) {
        plateServicePort.enableDisablePlate(plateId, enabled, bearerToken);
    }

    @Override
    public List<PlateClientResponse> getAllPlatesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String bearerToken) {
        List<PlateModel> plateList = plateServicePort.getAllPlatesByRestaurant(restaurantId, categoryId, page, size, bearerToken);

        return plateList.stream()
                .map(plateRequestMapper::toClientResponse)
                .toList();
    }
}

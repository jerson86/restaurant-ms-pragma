package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.model.PlateModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PlateHandler implements IPlateHandler {

    private final IPlateServicePort plateServicePort;
    private final IPlateRequestMapper plateRequestMapper;

    @Override
    public void savePlate(CreatePlateRequest createPlateRequest) {
        PlateModel plateModel = plateRequestMapper.toModel(createPlateRequest);
        plateServicePort.savePlate(plateModel);
    }

    @Override
    public void updatePlate(UpdatePlateRequest updatePlateRequest) {
        PlateModel plateModel = plateRequestMapper.toModel(updatePlateRequest);
        plateServicePort.updatePlate(plateModel);
    }
}

package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;

public interface IPlateHandler {
    void savePlate(CreatePlateRequest createPlateRequest);
    void updatePlate(UpdatePlateRequest request);
}

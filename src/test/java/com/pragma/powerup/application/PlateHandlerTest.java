package com.pragma.powerup.application;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.impl.PlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlateHandlerTest {

    @Mock
    private IPlateServicePort plateServicePort;

    @Mock
    private IPlateRequestMapper plateRequestMapper;

    @InjectMocks
    private PlateHandler plateHandler;

    private CreatePlateRequest mockRequest;
    private PlateModel mockModel;
    private UpdatePlateRequest mockUpdateRequest;
    private PlateModel mockUpdateModel;

    private final String TOKEN_BEARER_TEST = "Bearer: test";

    @BeforeEach
    void setUp() {
        mockRequest = new CreatePlateRequest(
                "Taco", 1L, "Descripción", new BigDecimal("5.00"), "url.jpg", 10L
        );
        mockModel = new PlateModel(
                null, "Taco", 1L, "Descripción", new BigDecimal("5.00"), "url.jpg", null, 10L
        );
        mockUpdateRequest = new UpdatePlateRequest(
                5L, new BigDecimal("25.50"), "Descripción Actualizada"
        );
        mockUpdateModel = new PlateModel(
                5L, null, null, "Descripción Actualizada", new BigDecimal("25.50"), null, null, null
        );
    }

    @Test
    void savePlate_CallsMapperAndUseCase() {
        // ARRANGE
        when(plateRequestMapper.toModel(mockRequest)).thenReturn(mockModel);
        doNothing().when(plateServicePort).savePlate(any(PlateModel.class), anyString());

        // ACT
        plateHandler.savePlate(mockRequest, TOKEN_BEARER_TEST);

        // ASSERT
        verify(plateRequestMapper, times(1)).toModel(mockRequest);
        verify(plateServicePort, times(1)).savePlate(mockModel, TOKEN_BEARER_TEST);
    }

    @Test
    void savePlate_ExceptionFromUseCase_PropagatesException() {
        // ARRANGE
        when(plateRequestMapper.toModel(mockRequest)).thenReturn(mockModel);

        doThrow(new DomainException(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE))
                .when(plateServicePort).savePlate(any(PlateModel.class), anyString());

        // ACT & ASSERT
        assertThrows(DomainException.class, () ->
                plateHandler.savePlate(mockRequest, TOKEN_BEARER_TEST)
        );

        verify(plateRequestMapper, times(1)).toModel(mockRequest);
    }

    @Test
    void updatePlate_CallsMapperAndUseCase() {
        // ARRANGE
        when(plateRequestMapper.toModel(mockUpdateRequest)).thenReturn(mockUpdateModel);
        doNothing().when(plateServicePort).updatePlate(any(PlateModel.class), anyString());

        // ACT
        plateHandler.updatePlate(mockUpdateRequest, TOKEN_BEARER_TEST);

        // ASSERT
        verify(plateRequestMapper, times(1)).toModel(mockUpdateRequest);

        verify(plateServicePort, times(1)).updatePlate(mockUpdateModel, TOKEN_BEARER_TEST);
    }

    @Test
    void updatePlate_ExceptionFromUseCase_PropagatesException() {
        // ARRANGE
        when(plateRequestMapper.toModel(mockUpdateRequest)).thenReturn(mockUpdateModel);

        doThrow(new DomainException(BusinessMessage.PLATE_NOT_FOUND))
                .when(plateServicePort).updatePlate(any(PlateModel.class), anyString());

        // ACT & ASSERT
        assertThrows(DomainException.class, () ->
                plateHandler.updatePlate(mockUpdateRequest, TOKEN_BEARER_TEST)
        );

        verify(plateRequestMapper, times(1)).toModel(mockUpdateRequest);
    }
}
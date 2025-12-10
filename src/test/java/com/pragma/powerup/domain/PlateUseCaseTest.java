package com.pragma.powerup.domain;

import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PlateModel;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.PlateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlateUseCaseTest {

    @Mock
    private IPlatePersistencePort platePersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private PlateUseCase plateUseCase;

    private final Long PLATE_ID = 5L;
    private final Long RESTAURANT_ID = 10L;

    private PlateModel validPlate;
    private PlateModel existingPlate;
    private PlateModel updateData;

    @BeforeEach
    void setUp() {
        validPlate = new PlateModel(
                null,
                "Pizza Margherita",
                1L,
                "Clásica pizza italiana",
                new BigDecimal("15.50"),
                "url.jpg",
                null,
                10L
        );
        existingPlate = new PlateModel(
                PLATE_ID, "Nombre Antiguo", 1L, "Descripción Antigua",
                new BigDecimal("20.00"), "url.jpg", true, RESTAURANT_ID
        );
        updateData = new PlateModel(
                PLATE_ID, null, null, "Nueva Descripción",
                new BigDecimal("25.50"), null, null, null
        );
    }

    @Test
    void savePlate_SuccessfulScenario_PersistsPlateAsActive() {
        // ARRANGE
        when(restaurantPersistencePort.existsRestaurantById(10L)).thenReturn(true);

        doNothing().when(platePersistencePort).savePlate(any(PlateModel.class));

        // ACT
        plateUseCase.savePlate(validPlate);

        // ASSERT
        verify(restaurantPersistencePort, times(1)).existsRestaurantById(10L);
        verify(platePersistencePort, times(1)).savePlate(any(PlateModel.class));
        verify(platePersistencePort).savePlate(argThat(plate -> plate.getActive().equals(true)));
    }

    @Test
    void savePlate_ThrowsException_WhenRestaurantNotExists() {
        // ARRANGE
        when(restaurantPersistencePort.existsRestaurantById(10L)).thenReturn(false);

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                plateUseCase.savePlate(validPlate)
        );

        assertEquals(BusinessMessage.RESTAURANT_ID_NOT_EXISTS.getMessage(), exception.getMessage());

        verify(platePersistencePort, never()).savePlate(any(PlateModel.class));
    }

    @Test
    void savePlate_ThrowsException_WhenPriceIsZero() {
        // ARRANGE
        validPlate.setPrice(BigDecimal.ZERO);

        when(restaurantPersistencePort.existsRestaurantById(10L)).thenReturn(true);

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                plateUseCase.savePlate(validPlate)
        );

        assertEquals(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE.getMessage(), exception.getMessage());

        verify(platePersistencePort, never()).savePlate(any(PlateModel.class));
    }

    @Test
    void updatePlate_SuccessfulScenario_UpdatesPriceAndDescriptionOnly() {
        // ARRANGE
        when(platePersistencePort.getPlateById(PLATE_ID)).thenReturn(existingPlate);

        // ACT
        plateUseCase.updatePlate(updateData);

        // ASSERT
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID);

        verify(platePersistencePort, times(1)).savePlate(argThat(updated ->
                updated.getPrice().compareTo(new BigDecimal("25.50")) == 0 &&
                        updated.getDescription().equals("Nueva Descripción") &&
                        updated.getName().equals("Nombre Antiguo") &&
                        updated.getRestaurantId().equals(RESTAURANT_ID) &&
                        updated.getActive().equals(true)
        ));
    }

    @Test
    void updatePlate_ThrowsException_WhenPlateNotFound() {
        // ARRANGE
        when(platePersistencePort.getPlateById(PLATE_ID)).thenReturn(null);

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                plateUseCase.updatePlate(updateData)
        );
        assertEquals(BusinessMessage.PLATE_NOT_FOUND.getMessage(), exception.getMessage());

        verify(platePersistencePort, never()).savePlate(any());
    }

    @Test
    void updatePlate_ThrowsException_WhenPriceIsNotPositive() {
        // ARRANGE
        when(platePersistencePort.getPlateById(PLATE_ID)).thenReturn(existingPlate);
        updateData.setPrice(new BigDecimal("-5.00"));

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                plateUseCase.updatePlate(updateData)
        );

        assertEquals(BusinessMessage.PLATE_PRICE_MUST_BE_POSITIVE.getMessage(), exception.getMessage());

        verify(platePersistencePort, never()).savePlate(any());
    }
}

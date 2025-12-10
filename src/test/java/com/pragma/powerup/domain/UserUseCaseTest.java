package com.pragma.powerup.domain;

import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.pragma.powerup.domain.util.CommonConstant.AGE_MIN_ADULT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class restaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort persistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel restaurant;

    @BeforeEach
    void setup() {
        restaurant = new RestaurantModel();
        restaurant.setId(1L);
        restaurant.setNombre("test");
        restaurant.setNit("1234");
    }

    @Test
    void saverestaurant_WhenEmailExists_ShouldThrowException() {

        // Arrange
        when(persistencePort.findByCorreo("test@mail.com")).thenReturn(new RestaurantModel());

        // Act + Assert
        DomainException ex = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant)
        );

        assertEquals(BusinessMessage.restaurant_EMAIL_ALREADY_EXISTS.getMessage(), ex.getMessage());

        verify(persistencePort, never()).saveRestaurant(any());
    }

    @Test
    void saverestaurant_WhenrestaurantIsNotAdult_ShouldThrowException() {

        // Arrange
        restaurant.setFechaNacimiento(LocalDate.now().minusYears(AGE_MIN_ADULT - 1)); // menor de edad
        when(persistencePort.findByCorreo(any())).thenReturn(null);

        // Act + Assert
        DomainException ex = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant)
        );

        assertEquals(BusinessMessage.restaurant_IS_NOT_ADULT.getMessage(), ex.getMessage());
        verify(persistencePort, never()).saveRestaurant(any());
    }

    @Test
    void saverestaurant_WhenDocumentExists_ShouldThrowException() {

        // Arrange
        when(persistencePort.findByCorreo(any())).thenReturn(null);
        when(persistencePort.existsByNit("123456789")).thenReturn(true);

        // Act + Assert
        DomainException ex = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant)
        );

        assertEquals(BusinessMessage.RESTAURANT_NIT_ALREADY_EXISTS.getMessage(), ex.getMessage());

        verify(persistencePort, never()).saveRestaurant(any());
    }

    @Test
    void saverestaurant_WhenValid_ShouldSaveSuccessfully() {

        // Arrange
        when(persistencePort.findByCorreo(any())).thenReturn(null);
        when(persistencePort.existsByNit(any())).thenReturn(false);

        // Act
        restaurantUseCase.saveRestaurant(restaurant);

        // Assert
        verify(persistencePort, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void saverestaurant_WhenExactly18YearsOld_ShouldBeAdultAndSave() {

        // Arrange
        restaurant.setFechaNacimiento(LocalDate.now().minusYears(AGE_MIN_ADULT));
        when(persistencePort.findByCorreo(any())).thenReturn(null);
        when(persistencePort.existsByNit(any())).thenReturn(false);

        // Act
        assertDoesNotThrow(() -> restaurantUseCase.saveRestaurant(restaurant));

        verify(persistencePort).saveRestaurant(restaurant);
    }
}

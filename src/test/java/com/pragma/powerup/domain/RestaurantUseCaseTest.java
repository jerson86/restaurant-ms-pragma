package com.pragma.powerup.domain;

import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserRestPort;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort userPersistencePort;

    @Mock
    private IUserRestPort userRestPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel validRestaurantModel;
    private UserModel mockUser;

    @BeforeEach
    void setUp() {
        validRestaurantModel = new RestaurantModel(
                1L,
                "NombreRest",
                "1111111111",
                "calle 11",
                "312333333",
                "logo.jpg",
                1L
        );

        mockUser = new UserModel(10L, "juan", "perez", "212121212", "322000000", null, "juan@test.com");
    }

    @Test
    void saveRestaurant_SuccessfulScenario() {
        // ARRANGE
        when(userRestPort.getUserById(validRestaurantModel.getUserId())).thenReturn(mockUser);
        when(userPersistencePort.existsByNit(validRestaurantModel.getNit())).thenReturn(false);

        // ACT
        restaurantUseCase.saveRestaurant(validRestaurantModel);

        // ASSERT
        verify(userRestPort, times(1)).getUserById(validRestaurantModel.getUserId());
        verify(userPersistencePort, times(1)).existsByNit(validRestaurantModel.getNit());
        verify(userPersistencePort, times(1)).saveRestaurant(validRestaurantModel);
    }

    @Test
    void saveRestaurant_ThrowsException_WhenUserNotExists() {
        // ARRANGE
        when(userRestPort.getUserById(validRestaurantModel.getUserId())).thenReturn(null);

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                restaurantUseCase.saveRestaurant(validRestaurantModel)
        );

        assertEquals(BusinessMessage.RESTAURANT_USER_ID_NOT_EXISTS.getMessage(), exception.getMessage());
        verify(userPersistencePort, never()).existsByNit(anyString());
        verify(userPersistencePort, never()).saveRestaurant(any(RestaurantModel.class));
    }

    @Test
    void saveRestaurant_ThrowsException_WhenNitAlreadyExists() {
        // ARRANGE
        when(userRestPort.getUserById(validRestaurantModel.getUserId())).thenReturn(mockUser);

        when(userPersistencePort.existsByNit(validRestaurantModel.getNit())).thenReturn(true);

        // ACT & ASSERT
        DomainException exception = assertThrows(DomainException.class, () ->
                restaurantUseCase.saveRestaurant(validRestaurantModel)
        );

        assertEquals(BusinessMessage.RESTAURANT_NIT_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(userPersistencePort, never()).saveRestaurant(any(RestaurantModel.class));
    }
}

package com.pragma.powerup.application;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort userServicePort;

    @Mock
    private IRestaurantRequestMapper objectRequestMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private CreateRestaurantRequest mockRequest;
    private RestaurantModel mockModel;

    private final String TOKEN_BEARER_TEST = "Bearer: test";

    @BeforeEach
    void setUp() {
        mockRequest = new CreateRestaurantRequest("NombreRest", "1111111111", "calle 11", "312333333", "logo.jpg", 1L);

        mockModel = new RestaurantModel(
                1L,
                "NombreRest",
                "1111111111",
                "calle 11",
                "312333333",
                "logo.jpg",
                1L
        );

        when(objectRequestMapper.toObject(mockRequest)).thenReturn(mockModel);
    }

    @Test
    void saveRestaurant_SuccessfulMappingAndPersistence() {
        // ARRANGE
        doNothing().when(userServicePort).saveRestaurant(any(RestaurantModel.class), anyString());

        // ACT
        restaurantHandler.saveRestaurant(mockRequest, TOKEN_BEARER_TEST);

        // ASSERT
        verify(objectRequestMapper, times(1)).toObject(mockRequest);
        verify(userServicePort, times(1)).saveRestaurant(mockModel, TOKEN_BEARER_TEST);

        verify(userServicePort).saveRestaurant(argThat(model ->
                model.getNombre().equals(mockModel.getNombre()) &&
                        model.getUserId().equals(mockModel.getUserId())
        ), anyString());
    }

    @Test
    void saveRestaurant_WhenServicePortThrowsException_ShouldThrow() {
        // ARRANGE
        doThrow(new RuntimeException("Error en el handler")).when(userServicePort).saveRestaurant(any(RestaurantModel.class), anyString());

        // ACT
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> restaurantHandler.saveRestaurant(mockRequest, TOKEN_BEARER_TEST));

        // ASSERT
        verify(objectRequestMapper, times(1)).toObject(mockRequest);
    }
}
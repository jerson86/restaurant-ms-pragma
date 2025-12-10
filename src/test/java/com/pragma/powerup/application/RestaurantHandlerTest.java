package com.pragma.powerup.application;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort userServicePort;

    @Mock
    private IRestaurantRequestMapper objectRequestMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    private CreateRestaurantRequest request;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setup() {
        request = new CreateRestaurantRequest();
        request.setNombre("Carlos");
        request.setApellido("Ramirez");
        request.setCorreo("owner@test.com");
        request.setClave("1234");

        restaurantModel = new RestaurantModel();
        restaurantModel.setCorreo("owner@test.com");
    }

    @Test
    void saveRestaurant_ShouldMapEncodeAndCallService() {

        // Arrange
        when(objectRequestMapper.toObject(request)).thenReturn(restaurantModel);
        when(passwordEncoder.encode("1234")).thenReturn("ENCODED_PASSWORD");

        // Act
        restaurantHandler.saveRestaurant(request);

        // Assert
        assertEquals("ENCODED_PASSWORD", restaurantModel.getClave());
        verify(objectRequestMapper, times(1)).toObject(request);
        verify(passwordEncoder, times(1)).encode("1234");

        // Capture argument to ensure correct object passed
        ArgumentCaptor<RestaurantModel> captor = ArgumentCaptor.forClass(RestaurantModel.class);
        verify(userServicePort, times(1)).saveRestaurant(captor.capture());

        assertEquals("ENCODED_PASSWORD", captor.getValue().getClave());
    }

    @Test
    void saveRestaurant_WhenServiceThrowsException_ShouldPropagate() {
        // Arrange
        when(objectRequestMapper.toObject(request)).thenReturn(restaurantModel);
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED");
        doThrow(new RuntimeException("DB error")).when(userServicePort).saveRestaurant(any());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> restaurantHandler.saveRestaurant(request)
        );

        assertEquals("DB error", ex.getMessage());
        verify(userServicePort).saveRestaurant(any());
    }

    @Test
    void saveRestaurant_ShouldFailIfMapperReturnsNull() {
        // Arrange
        when(objectRequestMapper.toObject(request)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> restaurantHandler.saveRestaurant(request));
    }
}

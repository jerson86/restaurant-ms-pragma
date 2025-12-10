package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.input.rest.RestaurantRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantRestController.class)
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    private CreateRestaurantRequest validRequest;
    private CreateRestaurantRequest invalidRequest;
    private final String BASE_URL = "/api/v1/restaurant";

    @BeforeEach
    void setUp() {
        validRequest = new CreateRestaurantRequest("NombreRest", "1111111111", "calle 11", "312333333", "logo.jpg", 1L);
        invalidRequest = new CreateRestaurantRequest("NombreRest", null, "calle 11", "312333333", "logo.jpg", null);
    }

    @Test
    void save_ValidRequest_ReturnsCreatedStatus() throws Exception {
        // ARRANGE
        doNothing().when(restaurantHandler).saveRestaurant(any(CreateRestaurantRequest.class));

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        verify(restaurantHandler, times(1)).saveRestaurant(argThat(req ->
                req.getNombre().equals(validRequest.getNombre()) &&
                        req.getOwnerId().equals(validRequest.getOwnerId())
        ));
    }

    @Test
    void save_InvalidRequest_ReturnsBadRequestStatus() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(restaurantHandler, never()).saveRestaurant(any(CreateRestaurantRequest.class));
    }

    @Test
    void save_UserNotExists_ReturnsNotFoundStatus() throws Exception {
        // ARRANGE
        doThrow(new DomainException(BusinessMessage.RESTAURANT_USER_ID_NOT_EXISTS))
                .when(restaurantHandler).saveRestaurant(any(CreateRestaurantRequest.class));

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());

        verify(restaurantHandler, times(1)).saveRestaurant(validRequest);
    }

    @Test
    void save_NitAlreadyExists_ReturnsConflictStatus() throws Exception {
        // ARRANGE
        doThrow(new DomainException(BusinessMessage.RESTAURANT_NIT_ALREADY_EXISTS))
                .when(restaurantHandler).saveRestaurant(any(CreateRestaurantRequest.class));

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict());

        verify(restaurantHandler, times(1)).saveRestaurant(validRequest);
    }
}
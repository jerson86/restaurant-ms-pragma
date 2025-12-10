package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.domain.enums.BusinessMessage;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.input.rest.PlateRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlateRestController.class)
class PlateRestControllerTest {

    private final String BASE_URL = "/api/v1/plate";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IPlateHandler plateHandler;

    private CreatePlateRequest validRequest;
    private CreatePlateRequest invalidPriceRequest;
    private final Long PLATE_ID_TEST = 5L;
    private UpdatePlateRequest validUpdateRequest;
    private UpdatePlateRequest invalidPriceUpdateRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreatePlateRequest(
                "Taco al pastor", 1L, "Plato mexicano", new BigDecimal("10.00"), "taco.png", 1L
        );
        invalidPriceRequest = new CreatePlateRequest(
                "Taco al pastor", 1L, "Plato mexicano", new BigDecimal("0.00"), "taco.png", 1L
        );
        validUpdateRequest = new UpdatePlateRequest(
                PLATE_ID_TEST, new BigDecimal("35.50"), "Descripción actualizada y validada"
        );
        invalidPriceUpdateRequest = new UpdatePlateRequest(
                PLATE_ID_TEST, new BigDecimal("0.00"), "Descripción"
        );
    }

    @Test
    //@WithMockUser(roles = "OWNER")
    void savePlate_ValidRequestAndOwnerRole_ReturnsCreatedStatus() throws Exception {
        // ARRANGE
        doNothing().when(plateHandler).savePlate(any(CreatePlateRequest.class));

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        verify(plateHandler, times(1)).savePlate(any(CreatePlateRequest.class));
    }

    @Test
    //@WithMockUser(roles = "OWNER")
    void savePlate_InvalidPriceValidation_ReturnsBadRequestStatus() throws Exception {

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPriceRequest)))
                .andExpect(status().isBadRequest());

        verify(plateHandler, never()).savePlate(any(CreatePlateRequest.class));
    }

    @Test
    //@WithMockUser(roles = "OWNER")
    void savePlate_RestaurantNotExists_ReturnsNotFoundStatus() throws Exception {
        // ARRANGE
        doThrow(new DomainException(BusinessMessage.RESTAURANT_ID_NOT_EXISTS))
                .when(plateHandler).savePlate(any(CreatePlateRequest.class));

        // ACT & ASSERT
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());

        verify(plateHandler, times(1)).savePlate(any(CreatePlateRequest.class));
    }

    @Test
    //@WithMockUser(roles = "OWNER")
    void updatePlate_ValidRequestAndOwnerRole_ReturnsOkStatus() throws Exception {
        // ARRANGE
        doNothing().when(plateHandler).updatePlate(any(UpdatePlateRequest.class));

        // ACT & ASSERT
        mockMvc.perform(put(BASE_URL + "/{id}", PLATE_ID_TEST) // Usar PUT y el ID en el path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andExpect(status().isOk());

        verify(plateHandler, times(1)).updatePlate(any(UpdatePlateRequest.class));
    }



    @Test
    //@WithMockUser(roles = "OWNER")
    void updatePlate_InvalidPriceValidation_ReturnsBadRequestStatus() throws Exception {
        // ARRANGE

        // ACT & ASSERT
        mockMvc.perform(put(BASE_URL + "/{id}", PLATE_ID_TEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPriceUpdateRequest)))
                .andExpect(status().isBadRequest());

        verify(plateHandler, never()).updatePlate(any(UpdatePlateRequest.class));
    }

    @Test
    //@WithMockUser(roles = "OWNER")
    void updatePlate_PlateNotFoundException_ReturnsNotFoundStatus() throws Exception {
        // ARRANGE
        doThrow(new DomainException(BusinessMessage.PLATE_NOT_FOUND))
                .when(plateHandler).updatePlate(any(UpdatePlateRequest.class));

        // ACT & ASSERT
        mockMvc.perform(put(BASE_URL + "/{id}", PLATE_ID_TEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(plateHandler, times(1)).updatePlate(any(UpdatePlateRequest.class));
    }

    @Test
    void updatePlate_Exception_Not_Controlled() throws Exception {
        // ARRANGE
        doThrow(new RuntimeException("Excepcion no controlada"))
                .when(plateHandler).updatePlate(any(UpdatePlateRequest.class));

        // ACT & ASSERT
        mockMvc.perform(put(BASE_URL + "/{id}", PLATE_ID_TEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andExpect(status().is5xxServerError());

        verify(plateHandler, times(1)).updatePlate(any(UpdatePlateRequest.class));
    }
}

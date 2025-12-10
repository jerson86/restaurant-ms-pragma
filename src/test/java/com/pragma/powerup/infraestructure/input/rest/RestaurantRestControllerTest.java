package com.pragma.powerup.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.infraestructure.configuration.TestSecurityConfig;
import com.pragma.powerup.infrastructure.input.rest.RestaurantRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantRestController.class)
@Import(TestSecurityConfig.class)
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler userHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateRestaurantRequest buildValidRequest() {
        CreateRestaurantRequest req = new CreateRestaurantRequest();
        req.setNombre("Juan");
        req.setDireccion("Perez");
        req.setNit("123456789");
        req.setTelefono("+573001112233");
        req.setOwnerId(1L);
        req.setUrlLogo("url");
        return req;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_WhenAdmin_ShouldReturn201() throws Exception {

        CreateRestaurantRequest req = buildValidRequest();

        mockMvc.perform(post("/api/v1/admin/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(userHandler, times(1)).saveRestaurant(any(CreateRestaurantRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void save_WhenNotAdmin_ShouldReturn403() throws Exception {

        mockMvc.perform(post("/api/v1/admin/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidRequest())))
                .andExpect(status().isForbidden());

        verify(userHandler, times(0)).saveRestaurant(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_WhenInvalidRequest_ShouldReturn400() throws Exception {

        CreateRestaurantRequest invalidReq = new CreateRestaurantRequest();
        invalidReq.setNombre("");

        mockMvc.perform(post("/api/v1/admin/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest());

        verify(userHandler, times(0)).saveRestaurant(any());
    }
}
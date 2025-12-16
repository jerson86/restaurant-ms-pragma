package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.dto.request.RestaurantClientResponse;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Agregar un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "restaurant created", content = @Content),
            @ApiResponse(responseCode = "409", description = "restaurant already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CreateRestaurantRequest createRestaurantRequest,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        restaurantHandler.saveRestaurant(createRestaurantRequest, bearerToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los restaurantes paginados y ordenados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes devuelta"),
            @ApiResponse(responseCode = "204", description = "No hay restaurantes disponibles")
    })
    @GetMapping
    public ResponseEntity<List<RestaurantClientResponse>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        List<RestaurantClientResponse> responseList = restaurantHandler.getAllRestaurants(page, size, bearerToken);

        if (responseList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(responseList);
    }
}
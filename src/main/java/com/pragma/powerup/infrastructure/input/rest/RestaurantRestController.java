package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CreateRestaurantRequest;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Add a new object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user created", content = @Content),
            @ApiResponse(responseCode = "409", description = "user already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CreateRestaurantRequest createRestaurantRequest) {
        restaurantHandler.saveRestaurant(createRestaurantRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
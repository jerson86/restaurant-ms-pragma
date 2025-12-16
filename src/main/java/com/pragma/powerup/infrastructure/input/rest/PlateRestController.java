package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.response.PlateClientResponse;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.IPlateHandler;
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
@RequestMapping("/api/v1/plate")
@RequiredArgsConstructor
public class PlateRestController {

    private final IPlateHandler plateHandler;

    @Operation(summary = "Crear un nuevo plato asociado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error de validación de entrada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante asociado no encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (solo Propietarios)", content = @Content)
    })

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CreatePlateRequest createPlateRequest,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        plateHandler.savePlate(createPlateRequest, bearerToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar precio y descripción de un plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error de validación de entrada o precio no positivo", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (no es el propietario del restaurante)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado", content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlate(@PathVariable Long id, @Valid @RequestBody UpdatePlateRequest updatePlateRequest,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        updatePlateRequest.setId(id);
        plateHandler.updatePlate(updatePlateRequest, bearerToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Habilitar/Deshabilitar plato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado/No es el propietario"),
            @ApiResponse(responseCode = "404", description = "Plato o restaurante no encontrado")
    })
    @PutMapping("/status/{plateId}")
    public ResponseEntity<Void> enableDisableDish(
            @PathVariable Long plateId,
            @RequestParam boolean enabled,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        plateHandler.enableDisablePlate(plateId, enabled, bearerToken);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar todos los platos paginados y ordenados por restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de platos"),
            @ApiResponse(responseCode = "204", description = "No hay platos disponibles")
    })
    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<PlateClientResponse>> getAllPlates(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {

        List<PlateClientResponse> responseList = plateHandler.getAllPlatesByRestaurant(restaurantId, categoryId,page, size, bearerToken);

        if (responseList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(responseList);
    }
}
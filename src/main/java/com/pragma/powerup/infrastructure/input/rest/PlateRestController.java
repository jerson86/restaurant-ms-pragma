package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CreatePlateRequest;
import com.pragma.powerup.application.dto.request.UpdatePlateRequest;
import com.pragma.powerup.application.handler.IPlateHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //@PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CreatePlateRequest createPlateRequest) {
        plateHandler.savePlate(createPlateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar precio y descripción de un plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error de validación de entrada o precio no positivo", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (no es el propietario del restaurante)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado", content = @Content)
    })
    //@PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlate(@PathVariable Long id, @Valid @RequestBody UpdatePlateRequest updatePlateRequest) {
        updatePlateRequest.setId(id);
        plateHandler.updatePlate(updatePlateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
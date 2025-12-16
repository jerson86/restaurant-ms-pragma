package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderAssignmentRequest;
import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.application.dto.response.OrderResponse;
import com.pragma.powerup.application.handler.IOrderHandler;
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
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @Operation(summary = "Crear una nueva orden en un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Error de validación de entrada", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (solo Propietarios)", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<Void> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        orderHandler.createOrder(request, token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar pedidos por estado (Solo Empleados)")
    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @RequestParam String status,
            @RequestParam Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<OrderResponse> response = orderHandler.getOrdersByStatus(restaurantId, status, page, size, token);

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Asignarse a pedidos y cambiar estado a EN_PREPARACION")
    @PatchMapping("/employee/assign")
    public ResponseEntity<Void> assignOrders(
            @RequestBody OrderAssignmentRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        orderHandler.assignOrders(request, token);
        return ResponseEntity.ok().build();
    }
}

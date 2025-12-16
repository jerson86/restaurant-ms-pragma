package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequest;
import com.pragma.powerup.application.handler.IOrderHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @PostMapping()
    public ResponseEntity<Void> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        orderHandler.createOrder(request, token);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

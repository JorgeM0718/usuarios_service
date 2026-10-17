package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.LoginRequest;
import com.pragma.usuarios.application.dto.response.LoginResponse;
import com.pragma.usuarios.application.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación")
public class AuthRestController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve un JWT para usar en Authorize.")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authHandler.login(request));
    }
}

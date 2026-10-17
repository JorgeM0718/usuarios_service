package com.pragma.usuarios.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "admin@plazoleta.com") @NotBlank(message = "El correo es obligatorio") String correo,
        @Schema(example = "Admin123*") @NotBlank(message = "La clave es obligatoria") String clave
) {
}

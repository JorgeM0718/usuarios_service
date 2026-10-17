package com.pragma.usuarios.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record EmpleadoRequest(
        @Schema(example = "María") @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Schema(example = "Gómez") @NotBlank(message = "El apellido es obligatorio") String apellido,
        @Schema(example = "1098765432") @NotBlank(message = "El documento de identidad es obligatorio") String documentoIdentidad,
        @Schema(example = "+573115556677") @NotBlank(message = "El celular es obligatorio") String celular,
        @Schema(example = "1995-08-20", description = "Opcional. Formato yyyy-MM-dd") LocalDate fechaNacimiento,
        @Schema(example = "maria.gomez@mail.com") @NotBlank(message = "El correo es obligatorio") String correo,
        @Schema(example = "MiClaveSegura123") @NotBlank(message = "La clave es obligatoria") String clave
) {
}

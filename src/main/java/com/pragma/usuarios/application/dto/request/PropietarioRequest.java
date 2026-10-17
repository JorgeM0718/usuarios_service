package com.pragma.usuarios.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PropietarioRequest(
        @Schema(example = "Juan") @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Schema(example = "Pérez") @NotBlank(message = "El apellido es obligatorio") String apellido,
        @Schema(example = "1012345678") @NotBlank(message = "El documento de identidad es obligatorio") String documentoIdentidad,
        @Schema(example = "+573005698325") @NotBlank(message = "El celular es obligatorio") String celular,
        @Schema(example = "1990-05-10", description = "Formato yyyy-MM-dd. Debe ser mayor de edad")
        @NotNull(message = "La fecha de nacimiento es obligatoria") LocalDate fechaNacimiento,
        @Schema(example = "juan.perez@mail.com") @NotBlank(message = "El correo es obligatorio") String correo,
        @Schema(example = "MiClaveSegura123") @NotBlank(message = "La clave es obligatoria") String clave
) {
}

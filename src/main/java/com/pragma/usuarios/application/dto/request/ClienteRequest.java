package com.pragma.usuarios.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ClienteRequest(
        @Schema(example = "Carlos") @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Schema(example = "Ramírez") @NotBlank(message = "El apellido es obligatorio") String apellido,
        @Schema(example = "1023456789") @NotBlank(message = "El documento de identidad es obligatorio") String documentoIdentidad,
        @Schema(example = "+573201112233") @NotBlank(message = "El celular es obligatorio") String celular,
        @Schema(example = "2000-03-15", description = "Opcional. Formato yyyy-MM-dd") LocalDate fechaNacimiento,
        @Schema(example = "carlos.ramirez@mail.com") @NotBlank(message = "El correo es obligatorio") String correo,
        @Schema(example = "MiClaveSegura123") @NotBlank(message = "La clave es obligatoria") String clave
) {
}

package com.pragma.usuarios.application.dto.response;

import com.pragma.usuarios.domain.model.Rol;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String documentoIdentidad,
        String celular,
        LocalDate fechaNacimiento,
        Integer edad,
        String correo,
        Rol rol
) {
}

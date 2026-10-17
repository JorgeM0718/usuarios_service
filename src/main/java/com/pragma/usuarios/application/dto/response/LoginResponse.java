package com.pragma.usuarios.application.dto.response;

import com.pragma.usuarios.domain.model.Rol;

public record LoginResponse(
        String token,
        String tipoToken,
        long expiraEnSegundos,
        Long idUsuario,
        Rol rol
) {
}

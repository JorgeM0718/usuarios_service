package com.pragma.usuarios.infrastructure.security;

import com.pragma.usuarios.domain.model.Rol;

public record AuthenticatedUser(Long id, String correo, Rol rol) {
}

package com.pragma.usuarios.domain.model;

public record AuthToken(String token, long expiraEnSegundos, Long idUsuario, Rol rol) {
}

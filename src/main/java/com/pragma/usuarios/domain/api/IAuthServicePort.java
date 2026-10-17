package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.AuthToken;

public interface IAuthServicePort {

    /** HU5: inicio de sesión con correo y clave. */
    AuthToken iniciarSesion(String correo, String clave);
}

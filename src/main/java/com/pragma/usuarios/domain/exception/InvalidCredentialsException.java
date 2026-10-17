package com.pragma.usuarios.domain.exception;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Correo o clave incorrectos");
    }
}

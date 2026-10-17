package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.LoginRequest;
import com.pragma.usuarios.application.dto.response.LoginResponse;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.model.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHandler {

    private final IAuthServicePort authServicePort;

    public LoginResponse login(LoginRequest request) {
        AuthToken token = authServicePort.iniciarSesion(request.correo(), request.clave());
        return new LoginResponse(token.token(), "Bearer", token.expiraEnSegundos(), token.idUsuario(), token.rol());
    }
}

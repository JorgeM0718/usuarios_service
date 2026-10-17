package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.exception.InvalidCredentialsException;
import com.pragma.usuarios.domain.exception.InvalidDataException;
import com.pragma.usuarios.domain.model.AuthToken;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenProviderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;

public class AuthUseCase implements IAuthServicePort {

    private final IUsuarioPersistencePort persistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenProviderPort tokenProviderPort;

    public AuthUseCase(IUsuarioPersistencePort persistencePort,
                       IPasswordEncoderPort passwordEncoderPort,
                       ITokenProviderPort tokenProviderPort) {
        this.persistencePort = persistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public AuthToken iniciarSesion(String correo, String clave) {
        if (correo == null || correo.isBlank() || clave == null || clave.isBlank()) {
            throw new InvalidDataException("El correo y la clave son obligatorios");
        }

        Usuario usuario = persistencePort.buscarPorCorreo(correo.trim().toLowerCase())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoderPort.coincide(clave, usuario.getClave())) {
            throw new InvalidCredentialsException();
        }
        return tokenProviderPort.generar(usuario);
    }
}

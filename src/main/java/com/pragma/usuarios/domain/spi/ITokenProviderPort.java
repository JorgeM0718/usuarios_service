package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.AuthToken;
import com.pragma.usuarios.domain.model.Usuario;

public interface ITokenProviderPort {

    AuthToken generar(Usuario usuario);
}

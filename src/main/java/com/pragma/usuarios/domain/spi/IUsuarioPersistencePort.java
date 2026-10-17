package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.Usuario;

import java.util.Optional;

public interface IUsuarioPersistencePort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorCorreo(String correo);

    boolean existePorCorreo(String correo);

    boolean existePorDocumento(String documentoIdentidad);
}

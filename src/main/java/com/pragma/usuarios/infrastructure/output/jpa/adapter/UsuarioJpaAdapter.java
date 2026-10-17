package com.pragma.usuarios.infrastructure.output.jpa.adapter;

import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUsuarioEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUsuarioRepository;

import java.util.Optional;

public class UsuarioJpaAdapter implements IUsuarioPersistencePort {

    private final IUsuarioRepository repository;
    private final IUsuarioEntityMapper mapper;

    public UsuarioJpaAdapter(IUsuarioRepository repository, IUsuarioEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return mapper.toModel(repository.save(mapper.toEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo).map(mapper::toModel);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return repository.existsByCorreo(correo);
    }

    @Override
    public boolean existePorDocumento(String documentoIdentidad) {
        return repository.existsByDocumentoIdentidad(documentoIdentidad);
    }
}

package com.pragma.usuarios.infrastructure.output.jpa.mapper;

import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUsuarioEntityMapper {

    UsuarioEntity toEntity(Usuario usuario);

    Usuario toModel(UsuarioEntity entity);
}

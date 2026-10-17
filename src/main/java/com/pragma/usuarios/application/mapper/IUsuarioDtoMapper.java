package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.request.ClienteRequest;
import com.pragma.usuarios.application.dto.request.EmpleadoRequest;
import com.pragma.usuarios.application.dto.request.PropietarioRequest;
import com.pragma.usuarios.application.dto.response.UsuarioResponse;
import com.pragma.usuarios.application.dto.response.UsuarioRolResponse;
import com.pragma.usuarios.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUsuarioDtoMapper {

    Usuario toModel(PropietarioRequest request);

    Usuario toModel(EmpleadoRequest request);

    Usuario toModel(ClienteRequest request);

    @Mapping(target = "edad", expression = "java(calcularEdad(usuario.getFechaNacimiento()))")
    UsuarioResponse toResponse(Usuario usuario);

    UsuarioRolResponse toRolResponse(Usuario usuario);

    /** Null si no se registró fecha de nacimiento (hoy es opcional para empleado y cliente). */
    default Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}

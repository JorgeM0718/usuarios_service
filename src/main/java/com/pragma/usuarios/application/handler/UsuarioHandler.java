package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.ClienteRequest;
import com.pragma.usuarios.application.dto.request.EmpleadoRequest;
import com.pragma.usuarios.application.dto.request.PropietarioRequest;
import com.pragma.usuarios.application.dto.response.UsuarioResponse;
import com.pragma.usuarios.application.dto.response.UsuarioRolResponse;
import com.pragma.usuarios.application.mapper.IUsuarioDtoMapper;
import com.pragma.usuarios.domain.api.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioHandler {

    private final IUsuarioServicePort usuarioServicePort;
    private final IUsuarioDtoMapper mapper;

    public UsuarioResponse crearPropietario(PropietarioRequest request) {
        return mapper.toResponse(usuarioServicePort.crearPropietario(mapper.toModel(request)));
    }

    public UsuarioResponse crearEmpleado(EmpleadoRequest request) {
        return mapper.toResponse(usuarioServicePort.crearEmpleado(mapper.toModel(request)));
    }

    public UsuarioResponse crearCliente(ClienteRequest request) {
        return mapper.toResponse(usuarioServicePort.crearCliente(mapper.toModel(request)));
    }

    public UsuarioRolResponse obtenerRol(Long idUsuario) {
        return mapper.toRolResponse(usuarioServicePort.obtenerPorId(idUsuario));
    }
}

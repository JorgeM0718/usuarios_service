package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.Usuario;

public interface IUsuarioServicePort {

    /** HU1: crea un usuario con rol PROPIETARIO (debe ser mayor de edad). */
    Usuario crearPropietario(Usuario usuario);

    /** HU6: crea un usuario con rol EMPLEADO. */
    Usuario crearEmpleado(Usuario usuario);

    /** HU8: crea un usuario con rol CLIENTE. */
    Usuario crearCliente(Usuario usuario);

    Usuario obtenerPorId(Long idUsuario);
}

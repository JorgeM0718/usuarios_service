package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IUsuarioServicePort;
import com.pragma.usuarios.domain.exception.DuplicateResourceException;
import com.pragma.usuarios.domain.exception.InvalidDataException;
import com.pragma.usuarios.domain.exception.ResourceNotFoundException;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UsuarioUseCase implements IUsuarioServicePort {

    private static final int EDAD_MINIMA = 18;
    private static final int CELULAR_LONGITUD_MAXIMA = 13;
    private static final Pattern SOLO_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern CELULAR = Pattern.compile("^\\+?\\d+$");
    private static final Pattern CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

    private final IUsuarioPersistencePort persistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final Clock clock;

    public UsuarioUseCase(IUsuarioPersistencePort persistencePort,
                          IPasswordEncoderPort passwordEncoderPort,
                          Clock clock) {
        this.persistencePort = persistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.clock = clock;
    }

    @Override
    public Usuario crearPropietario(Usuario usuario) {
        validarDatosBasicos(usuario);
        validarMayorDeEdad(usuario.getFechaNacimiento());
        return registrar(usuario, Rol.PROPIETARIO);
    }

    @Override
    public Usuario crearEmpleado(Usuario usuario) {
        validarDatosBasicos(usuario);
        return registrar(usuario, Rol.EMPLEADO);
    }

    @Override
    public Usuario crearCliente(Usuario usuario) {
        validarDatosBasicos(usuario);
        return registrar(usuario, Rol.CLIENTE);
    }

    @Override
    public Usuario obtenerPorId(Long idUsuario) {
        return persistencePort.buscarPorId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con id " + idUsuario + " no existe"));
    }

    private Usuario registrar(Usuario usuario, Rol rol) {
        String correo = usuario.getCorreo().trim().toLowerCase();
        String documento = usuario.getDocumentoIdentidad().trim();

        if (persistencePort.existePorCorreo(correo)) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con el correo " + correo);
        }
        if (persistencePort.existePorDocumento(documento)) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con el documento " + documento);
        }

        usuario.setId(null);
        usuario.setNombre(usuario.getNombre().trim());
        usuario.setApellido(usuario.getApellido().trim());
        usuario.setDocumentoIdentidad(documento);
        usuario.setCelular(usuario.getCelular().trim());
        usuario.setCorreo(correo);
        usuario.setClave(passwordEncoderPort.encriptar(usuario.getClave()));
        usuario.setRol(rol);
        return persistencePort.guardar(usuario);
    }

    private void validarDatosBasicos(Usuario usuario) {
        requerido(usuario.getNombre(), "nombre");
        requerido(usuario.getApellido(), "apellido");
        requerido(usuario.getDocumentoIdentidad(), "documento de identidad");
        requerido(usuario.getCelular(), "celular");
        requerido(usuario.getCorreo(), "correo");
        requerido(usuario.getClave(), "clave");

        if (!SOLO_NUMEROS.matcher(usuario.getDocumentoIdentidad().trim()).matches()) {
            throw new InvalidDataException("El documento de identidad debe ser únicamente numérico");
        }

        String celular = usuario.getCelular().trim();
        if (celular.length() > CELULAR_LONGITUD_MAXIMA || !CELULAR.matcher(celular).matches()) {
            throw new InvalidDataException(
                    "El celular debe tener máximo 13 caracteres y solo puede contener números y el símbolo + al inicio");
        }

        if (!CORREO.matcher(usuario.getCorreo().trim()).matches()) {
            throw new InvalidDataException("El correo no tiene una estructura válida");
        }
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new InvalidDataException("La fecha de nacimiento es obligatoria");
        }
        int edad = Period.between(fechaNacimiento, LocalDate.now(clock)).getYears();
        if (edad < EDAD_MINIMA) {
            throw new InvalidDataException("El usuario debe ser mayor de edad");
        }
    }

    private void requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new InvalidDataException("El campo " + campo + " es obligatorio");
        }
    }
}

package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.exception.DuplicateResourceException;
import com.pragma.usuarios.domain.exception.InvalidDataException;
import com.pragma.usuarios.domain.exception.ResourceNotFoundException;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-09-23T12:00:00Z"), ZoneOffset.UTC);

    @Mock
    private IUsuarioPersistencePort persistencePort;
    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private UsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UsuarioUseCase(persistencePort, passwordEncoderPort, CLOCK);
    }

    private Usuario usuarioValido() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .documentoIdentidad("123456789")
                .celular("+573005698325")
                .fechaNacimiento(LocalDate.of(1990, 5, 10))
                .correo("Juan@Mail.com")
                .clave("secreta123")
                .build();
    }

    private void stubRegistroExitoso() {
        when(persistencePort.existePorCorreo("juan@mail.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(false);
        when(passwordEncoderPort.encriptar("secreta123")).thenReturn("$2a$hash");
        when(persistencePort.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // ---------- HU1: crear propietario ----------

    @Test
    void crearPropietario_conDatosValidos_guardaConRolPropietarioClaveEncriptadaYCorreoNormalizado() {
        stubRegistroExitoso();

        Usuario resultado = useCase.crearPropietario(usuarioValido());

        assertThat(resultado.getRol()).isEqualTo(Rol.PROPIETARIO);
        assertThat(resultado.getClave()).isEqualTo("$2a$hash");
        assertThat(resultado.getCorreo()).isEqualTo("juan@mail.com");
    }

    @Test
    void crearPropietario_conExactamente18Anios_esValido() {
        stubRegistroExitoso();
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(LocalDate.of(2008, 9, 23));

        Usuario resultado = useCase.crearPropietario(usuario);

        assertThat(resultado.getRol()).isEqualTo(Rol.PROPIETARIO);
    }

    @Test
    void crearPropietario_aUnDiaDeCumplir18_lanzaInvalidData() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(LocalDate.of(2008, 9, 24));

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("mayor de edad");
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearPropietario_sinFechaDeNacimiento_lanzaInvalidData() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("fecha de nacimiento");
    }

    @ParameterizedTest
    @ValueSource(strings = {"juan", "juan@", "@mail.com", "juan@mail", "juan mail@mail.com", "juan@@mail.com"})
    void crearPropietario_conCorreoInvalido_lanzaInvalidData(String correo) {
        Usuario usuario = usuarioValido();
        usuario.setCorreo(correo);

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("correo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"3005698325ab", "+57300569832512", "57+3005698325", "++573005698", "300 569 8325"})
    void crearPropietario_conCelularInvalido_lanzaInvalidData(String celular) {
        Usuario usuario = usuarioValido();
        usuario.setCelular(celular);

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("celular");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345abc", "123.456", "-123456", "ABC"})
    void crearPropietario_conDocumentoNoNumerico_lanzaInvalidData(String documento) {
        Usuario usuario = usuarioValido();
        usuario.setDocumentoIdentidad(documento);

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("documento de identidad");
    }

    @Test
    void crearPropietario_conCamposObligatoriosVacios_lanzaInvalidData() {
        Usuario usuario = usuarioValido();
        usuario.setNombre("  ");

        assertThatThrownBy(() -> useCase.crearPropietario(usuario))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crearPropietario_conCorreoYaRegistrado_lanzaDuplicate() {
        when(persistencePort.existePorCorreo("juan@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.crearPropietario(usuarioValido()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("correo");
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearPropietario_conDocumentoYaRegistrado_lanzaDuplicate() {
        when(persistencePort.existePorCorreo("juan@mail.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(true);

        assertThatThrownBy(() -> useCase.crearPropietario(usuarioValido()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("documento");
        verify(persistencePort, never()).guardar(any());
    }

    // ---------- HU6: crear empleado ----------

    @Test
    void crearEmpleado_conDatosValidos_guardaConRolEmpleadoSinExigirFechaDeNacimiento() {
        stubRegistroExitoso();
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);

        Usuario resultado = useCase.crearEmpleado(usuario);

        assertThat(resultado.getRol()).isEqualTo(Rol.EMPLEADO);
        assertThat(resultado.getClave()).isEqualTo("$2a$hash");
    }

    @Test
    void crearEmpleado_ignoraUnRolEnviadoPorElLlamador() {
        stubRegistroExitoso();
        Usuario usuario = usuarioValido();
        usuario.setRol(Rol.ADMINISTRADOR);

        Usuario resultado = useCase.crearEmpleado(usuario);

        assertThat(resultado.getRol()).isEqualTo(Rol.EMPLEADO);
    }

    @Test
    void crearEmpleado_conCelularInvalido_lanzaInvalidData() {
        Usuario usuario = usuarioValido();
        usuario.setCelular("abc");

        assertThatThrownBy(() -> useCase.crearEmpleado(usuario))
                .isInstanceOf(InvalidDataException.class);
        verify(persistencePort, never()).guardar(any());
    }

    // ---------- HU8: crear cliente ----------

    @Test
    void crearCliente_conDatosValidos_guardaConRolCliente() {
        stubRegistroExitoso();
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);

        Usuario resultado = useCase.crearCliente(usuario);

        assertThat(resultado.getRol()).isEqualTo(Rol.CLIENTE);
        assertThat(resultado.getClave()).isEqualTo("$2a$hash");
    }

    @Test
    void crearCliente_conCorreoInvalido_lanzaInvalidData() {
        Usuario usuario = usuarioValido();
        usuario.setCorreo("no-es-correo");

        assertThatThrownBy(() -> useCase.crearCliente(usuario))
                .isInstanceOf(InvalidDataException.class);
    }

    // ---------- consulta por id ----------

    @Test
    void obtenerPorId_existente_devuelveElUsuario() {
        Usuario usuario = Usuario.builder().id(5L).rol(Rol.PROPIETARIO).build();
        when(persistencePort.buscarPorId(5L)).thenReturn(Optional.of(usuario));

        assertThat(useCase.obtenerPorId(5L).getRol()).isEqualTo(Rol.PROPIETARIO);
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(persistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

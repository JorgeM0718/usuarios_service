package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.exception.InvalidCredentialsException;
import com.pragma.usuarios.domain.exception.InvalidDataException;
import com.pragma.usuarios.domain.model.AuthToken;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenProviderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUsuarioPersistencePort persistencePort;
    @Mock
    private IPasswordEncoderPort passwordEncoderPort;
    @Mock
    private ITokenProviderPort tokenProviderPort;

    private AuthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AuthUseCase(persistencePort, passwordEncoderPort, tokenProviderPort);
    }

    private Usuario usuario() {
        return Usuario.builder().id(1L).correo("admin@plazoleta.com").clave("$2a$hash").rol(Rol.ADMINISTRADOR).build();
    }

    @Test
    void iniciarSesion_conCredencialesValidas_devuelveToken() {
        Usuario usuario = usuario();
        AuthToken esperado = new AuthToken("jwt", 7200, 1L, Rol.ADMINISTRADOR);
        when(persistencePort.buscarPorCorreo("admin@plazoleta.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coincide("Admin123*", "$2a$hash")).thenReturn(true);
        when(tokenProviderPort.generar(usuario)).thenReturn(esperado);

        AuthToken resultado = useCase.iniciarSesion("  Admin@Plazoleta.com ", "Admin123*");

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void iniciarSesion_conCorreoInexistente_lanzaInvalidCredentials() {
        when(persistencePort.buscarPorCorreo("nadie@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.iniciarSesion("nadie@mail.com", "x"))
                .isInstanceOf(InvalidCredentialsException.class);
        verify(tokenProviderPort, never()).generar(any());
    }

    @Test
    void iniciarSesion_conClaveIncorrecta_lanzaInvalidCredentials() {
        when(persistencePort.buscarPorCorreo("admin@plazoleta.com")).thenReturn(Optional.of(usuario()));
        when(passwordEncoderPort.coincide("mala", "$2a$hash")).thenReturn(false);

        assertThatThrownBy(() -> useCase.iniciarSesion("admin@plazoleta.com", "mala"))
                .isInstanceOf(InvalidCredentialsException.class);
        verify(tokenProviderPort, never()).generar(any());
    }

    @Test
    void iniciarSesion_conCamposVacios_lanzaInvalidData() {
        assertThatThrownBy(() -> useCase.iniciarSesion(" ", "clave"))
                .isInstanceOf(InvalidDataException.class);
        assertThatThrownBy(() -> useCase.iniciarSesion("a@b.com", null))
                .isInstanceOf(InvalidDataException.class);
    }
}

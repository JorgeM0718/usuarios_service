package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUsuarioServicePort;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenProviderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;
import com.pragma.usuarios.domain.usecase.AuthUseCase;
import com.pragma.usuarios.domain.usecase.UsuarioUseCase;
import com.pragma.usuarios.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.IUsuarioEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.IUsuarioRepository;
import com.pragma.usuarios.infrastructure.output.security.PasswordEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

/** Aquí se "cablea" el hexágono: puertos de salida -> adaptadores, casos de uso -> puertos de entrada. */
@Configuration
public class BeanConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IPasswordEncoderPort passwordEncoderPort(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }

    @Bean
    public IUsuarioPersistencePort usuarioPersistencePort(IUsuarioRepository repository,
                                                          IUsuarioEntityMapper mapper) {
        return new UsuarioJpaAdapter(repository, mapper);
    }

    @Bean
    public IUsuarioServicePort usuarioServicePort(IUsuarioPersistencePort persistencePort,
                                                  IPasswordEncoderPort passwordEncoderPort,
                                                  Clock clock) {
        return new UsuarioUseCase(persistencePort, passwordEncoderPort, clock);
    }

    @Bean
    public IAuthServicePort authServicePort(IUsuarioPersistencePort persistencePort,
                                            IPasswordEncoderPort passwordEncoderPort,
                                            ITokenProviderPort tokenProviderPort) {
        return new AuthUseCase(persistencePort, passwordEncoderPort, tokenProviderPort);
    }
}

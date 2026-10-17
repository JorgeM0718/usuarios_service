package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IUsuarioPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Crea el administrador inicial (no existe endpoint público para crear administradores).
 * Credenciales configurables por variables de entorno (ver application.yml).
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final IUsuarioPersistencePort persistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final Usuario admin;

    public AdminSeeder(IUsuarioPersistencePort persistencePort,
                       IPasswordEncoderPort passwordEncoderPort,
                       @Value("${app.admin.nombre}") String nombre,
                       @Value("${app.admin.apellido}") String apellido,
                       @Value("${app.admin.documento}") String documento,
                       @Value("${app.admin.celular}") String celular,
                       @Value("${app.admin.fecha-nacimiento}") String fechaNacimiento,
                       @Value("${app.admin.correo}") String correo,
                       @Value("${app.admin.clave}") String clave) {
        this.persistencePort = persistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.admin = Usuario.builder()
                .nombre(nombre)
                .apellido(apellido)
                .documentoIdentidad(documento)
                .celular(celular)
                .fechaNacimiento(LocalDate.parse(fechaNacimiento))
                .correo(correo.trim().toLowerCase())
                .clave(clave)
                .rol(Rol.ADMINISTRADOR)
                .build();
    }

    @Override
    public void run(String... args) {
        if (persistencePort.existePorCorreo(admin.getCorreo())) {
            return;
        }
        admin.setClave(passwordEncoderPort.encriptar(admin.getClave()));
        persistencePort.guardar(admin);
        log.info("Administrador inicial creado con el correo {}", admin.getCorreo());
    }
}

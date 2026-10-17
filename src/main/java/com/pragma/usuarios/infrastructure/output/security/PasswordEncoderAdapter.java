package com.pragma.usuarios.infrastructure.output.security;

import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptar(String claveEnClaro) {
        return passwordEncoder.encode(claveEnClaro);
    }

    @Override
    public boolean coincide(String claveEnClaro, String claveEncriptada) {
        return passwordEncoder.matches(claveEnClaro, claveEncriptada);
    }
}

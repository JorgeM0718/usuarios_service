package com.pragma.usuarios.domain.spi;

public interface IPasswordEncoderPort {

    String encriptar(String claveEnClaro);

    boolean coincide(String claveEnClaro, String claveEncriptada);
}

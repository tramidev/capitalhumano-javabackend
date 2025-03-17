package com.sensormanager.iot.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenerateApiKey {

	/**
     * Genera un API Key basado en una entrada de datos usando SHA-256 + Base64.
     * @param input Datos a encriptar
     * @return API Key en formato Base64 (44 caracteres)
     */
    public static String generate(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // 44 caracteres
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generando API Key", e);
        }
    }
}

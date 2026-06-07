package org.krypto.lab1.dto;

public record AutoDetectResponse(int key, String decryptedText, double confidence) {
}

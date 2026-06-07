package org.krypto.lab1.service;

import org.krypto.lab1.dto.AutoDetectResponse;

import java.util.Map;

public interface AttackService {
    int findKeyKnownPlaintext(String plainText, String cipherText);

    Map<Integer, String> bruteForceAll(String cipherText);

    AutoDetectResponse autoDetectKey(String cipherText);
}
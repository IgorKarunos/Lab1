package org.krypto.lab1.service.impl;

import org.krypto.lab1.service.CipherService;
import org.springframework.stereotype.Service;

@Service
public class CaesarCipherServiceImpl implements CipherService {

    private static final int ALPHABET_SIZE = 26;

    @Override
    public String encrypt(String text, int key) {
        return shift(text, key);
    }

    @Override
    public String decrypt(String text, int key) {
        return shift(text, -key);
    }

    private String shift(String text, int key) {
        StringBuilder result = new StringBuilder();
        // Нормализуем ключ, чтобы он всегда был от 0 до 25, даже если отрицательный
        int normalizedKey = ((key % ALPHABET_SIZE) + ALPHABET_SIZE) % ALPHABET_SIZE;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c) && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char shifted = (char) ((c - base + normalizedKey) % ALPHABET_SIZE + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}

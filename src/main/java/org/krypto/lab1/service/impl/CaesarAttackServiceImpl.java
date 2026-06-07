package org.krypto.lab1.service.impl;

import lombok.RequiredArgsConstructor;
import org.krypto.lab1.dto.AutoDetectResponse;
import org.krypto.lab1.service.AttackService;
import org.krypto.lab1.service.CipherService;
import org.krypto.lab1.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CaesarAttackServiceImpl implements AttackService {

    private final CipherService cipherService;
    private final DictionaryService dictionaryService;

    @Override
    public int findKeyKnownPlaintext(String plainText, String cipherText) {
        int pIdx = findFirstLetterIndex(plainText);
        int cIdx = findFirstLetterIndex(cipherText);

        if (pIdx == -1 || cIdx == -1) {
            throw new IllegalArgumentException("Тексты должны содержать хотя бы одну латинскую букву");
        }

        char p = plainText.charAt(pIdx);
        char c = cipherText.charAt(cIdx);
        int base = Character.isUpperCase(p) ? 'A' : 'a';

        int key = ((c - base) - (p - base) + 26) % 26;

        // Верификация: проверяем, подходит ли ключ ко всему тексту
        if (!cipherService.encrypt(plainText, key).equals(cipherText)) {
            throw new IllegalArgumentException("Не удалось найти ключ: тексты не соответствуют шифру Цезаря");
        }
        return key;
    }

    @Override
    public Map<Integer, String> bruteForceAll(String cipherText) {
        Map<Integer, String> variants = new LinkedHashMap<>();
        IntStream.range(0, 26).forEach(key ->
                variants.put(key, cipherService.decrypt(cipherText, key))
        );
        return variants;
    }

    @Override
    public AutoDetectResponse autoDetectKey(String cipherText) {
        int bestKey = 0;
        double maxScore = -1.0;
        String bestText = "";

        for (int key = 0; key < 26; key++) {
            String decrypted = cipherService.decrypt(cipherText, key);
            double score = dictionaryService.calculateWordMatchScore(decrypted);

            if (score > maxScore) {
                maxScore = score;
                bestKey = key;
                bestText = decrypted;
            }
        }
        return new AutoDetectResponse(bestKey, bestText, maxScore);
    }

    private int findFirstLetterIndex(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) return i;
        }
        return -1;
    }
}

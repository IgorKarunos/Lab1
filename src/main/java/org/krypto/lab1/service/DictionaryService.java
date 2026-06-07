package org.krypto.lab1.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DictionaryService {

    private final Set<String> dictionary = new HashSet<>();
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+");

    @PostConstruct
    public void loadDictionary() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("dictionary.txt").getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            dictionary.addAll(Set.of("the", "and", "for", "are", "but", "not", "you", "all", "can",
                    "hello", "world", "attack", "defense", "crypto", "key"));
        }
    }

    public double calculateWordMatchScore(String text) {
        if (text == null || text.isBlank()) return 0.0;

        Matcher matcher = WORD_PATTERN.matcher(text);
        int totalWords = 0;
        int matchedWords = 0;

        while (matcher.find()) {
            totalWords++;
            if (dictionary.contains(matcher.group().toLowerCase())) {
                matchedWords++;
            }
        }
        return totalWords == 0 ? 0.0 : (double) matchedWords / totalWords;
    }
}

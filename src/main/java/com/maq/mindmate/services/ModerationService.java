package com.maq.mindmate.services;

import com.maq.mindmate.exceptions.BadWordsFileLoadException;
import com.maq.mindmate.exceptions.EmptyBadWordListException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class ModerationService {

    private final Set<String> badWords = ConcurrentHashMap.newKeySet();
    private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    public ModerationService() {
        loadBadWordsFromFile("badwords.txt");
        if (badWords.isEmpty()) {
            throw new EmptyBadWordListException();
        }
    }

    private void loadBadWordsFromFile(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource(filename);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                        .forEach(this::addBadWord);
            }
        } catch (Exception e) {
            throw new BadWordsFileLoadException("Failed to load bad words from file: " + filename, e);
        }
    }

    public void setBadWords(List<String> words) {
        badWords.clear();
        patternCache.clear();
        words.forEach(this::addBadWord);
    }

    public void addBadWord(String word) {
        if (word == null || word.isBlank()) return;
        String normalized = word.trim().toLowerCase();
        badWords.add(normalized);
        patternCache.put(normalized, Pattern.compile("\\b" + Pattern.quote(normalized) + "\\b", Pattern.CASE_INSENSITIVE));
    }

    public void removeBadWord(String word) {
        if (word == null || word.isBlank()) return;
        String normalized = word.trim().toLowerCase();
        badWords.remove(normalized);
        patternCache.remove(normalized);
    }

    public boolean containsBadWords(String content) {
        if (content == null || content.isBlank()) return false;
        return patternCache.values().stream().anyMatch(p -> p.matcher(content).find());
    }

    public List<String> getDetectedBadWords(String content) {
        if (content == null || content.isBlank()) return Collections.emptyList();
        List<String> found = new ArrayList<>();
        for (Map.Entry<String, Pattern> entry : patternCache.entrySet()) {
            if (entry.getValue().matcher(content).find()) {
                found.add(entry.getKey());
            }
        }
        return found;
    }

    public String maskBadWords(String content) {
        if (content == null || content.isBlank()) return content;
        String result = content;
        for (Pattern pattern : patternCache.values()) {
            result = pattern.matcher(result).replaceAll(m -> "*".repeat(m.group().length()));
        }
        return result;
    }

    public Set<String> getBadWords() {
        return Collections.unmodifiableSet(badWords);
    }
}

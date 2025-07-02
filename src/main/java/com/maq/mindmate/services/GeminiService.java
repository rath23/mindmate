package com.maq.mindmate.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> defaultTasks = List.of(
            "Take 3 deep breaths",
            "Write down a positive thought",
            "Drink a glass of water"
    );

    public List<String> generateDailyTasks(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Enhanced prompt with strict formatting instructions
            String prompt = String.format(
                    "Generate exactly 3 daily mental wellness tasks for %s. " +
                            "Each task must be: " +
                            "- Short and action-oriented (max 10 words) " +
                            "- Without explanations, parentheses, or markdown formatting " +
                            "- Numbered 1. 2. 3. only " +
                            "Output example: " +
                            "1. Practice deep breathing\\n" +
                            "2. Journal for 5 minutes\\n" +
                            "3. Take a mindful walk",
                    username
            );

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            String json = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Unexpected response code: " + response.statusCode());
            }

            JsonNode root = objectMapper.readTree(response.body());
            String text = root.at("/candidates/0/content/parts/0/text").asText();

            return parseAndValidateTasks(text);

        } catch (Exception e) {
            e.printStackTrace();
            return defaultTasks; // Return defaults on error
        }
    }

    private List<String> parseAndValidateTasks(String responseText) {
        // Parse tasks with enhanced cleaning
        List<String> tasks = Arrays.stream(responseText.split("\n"))
                .map(this::cleanTask)
                .filter(task -> !task.isBlank())
                .limit(3) // Ensure maximum of 3 tasks
                .toList();

        // Fill with defaults if fewer than 3 tasks
        if (tasks.size() < 3) {
            List<String> validatedTasks = new ArrayList<>(tasks);
            for (int i = tasks.size(); i < 3; i++) {
                validatedTasks.add(defaultTasks.get(i));
            }
            return validatedTasks;
        }
        return tasks;
    }

    private String cleanTask(String task) {
        return task
                // Remove numbering prefixes (1., 2., etc)
                .replaceAll("^[0-9.\\-\\s]+", "")
                // Remove markdown formatting
                .replaceAll("\\*\\*", "")
                .replaceAll("\\*", "")
                // Remove explanatory text in parentheses
                .replaceAll("\\(.*\\)", "")
                .trim();
    }
}
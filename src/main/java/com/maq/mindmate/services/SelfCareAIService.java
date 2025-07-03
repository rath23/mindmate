package com.maq.mindmate.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maq.mindmate.dto.MoodEntryRequest;

import com.maq.mindmate.models.User;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SelfCareAIService {

    @Autowired
    private MoodService moodService;

    @Autowired
    private UserService userService;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${gemini.api.key}")
    private String geminiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Pattern JSON_MARKDOWN_PATTERN = Pattern.compile("^\\s*```json\\s*(.*?)\\s*```\\s*$", Pattern.DOTALL);

    public List<Map<String, String>> generateSuggestions(List<MoodEntryRequest> recentMoods) {
        String prompt = buildPrompt(recentMoods);

        try {
            String response = callGemini(prompt);
            String cleanedResponse = cleanJsonResponse(response);
            return extractSuggestions(cleanedResponse);
        } catch (Exception ex) {
            System.err.println("Error generating suggestions: " + ex.getMessage());
            return List.of();
        }
    }

    private String buildPrompt(List<MoodEntryRequest> recent) {
        MoodEntryRequest mood = recent.get(0);
        String tags = String.join(", ", mood.getTags());

        return String.format("""
                You are a mental health assistant.
                User's mood today is: %s
                Tags: %s

                Suggest EXACTLY 3 self-care activities with explanations:
                - The FIRST suggestion MUST be the BEST recommendation for this specific mood/tags
                - The next two should be good alternatives
                - Each suggestion MUST include:
                    • A plain text title (no emoji)
                    • A relevant emoji in a SEPARATE 'emoji' field
                    • Clear reasoning tied to the user's mood/tags
                
                Respond with ONLY the raw JSON (no Markdown, no additional text) in this exact format:
                {
                  "suggestions": [
                    {
                      "title": "Nature Walk",
                      "type": "Outdoor Activity",
                      "content": "Take a 15-minute walk in a park",
                      "reason": "Best for your current mood because...",
                      "emoji": "🌳"
                    },
                    {
                      "title": "...",
                      "type": "...",
                      "content": "...",
                      "reason": "...",
                      "emoji": "..."
                    },
                    {
                      "title": "...",
                      "type": "...",
                      "content": "...",
                      "reason": "...",
                      "emoji": "..."
                    }
                  ]
                }
                """, mood.getMood(), tags);
    }


    private String callGemini(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json"
                )
        );

        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());

        if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
            JsonNode content = root.get("candidates").get(0).get("content");
            if (content != null && content.has("parts")) {
                JsonNode parts = content.get("parts");
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).get("text").asText();
                }
            }
        }

        throw new RuntimeException("Invalid Gemini response: " + response.body());
    }


    private String cleanJsonResponse(String response) {
        // Remove Markdown code block wrappers if present
        return JSON_MARKDOWN_PATTERN.matcher(response).replaceFirst("$1");
    }

    private List<Map<String, String>> extractSuggestions(String json) throws JsonProcessingException {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode suggestions = node.get("suggestions");
            return objectMapper.convertValue(suggestions, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse JSON response: " + json);
            throw e;
        }
    }




    public ResponseEntity<?> getAISuggestions(User user) {
        List<MoodEntryRequest> recent = moodService.getMoodHistory(user).stream().limit(5).toList();

        if (recent.isEmpty()) {
            return ResponseEntity.badRequest().body("Please submit a mood first.");
        }

        List<Map<String, String>> suggestions = generateSuggestions(recent);
        return ResponseEntity.ok(Map.of("suggestions", suggestions));
    }
}

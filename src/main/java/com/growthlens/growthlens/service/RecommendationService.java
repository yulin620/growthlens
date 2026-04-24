package com.growthlens.growthlens.service;

import com.growthlens.growthlens.model.UserLifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RecommendationService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateRecommendation(UserLifecycle user) {
        String prompt = buildPrompt(user);

        // Build request body
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));
        body.put("max_tokens", 300);
        body.put("temperature", 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    request,
                    Map.class
            );

            // Parse response
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> responseMessage = (Map<String, Object>) choice.get("message");
            return (String) responseMessage.get("content");

        } catch (Exception e) {
            return "Unable to generate recommendations at this time. Please try again later.";
        }
    }

    private String buildPrompt(UserLifecycle user) {
        return String.format("""
            You are a growth PM assistant. A user has become at-risk of churning.
            
            User profile:
            - Lifecycle segment: %s
            - Reason tag: %s
            - Acquisition channel: %s
            - Sessions in last 30 days: %d
            - Last active: %d days ago
            - Total spent: $%.2f
            
            Give exactly 3 short, practical, action-oriented re-engagement suggestions.
            Each suggestion should be 1-2 sentences max.
            Do NOT use vague suggestions like "improve engagement".
            Format as a numbered list (1. 2. 3.)
            """,
                user.getLifecycleSegment(),
                user.getReasonTag() != null ? user.getReasonTag() : "N/A",
                user.getAcquisitionChannel(),
                user.getSessionsLast30Days(),
                user.getLastActiveDaysAgo(),
                user.getTotalSpent()
        );
    }
}
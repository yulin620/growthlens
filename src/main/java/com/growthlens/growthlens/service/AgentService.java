package com.growthlens.growthlens.service;

import com.growthlens.growthlens.model.ChannelLTV;
import com.growthlens.growthlens.model.ChannelRetentionSummary;
import com.growthlens.growthlens.model.UserLifecycle;
import com.growthlens.growthlens.repository.LTVRepository;
import com.growthlens.growthlens.repository.UserLifecycleRepository;
import com.growthlens.growthlens.repository.ChannelRetentionSummaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserLifecycleRepository lifecycleRepository;
    private final LTVRepository ltvRepository;
    private final ChannelRetentionSummaryRepository channelRepository;

    public AgentService(UserLifecycleRepository lifecycleRepository,
                        LTVRepository ltvRepository,
                        ChannelRetentionSummaryRepository channelRepository) {
        this.lifecycleRepository = lifecycleRepository;
        this.ltvRepository = ltvRepository;
        this.channelRepository = channelRepository;
    }

    public String answer(String question) {
        // Step 1: gather all relevant data
        String context = buildContext();

        // Step 2: build prompt with data context
        String prompt = String.format("""
            You are a growth analytics AI assistant for GrowthLens.
            
            You have access to the following real data from the platform:
            
            %s
            
            User question: %s
            
            Based on the data above, provide a clear, concise, data-driven answer.
            - Reference specific numbers from the data
            - Give 2-3 actionable recommendations
            - Keep the response under 200 words
            """, context, question);

        // Step 3: call Groq API
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));
        body.put("max_tokens", 400);
        body.put("temperature", 0.7);

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
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> msg = (Map<String, Object>) choice.get("message");
            return (String) msg.get("content");
        } catch (Exception e) {
            return "Unable to generate answer at this time. Please try again later.";
        }
    }

    private String buildContext() {
        StringBuilder ctx = new StringBuilder();

        // Channel retention data
        List<ChannelRetentionSummary> channels = channelRepository.findAll();
        ctx.append("=== Channel Analytics ===\n");
        for (ChannelRetentionSummary c : channels) {
            ctx.append(String.format("- %s: %d users, $%.2f revenue, %.1f%% retention\n",
                    c.getChannelName(), c.getTotalUsers(),
                    c.getTotalRevenue(), c.getRetentionRatePct()));
        }

        // LTV data
        List<ChannelLTV> ltvList = ltvRepository.findChannelLTV();
        ctx.append("\n=== LTV by Channel ===\n");
        for (ChannelLTV l : ltvList) {
            ctx.append(String.format("- %s: avg LTV $%.2f\n",
                    l.getChannelName(), l.getAvgLTV()));
        }

        // Lifecycle summary
        List<UserLifecycle> users = lifecycleRepository.findAllWithLifecycle();
        Map<String, Long> segmentCount = users.stream()
                .collect(Collectors.groupingBy(UserLifecycle::getLifecycleSegment, Collectors.counting()));
        ctx.append("\n=== User Lifecycle Segments ===\n");
        segmentCount.forEach((seg, count) ->
                ctx.append(String.format("- %s: %d users\n", seg, count)));

        // At-risk users
        List<UserLifecycle> atRisk = lifecycleRepository.findAtRiskUsers();
        ctx.append(String.format("\nAt-risk users: %d total\n", atRisk.size()));
        for (UserLifecycle u : atRisk) {
            ctx.append(String.format("- %s (channel: %s, last active: %d days ago, spent: $%.2f, reason: %s)\n",
                    u.getEmail(), u.getAcquisitionChannel(),
                    u.getLastActiveDaysAgo(), u.getTotalSpent(),
                    u.getReasonTag() != null ? u.getReasonTag() : "N/A"));
        }

        return ctx.toString();
    }
}
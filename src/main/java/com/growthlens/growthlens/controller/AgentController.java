package com.growthlens.growthlens.controller;

import com.growthlens.growthlens.service.AgentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    // POST /api/agent/query
    @PostMapping("/query")
    public Map<String, Object> query(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question == null || question.trim().isEmpty()) {
            return Map.of("error", "Question cannot be empty");
        }
        String answer = agentService.answer(question);
        return Map.of(
                "question", question,
                "answer", answer
        );
    }
}
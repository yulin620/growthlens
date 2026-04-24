package com.growthlens.growthlens.controller;

import com.growthlens.growthlens.model.UserLifecycle;
import com.growthlens.growthlens.service.UserLifecycleService;
import com.growthlens.growthlens.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserLifecycleController {

    private final UserLifecycleService lifecycleService;
    private final RecommendationService recommendationService;

    public UserLifecycleController(UserLifecycleService lifecycleService,
                                   RecommendationService recommendationService) {
        this.lifecycleService = lifecycleService;
        this.recommendationService = recommendationService;
    }

    // GET /api/users/lifecycle - all users with their lifecycle segment
    @GetMapping("/lifecycle")
    public List<UserLifecycle> getAllUsers() {
        return lifecycleService.getAllUsers();
    }

    // GET /api/users/lifecycle/summary - count per segment
    @GetMapping("/lifecycle/summary")
    public Map<String, Long> getSegmentSummary() {
        return lifecycleService.getSegmentSummary();
    }

    // GET /api/users/at-risk - only at-risk users
    @GetMapping("/at-risk")
    public List<UserLifecycle> getAtRiskUsers() {
        return lifecycleService.getAtRiskUsers();
    }

    // POST /api/users/{id}/recommendation - AI re-engagement suggestions
    @PostMapping("/{id}/recommendation")
    public Map<String, Object> getRecommendation(@PathVariable Integer id) {
        UserLifecycle user = lifecycleService.getUserById(id);
        if (user == null) {
            return Map.of("error", "User not found");
        }
        String recommendation = recommendationService.generateRecommendation(user);
        return Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "lifecycleSegment", user.getLifecycleSegment(),
                "reasonTag", user.getReasonTag() != null ? user.getReasonTag() : "N/A",
                "suggestions", recommendation
        );
    }
}
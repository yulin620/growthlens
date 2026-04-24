package com.growthlens.growthlens.service;

import com.growthlens.growthlens.model.UserLifecycle;
import com.growthlens.growthlens.repository.UserLifecycleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserLifecycleService {

    private final UserLifecycleRepository repository;

    public UserLifecycleService(UserLifecycleRepository repository) {
        this.repository = repository;
    }

    public List<UserLifecycle> getAllUsers() {
        return repository.findAllWithLifecycle();
    }

    public List<UserLifecycle> getAtRiskUsers() {
        return repository.findAtRiskUsers();
    }

    public UserLifecycle getUserById(Integer userId) {
        return repository.findById(userId);
    }

    // Summary count by segment
    public Map<String, Long> getSegmentSummary() {
        List<UserLifecycle> all = repository.findAllWithLifecycle();
        return Map.of(
                "New",     all.stream().filter(u -> "New".equals(u.getLifecycleSegment())).count(),
                "Active",  all.stream().filter(u -> "Active".equals(u.getLifecycleSegment())).count(),
                "At-Risk", all.stream().filter(u -> "At-Risk".equals(u.getLifecycleSegment())).count(),
                "Churned", all.stream().filter(u -> "Churned".equals(u.getLifecycleSegment())).count()
        );
    }
}
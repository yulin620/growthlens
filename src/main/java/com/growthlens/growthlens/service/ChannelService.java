package com.growthlens.growthlens.service;

import com.growthlens.growthlens.model.ChannelRetentionSummary;
import com.growthlens.growthlens.repository.ChannelRetentionSummaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRetentionSummaryRepository repository;

    public ChannelService(ChannelRetentionSummaryRepository repository) {
        this.repository = repository;
    }

    public List<ChannelRetentionSummary> getAllChannelStats() {
        return repository.findAll();
    }
}
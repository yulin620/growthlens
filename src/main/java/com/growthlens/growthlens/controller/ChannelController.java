package com.growthlens.growthlens.controller;

import com.growthlens.growthlens.model.ChannelRetentionSummary;
import com.growthlens.growthlens.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@CrossOrigin(origins = "*")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping
    public List<ChannelRetentionSummary> getAllChannelStats() {
        return channelService.getAllChannelStats();
    }
}
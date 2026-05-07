package com.growthlens.growthlens.controller;

import com.growthlens.growthlens.model.ChannelLTV;
import com.growthlens.growthlens.service.LTVService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ltv")
@CrossOrigin(origins = "*")
public class LTVController {

    private final LTVService ltvService;

    public LTVController(LTVService ltvService) {
        this.ltvService = ltvService;
    }

    // GET /api/ltv/channels - LTV by acquisition channel
    @GetMapping("/channels")
    public List<ChannelLTV> getChannelLTV() {
        return ltvService.getChannelLTV();
    }
}
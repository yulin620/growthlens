package com.growthlens.growthlens.service;

import com.growthlens.growthlens.model.ChannelLTV;
import com.growthlens.growthlens.repository.LTVRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LTVService {

    private final LTVRepository repository;

    public LTVService(LTVRepository repository) {
        this.repository = repository;
    }

    public List<ChannelLTV> getChannelLTV() {
        return repository.findChannelLTV();
    }
}
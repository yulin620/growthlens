package com.growthlens.growthlens.repository;

import com.growthlens.growthlens.model.ChannelRetentionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRetentionSummaryRepository
        extends JpaRepository<ChannelRetentionSummary, String> {
}
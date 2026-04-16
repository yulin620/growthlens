package com.growthlens.growthlens.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "vw_channelretentionsummary")
public class ChannelRetentionSummary {

    @Id
    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "total_users")
    private Long totalUsers;

    @Column(name = "active_users")
    private Long activeUsers;

    @Column(name = "total_purchases")
    private Long totalPurchases;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "retention_rate_pct")
    private BigDecimal retentionRatePct;
}
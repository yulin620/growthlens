package com.growthlens.growthlens.model;

public class ChannelLTV {

    private String channelName;
    private Long totalUsers;
    private Double totalRevenue;
    private Double avgLTV;

    public ChannelLTV() {}

    public ChannelLTV(String channelName, Long totalUsers, Double totalRevenue, Double avgLTV) {
        this.channelName = channelName;
        this.totalUsers = totalUsers;
        this.totalRevenue = totalRevenue;
        this.avgLTV = avgLTV;
    }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Double getAvgLTV() { return avgLTV; }
    public void setAvgLTV(Double avgLTV) { this.avgLTV = avgLTV; }
}
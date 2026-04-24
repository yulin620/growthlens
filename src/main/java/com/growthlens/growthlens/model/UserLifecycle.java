package com.growthlens.growthlens.model;

public class UserLifecycle {

    private Integer userId;
    private String email;
    private String signupDate;
    private String lifecycleSegment;
    private String reasonTag;
    private String acquisitionChannel;
    private Integer sessionsLast30Days;
    private Integer lastActiveDaysAgo;
    private Double totalSpent;

    public UserLifecycle() {}

    public UserLifecycle(Integer userId, String email, String signupDate,
                         String lifecycleSegment, String reasonTag,
                         String acquisitionChannel, Integer sessionsLast30Days,
                         Integer lastActiveDaysAgo, Double totalSpent) {
        this.userId = userId;
        this.email = email;
        this.signupDate = signupDate;
        this.lifecycleSegment = lifecycleSegment;
        this.reasonTag = reasonTag;
        this.acquisitionChannel = acquisitionChannel;
        this.sessionsLast30Days = sessionsLast30Days;
        this.lastActiveDaysAgo = lastActiveDaysAgo;
        this.totalSpent = totalSpent;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSignupDate() { return signupDate; }
    public void setSignupDate(String signupDate) { this.signupDate = signupDate; }

    public String getLifecycleSegment() { return lifecycleSegment; }
    public void setLifecycleSegment(String lifecycleSegment) { this.lifecycleSegment = lifecycleSegment; }

    public String getReasonTag() { return reasonTag; }
    public void setReasonTag(String reasonTag) { this.reasonTag = reasonTag; }

    public String getAcquisitionChannel() { return acquisitionChannel; }
    public void setAcquisitionChannel(String acquisitionChannel) { this.acquisitionChannel = acquisitionChannel; }

    public Integer getSessionsLast30Days() { return sessionsLast30Days; }
    public void setSessionsLast30Days(Integer sessionsLast30Days) { this.sessionsLast30Days = sessionsLast30Days; }

    public Integer getLastActiveDaysAgo() { return lastActiveDaysAgo; }
    public void setLastActiveDaysAgo(Integer lastActiveDaysAgo) { this.lastActiveDaysAgo = lastActiveDaysAgo; }

    public Double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }
}
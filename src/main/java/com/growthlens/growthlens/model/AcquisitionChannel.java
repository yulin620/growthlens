package com.growthlens.growthlens.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AcquisitionChannel")
public class AcquisitionChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "channel_name", nullable = false)
    private String channelName;
}
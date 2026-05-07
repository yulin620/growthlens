package com.growthlens.growthlens.repository;

import com.growthlens.growthlens.model.ChannelLTV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LTVRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String LTV_QUERY = """
        SELECT
            ac.channel_name,
            COUNT(DISTINCT u.user_id) AS total_users,
            COALESCE(SUM(pay.amount), 0) AS total_revenue,
            COALESCE(SUM(pay.amount), 0) / COUNT(DISTINCT u.user_id) AS avg_ltv
        FROM AcquisitionChannel ac
        JOIN UserAcquisition ua ON ac.channel_id = ua.channel_id
        JOIN User u ON ua.user_id = u.user_id
        LEFT JOIN Purchase p ON u.user_id = p.user_id
        LEFT JOIN Payment pay ON p.purchase_id = pay.purchase_id
            AND pay.payment_status = 'Paid'
        GROUP BY ac.channel_id, ac.channel_name
        ORDER BY avg_ltv DESC
        """;

    public List<ChannelLTV> findChannelLTV() {
        return jdbcTemplate.query(LTV_QUERY, (rs, rowNum) -> {
            ChannelLTV ltv = new ChannelLTV();
            ltv.setChannelName(rs.getString("channel_name"));
            ltv.setTotalUsers(rs.getLong("total_users"));
            ltv.setTotalRevenue(rs.getDouble("total_revenue"));
            ltv.setAvgLTV(rs.getDouble("avg_ltv"));
            return ltv;
        });
    }
}
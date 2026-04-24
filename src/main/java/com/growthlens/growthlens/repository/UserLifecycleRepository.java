package com.growthlens.growthlens.repository;

import com.growthlens.growthlens.model.UserLifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserLifecycleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String LIFECYCLE_QUERY = """
        SELECT
            u.user_id,
            u.email,
            DATE_FORMAT(u.signup_date, '%Y-%m-%d') AS signup_date,
            ac.channel_name AS acquisition_channel,
            DATEDIFF(CURDATE(), u.signup_date) AS days_since_signup,

            COALESCE(s30.cnt, 0) AS sessions_last_30_days,

            COALESCE(DATEDIFF(CURDATE(), last_s.last_session), 9999) AS last_active_days_ago,

            COALESCE(rev.total_spent, 0) AS total_spent,

            CASE
                WHEN DATEDIFF(CURDATE(), u.signup_date) <= 7
                    THEN 'New'
                WHEN s7.cnt > 0
                    THEN 'Active'
                WHEN s30.cnt > 0
                    THEN 'At-Risk'
                ELSE 'Churned'
            END AS lifecycle_segment,

            CASE
                WHEN s_any.cnt > 0 AND COALESCE(rev.total_spent, 0) = 0
                    THEN 'No Conversion'
                ELSE NULL
            END AS reason_tag

        FROM User u

        -- first-touch acquisition channel
        LEFT JOIN (
            SELECT ua.user_id, MIN(ua.acquisition_date) AS first_date
            FROM UserAcquisition ua
            GROUP BY ua.user_id
        ) first_ua ON u.user_id = first_ua.user_id
        LEFT JOIN UserAcquisition ua2
            ON u.user_id = ua2.user_id
            AND ua2.acquisition_date = first_ua.first_date
        LEFT JOIN AcquisitionChannel ac ON ua2.channel_id = ac.channel_id

        -- sessions in last 7 days
        LEFT JOIN (
            SELECT user_id, COUNT(*) AS cnt
            FROM Session
            WHERE session_start >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
            GROUP BY user_id
        ) s7 ON u.user_id = s7.user_id

        -- sessions in last 30 days
        LEFT JOIN (
            SELECT user_id, COUNT(*) AS cnt
            FROM Session
            WHERE session_start >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY user_id
        ) s30 ON u.user_id = s30.user_id

        -- any session ever
        LEFT JOIN (
            SELECT user_id, COUNT(*) AS cnt
            FROM Session
            GROUP BY user_id
        ) s_any ON u.user_id = s_any.user_id

        -- last session date
        LEFT JOIN (
            SELECT user_id, MAX(DATE(session_start)) AS last_session
            FROM Session
            GROUP BY user_id
        ) last_s ON u.user_id = last_s.user_id

        -- total revenue (paid only)
        LEFT JOIN (
            SELECT p.user_id, SUM(pay.amount) AS total_spent
            FROM Purchase p
            JOIN Payment pay ON p.purchase_id = pay.purchase_id
            WHERE pay.payment_status = 'Paid'
            GROUP BY p.user_id
        ) rev ON u.user_id = rev.user_id

        GROUP BY u.user_id, u.email, u.signup_date,
                 ac.channel_name, s7.cnt, s30.cnt,
                 s_any.cnt, last_s.last_session, rev.total_spent
        ORDER BY u.user_id
        """;

    public List<UserLifecycle> findAllWithLifecycle() {
        return jdbcTemplate.query(LIFECYCLE_QUERY, (rs, rowNum) -> {
            UserLifecycle ul = new UserLifecycle();
            ul.setUserId(rs.getInt("user_id"));
            ul.setEmail(rs.getString("email"));
            ul.setSignupDate(rs.getString("signup_date"));
            ul.setAcquisitionChannel(rs.getString("acquisition_channel"));
            ul.setSessionsLast30Days(rs.getInt("sessions_last_30_days"));
            ul.setLastActiveDaysAgo(rs.getInt("last_active_days_ago"));
            ul.setTotalSpent(rs.getDouble("total_spent"));
            ul.setLifecycleSegment(rs.getString("lifecycle_segment"));
            ul.setReasonTag(rs.getString("reason_tag"));
            return ul;
        });
    }

    public List<UserLifecycle> findAtRiskUsers() {
        return findAllWithLifecycle().stream()
                .filter(u -> "At-Risk".equals(u.getLifecycleSegment()))
                .toList();
    }

    public UserLifecycle findById(Integer userId) {
        return findAllWithLifecycle().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
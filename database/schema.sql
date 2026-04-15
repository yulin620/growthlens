-- =========================================================
-- GrowthLens Database Schema (MySQL)
-- Adapted from Team 6 User Analytics Database System
-- Northeastern University, Data Management & Database Design
-- =========================================================

CREATE DATABASE IF NOT EXISTS growthlens;
USE growthlens;

-- =========================================================
-- Drop tables in reverse dependency order
-- =========================================================
DROP TABLE IF EXISTS UserAcquisition;
DROP TABLE IF EXISTS ReportMetric;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS Report;
DROP TABLE IF EXISTS Purchase;
DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS ProductOrPlan;
DROP TABLE IF EXISTS EventType;
DROP TABLE IF EXISTS Campaign;
DROP TABLE IF EXISTS AcquisitionChannel;
DROP TABLE IF EXISTS Location;

-- =========================================================
-- 1. Location
-- =========================================================
CREATE TABLE Location (
    location_id   INT          NOT NULL AUTO_INCREMENT,
    country       VARCHAR(100) NOT NULL,
    state         VARCHAR(100) NOT NULL,
    city          VARCHAR(100) NOT NULL,
    PRIMARY KEY (location_id)
);

-- =========================================================
-- 2. AcquisitionChannel
-- =========================================================
CREATE TABLE AcquisitionChannel (
    channel_id    INT          NOT NULL AUTO_INCREMENT,
    channel_name  VARCHAR(100) NOT NULL,
    PRIMARY KEY (channel_id)
);

-- =========================================================
-- 3. Campaign
-- =========================================================
CREATE TABLE Campaign (
    campaign_id    INT          NOT NULL AUTO_INCREMENT,
    campaign_name  VARCHAR(150) NOT NULL,
    start_date     DATE         NOT NULL,
    end_date       DATE         NOT NULL,
    PRIMARY KEY (campaign_id),
    CONSTRAINT CHK_Campaign_Date CHECK (end_date >= start_date)
);

-- =========================================================
-- 4. EventType
-- =========================================================
CREATE TABLE EventType (
    event_type_id  INT          NOT NULL AUTO_INCREMENT,
    event_name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (event_type_id)
);

-- =========================================================
-- 5. ProductOrPlan
-- =========================================================
CREATE TABLE ProductOrPlan (
    product_id    INT             NOT NULL AUTO_INCREMENT,
    product_name  VARCHAR(150)    NOT NULL,
    product_type  VARCHAR(50)     NOT NULL,
    price         DECIMAL(10, 2)  NOT NULL,
    PRIMARY KEY (product_id),
    CONSTRAINT CHK_Price CHECK (price >= 0)
);

-- =========================================================
-- 6. User
-- =========================================================
CREATE TABLE User (
    user_id      INT          NOT NULL AUTO_INCREMENT,
    email        VARCHAR(255) NOT NULL,
    signup_date  DATE         NOT NULL,
    status       VARCHAR(50)  NOT NULL DEFAULT 'Active',
    location_id  INT          NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY UQ_User_Email (email),
    CONSTRAINT CHK_User_Status CHECK (status IN ('Active', 'Inactive', 'Suspended')),
    CONSTRAINT FK_User_Location FOREIGN KEY (location_id)
        REFERENCES Location(location_id)
);

-- =========================================================
-- 7. Session
-- =========================================================
CREATE TABLE Session (
    session_id     INT      NOT NULL AUTO_INCREMENT,
    user_id        INT      NOT NULL,
    session_start  DATETIME NOT NULL,
    session_end    DATETIME NOT NULL,
    duration       INT      NOT NULL COMMENT 'Duration in seconds',
    PRIMARY KEY (session_id),
    CONSTRAINT CHK_Session_Time CHECK (session_end >= session_start),
    CONSTRAINT CHK_Session_Duration CHECK (duration >= 0),
    CONSTRAINT FK_Session_User FOREIGN KEY (user_id)
        REFERENCES User(user_id)
);

-- =========================================================
-- 8. Purchase
-- =========================================================
CREATE TABLE Purchase (
    purchase_id    INT      NOT NULL AUTO_INCREMENT,
    user_id        INT      NOT NULL,
    product_id     INT      NOT NULL,
    purchase_date  DATETIME NOT NULL,
    PRIMARY KEY (purchase_id),
    CONSTRAINT FK_Purchase_User FOREIGN KEY (user_id)
        REFERENCES User(user_id),
    CONSTRAINT FK_Purchase_Product FOREIGN KEY (product_id)
        REFERENCES ProductOrPlan(product_id)
);

-- =========================================================
-- 9. Report
-- =========================================================
CREATE TABLE Report (
    report_id    INT          NOT NULL AUTO_INCREMENT,
    user_id      INT          NOT NULL,
    report_name  VARCHAR(150) NOT NULL,
    report_type  VARCHAR(100) NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (report_id),
    CONSTRAINT FK_Report_User FOREIGN KEY (user_id)
        REFERENCES User(user_id)
);

-- =========================================================
-- 10. Event
-- =========================================================
CREATE TABLE Event (
    event_id       INT      NOT NULL AUTO_INCREMENT,
    session_id     INT      NOT NULL,
    event_type_id  INT      NOT NULL,
    event_time     DATETIME NOT NULL,
    PRIMARY KEY (event_id),
    CONSTRAINT FK_Event_Session FOREIGN KEY (session_id)
        REFERENCES Session(session_id),
    CONSTRAINT FK_Event_EventType FOREIGN KEY (event_type_id)
        REFERENCES EventType(event_type_id)
);

-- =========================================================
-- 11. Payment
-- =========================================================
CREATE TABLE Payment (
    purchase_id      INT            NOT NULL,
    payment_no       INT            NOT NULL,
    amount           DECIMAL(10, 2) NOT NULL,
    payment_method   VARCHAR(50)    NOT NULL,
    payment_status   VARCHAR(50)    NOT NULL DEFAULT 'Pending',
    paid_at          DATETIME       NOT NULL,
    PRIMARY KEY (purchase_id, payment_no),
    CONSTRAINT CHK_Payment_Amount CHECK (amount >= 0),
    CONSTRAINT CHK_Payment_Status CHECK (payment_status IN ('Pending', 'Paid', 'Failed', 'Refunded')),
    CONSTRAINT FK_Payment_Purchase FOREIGN KEY (purchase_id)
        REFERENCES Purchase(purchase_id)
);

-- =========================================================
-- 12. ReportMetric
-- =========================================================
CREATE TABLE ReportMetric (
    metric_id         INT          NOT NULL AUTO_INCREMENT,
    report_id         INT          NOT NULL,
    metric_name       VARCHAR(150) NOT NULL,
    calculation_rule  VARCHAR(500) NOT NULL,
    PRIMARY KEY (metric_id),
    CONSTRAINT FK_ReportMetric_Report FOREIGN KEY (report_id)
        REFERENCES Report(report_id)
);

-- =========================================================
-- 13. UserAcquisition
-- =========================================================
CREATE TABLE UserAcquisition (
    user_id           INT  NOT NULL,
    channel_id        INT  NOT NULL,
    campaign_id       INT  NOT NULL,
    acquisition_date  DATE NOT NULL,
    PRIMARY KEY (user_id, channel_id, campaign_id, acquisition_date),
    CONSTRAINT FK_UA_User FOREIGN KEY (user_id)
        REFERENCES User(user_id),
    CONSTRAINT FK_UA_Channel FOREIGN KEY (channel_id)
        REFERENCES AcquisitionChannel(channel_id),
    CONSTRAINT FK_UA_Campaign FOREIGN KEY (campaign_id)
        REFERENCES Campaign(campaign_id)
);

-- =========================================================
-- Views
-- =========================================================

-- View 1: User Acquisition Summary
CREATE OR REPLACE VIEW vw_UserAcquisitionSummary AS
SELECT
    u.user_id,
    u.email,
    u.signup_date,
    u.status,
    ua.acquisition_date,
    ac.channel_name,
    c.campaign_name
FROM User u
JOIN UserAcquisition ua  ON u.user_id    = ua.user_id
JOIN AcquisitionChannel ac ON ua.channel_id = ac.channel_id
JOIN Campaign c          ON ua.campaign_id = c.campaign_id;

-- View 2: Purchase Payment Summary
CREATE OR REPLACE VIEW vw_PurchasePaymentSummary AS
SELECT
    p.purchase_id,
    u.user_id,
    u.email,
    pop.product_name,
    pop.product_type,
    p.purchase_date,
    pay.payment_no,
    pay.amount,
    pay.payment_method,
    pay.payment_status,
    pay.paid_at
FROM Purchase p
JOIN User u              ON p.user_id   = u.user_id
JOIN ProductOrPlan pop   ON p.product_id = pop.product_id
LEFT JOIN Payment pay    ON p.purchase_id = pay.purchase_id;

-- View 3: Channel Retention Summary (new — for GrowthLens dashboard)
CREATE OR REPLACE VIEW vw_ChannelRetentionSummary AS
SELECT
    ac.channel_name,
    COUNT(DISTINCT u.user_id)                          AS total_users,
    COUNT(DISTINCT CASE WHEN u.status = 'Active'
          THEN u.user_id END)                          AS active_users,
    COUNT(DISTINCT p.purchase_id)                      AS total_purchases,
    COALESCE(SUM(pay.amount), 0)                       AS total_revenue,
    ROUND(
        COUNT(DISTINCT CASE WHEN u.status = 'Active'
              THEN u.user_id END)
        / NULLIF(COUNT(DISTINCT u.user_id), 0) * 100
    , 2)                                               AS retention_rate_pct
FROM AcquisitionChannel ac
JOIN UserAcquisition ua  ON ac.channel_id  = ua.channel_id
JOIN User u              ON ua.user_id     = u.user_id
LEFT JOIN Purchase p     ON u.user_id      = p.user_id
LEFT JOIN Payment pay    ON p.purchase_id  = pay.purchase_id
GROUP BY ac.channel_id, ac.channel_name;

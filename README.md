# GrowthLens 🔍  
**AI-Powered Growth Analytics Copilot**

> Understand why users become inactive — and what to do about it.

---

## 🎯 Problem

Growth teams often struggle with:

- User data scattered across multiple sources (e.g., Excel, dashboards)
- Limited visibility into user behavior after acquisition
- Difficulty identifying why users stop engaging
- Lack of actionable insights to improve retention

---

## 💡 Solution

GrowthLens is an AI-powered analytics tool focused on one key problem:

> **Identifying at-risk users and helping teams take action to improve retention.**

The system combines user behavior data with lifecycle segmentation to:

- Identify users who are likely to churn  
- Diagnose potential reasons for inactivity  
- Generate actionable re-engagement strategies  

---

## 🚀 Current MVP (v1)

GrowthLens currently supports:

- **Channel Analytics Dashboard**  
  Analyze user acquisition performance, including retention and revenue by channel

- **User Lifecycle Segmentation**  
  Classify users into New / Active / At-Risk / Churned based on session activity

- **At-Risk User Detection**  
  Identify users who are likely to churn based on recent inactivity

- **AI-Powered Recommendations**  
  Generate simple re-engagement strategies using user behavior signals

---

## 🔥 Key Feature — At-Risk User Recovery

GrowthLens goes beyond traditional dashboards by addressing a critical growth problem:

> Users becoming inactive without clear reasons.

The system introduces a simple pipeline:

1. **Identify At-Risk Users**  
   Based on recent session activity (inactive in last 7 days but active within 30 days)

2. **Diagnose Behavior Patterns**  
   Classify users into categories such as:
   - Engagement Drop  
   - No Conversion  
   - One-Day User  

3. **Generate AI-Powered Strategies**  
   Use structured user signals to generate personalized re-engagement actions

This enables teams to move from:

> **Passive analytics → Proactive growth actions**

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React |
| Backend | Spring Boot (Java) |
| Database | MySQL |
| AI Layer | Python (LLM API integration) |

---

## 🗄️ Database Schema

The system integrates multiple aspects of user data:

- **User** — basic user information  
- **UserAcquisition / Channel / Campaign** — acquisition source  
- **Session** — user activity tracking  
- **Purchase / Payment** — monetization data  

---

## ⚠️ Known Limitations & Next Steps

### Retention Calculation
- **Current:** Uses user status as a proxy for activity  
- **Planned:** Implement cohort-based retention (Day 1 / Day 7 / Day 30) using session data  

### User Segmentation
- **Current:** Basic rule-based segmentation  
- **Planned:** Refine segmentation using more behavioral signals  

### Monetization Analysis
- **Planned:** Add LTV, CAC, and high-value user identification  

---

## 🔮 Roadmap

Future improvements include:

- Cohort-based retention analysis  
- High-value user segmentation  
- Revenue-risk alert system  
- A/B test analysis and recommendation  

---

## 🚀 Getting Started

```bash
# Database setup
mysql -u root growthlens < database/schema.sql
mysql -u root growthlens < database/seed.sql

# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install && npm start
```

---

## 📁 Project Structure

```
growthlens/
├── backend/          # Spring Boot (Java)
├── ai/               # Python + Groq Agent
├── frontend/         # React Dashboard
├── mobile/           # Flutter App
├── database/         # SQL schema + seed data
└── README.md
```

---

## 📅 Dev Log

| Date | What was built |
|------|---------------|
| Apr 16 | Project setup, MySQL schema, seed data, Spring Boot backend API, React dashboard with channel analytics |

---

## 👤 Author

Yulin Wang — Product Manager & Full-Stack Developer  
[LinkedIn](https://www.linkedin.com/in/yulin-wang-4b25b7311) | [GitHub](https://github.com/yulin620/growthlens)

*Inspired by real growth challenges at Sohu.com and Summer App*

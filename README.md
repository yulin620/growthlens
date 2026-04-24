# GrowthLens 🔍
**AI-Powered Growth Analytics Copilot**

> Turn scattered user data into actionable retention and acquisition insights — in real time, without waiting for engineers.

---

## 🎯 Problem

As a growth PM, I experienced these pain points firsthand:
- Channel data scattered across Excel sheets, requiring manual consolidation
- No real-time visibility into user behavior patterns
- A/B test results hard to interpret and act on
- Silent users churning without knowing why or how to re-engage them

---

## 💡 Solution

GrowthLens is an AI-powered analytics platform that automatically:
1. Aggregates user acquisition, behavior, and payment data in one place
2. Segments users by lifecycle stage (New / Active / At-Risk / Churned)
3. Answers growth questions in natural language ("Which channel has the best 30-day retention?")
4. Interprets A/B test results and recommends next actions
5. Identifies why users go silent and generates personalized re-engagement strategies

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React (Web Dashboard) |
| Mobile | Flutter |
| Backend | Spring Boot (Java) |
| AI Layer | Python + Groq API (LLM Agent) |
| Database | MySQL |
| Deployment | AWS (Lambda + API Gateway) |

---

## 📦 Core Modules

### Phase 1 — MVP (Week 1)
- [x] Channel analytics dashboard (retention rate, revenue by channel) — *Apr 16*
- [x] User lifecycle segmentation (New / Active / At-Risk / Churned) — *Apr 23*
- [x] At-risk user detection — *Apr 23*
- [x] AI re-engagement suggestions (Groq API) — *Apr 23*

### Phase 2 (Week 2-3)
- [ ] A/B test result interpreter
- [ ] Silent user analysis + re-engagement strategy generator
- [ ] AI Agent workflow (auto-generate full growth analysis report)

### Phase 3 (Week 4)
- [ ] Flutter mobile app
- [ ] Real-time data simulation
- [ ] Export report as PDF

---

## 🗄️ Database Schema

Based on User Analytics Database System (Northeastern University, Team 6):
- **User** — who the user is
- **AcquisitionChannel + Campaign + UserAcquisition** — where they came from
- **Session + Event + EventType** — what they do
- **Purchase + Payment + ProductOrPlan** — what they buy
- **Report + ReportMetric** — analytics output

---

## 🤖 AI Components

| Component | Source | Purpose |
|-----------|--------|---------|
| LLM Agent | Assignment 5 | Orchestrates full analysis workflow |
| Natural Language Query | Assignment 3 | Translate user questions to SQL insights |
| Growth Strategy Generator | Assignment 4 (DPO) | Generate high-quality re-engagement recommendations |

---

## ⚠️ Known Limitations & Next Steps

### Retention Rate
- **Current:** Retention is calculated using `user.status = 'Active'` as a proxy
- **Limitation:** This reflects account status, not actual behavioral retention
- **Planned:** Redefine retention based on Session table — calculate 1-day, 7-day, and 30-day retention by checking whether a user has a session record N days after signup

### CAC (Customer Acquisition Cost)
- **Current:** Not yet implemented
- **Limitation:** No ad spend data in the current schema
- **Planned:** Add `ad_spend` field to Campaign table, then calculate CAC = ad_spend / new_users per channel

### LTV (Lifetime Value)
- **Current:** Not yet implemented
- **Planned:** Calculate LTV per user based on Purchase + Payment tables, then aggregate by acquisition channel

### User Lifecycle Segmentation
- **Current:** Not yet implemented
- **Planned:** Segment users into New / Active / Silent / Churned based on last session date and purchase history

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
| Apr 23 | User lifecycle segmentation (New/Active/At-Risk/Churned), At-Risk user detection, AI re-engagement suggestions via Groq API, React frontend with 3 pages |

---

## 👤 Author

Yulin Wang — Product Manager & Full-Stack Developer  
[LinkedIn](https://www.linkedin.com/in/yulin-wang-4b25b7311) | [GitHub](https://github.com/yulin620/growthlens)

*Inspired by real growth challenges at Sohu.com and Summer App*

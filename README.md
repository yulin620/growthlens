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
- [ ] Channel analytics dashboard (CAC, retention rate, LTV by channel)
- [ ] User lifecycle segmentation (New / Active / Silent / Churned)
- [ ] Natural language query ("Which channel has the highest retention?")

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

## 🚀 Getting Started

```bash
# Backend
cd backend
./mvnw spring-boot:run

# AI Layer
cd ai
pip install -r requirements.txt
python agent.py

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

## 👤 Author

Yulin Wang — Product Manager & Full-Stack Developer  
[LinkedIn] | [GitHub]

*Inspired by real growth challenges at Sohu.com and Summer App*

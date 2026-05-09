import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell
} from 'recharts';
import './App.css';

const API = 'http://localhost:8080/api';

function App() {
  const [page, setPage] = useState('dashboard');
  const [channels, setChannels] = useState([]);
  const [lifecycle, setLifecycle] = useState([]);
  const [atRisk, setAtRisk] = useState([]);
  const [ltv, setLtv] = useState([]);
  const [loading, setLoading] = useState(true);
  const [recommendations, setRecommendations] = useState({});
  const [loadingRec, setLoadingRec] = useState({});

  // Ask AI state
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [loadingAnswer, setLoadingAnswer] = useState(false);

  useEffect(() => {
    Promise.all([
      axios.get(`${API}/channels`),
      axios.get(`${API}/users/lifecycle`),
      axios.get(`${API}/users/at-risk`),
      axios.get(`${API}/ltv/channels`),
    ]).then(([ch, lc, ar, lv]) => {
      setChannels(ch.data);
      setLifecycle(lc.data);
      setAtRisk(ar.data);
      setLtv(lv.data);
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  const getRecommendation = (userId) => {
    setLoadingRec(prev => ({ ...prev, [userId]: true }));
    axios.post(`${API}/users/${userId}/recommendation`)
      .then(res => {
        setRecommendations(prev => ({ ...prev, [userId]: res.data.suggestions }));
        setLoadingRec(prev => ({ ...prev, [userId]: false }));
      })
      .catch(() => setLoadingRec(prev => ({ ...prev, [userId]: false })));
  };

  const askAgent = () => {
    if (!question.trim()) return;
    setLoadingAnswer(true);
    setAnswer('');
    axios.post(`${API}/agent/query`, { question })
      .then(res => {
        setAnswer(res.data.answer);
        setLoadingAnswer(false);
      })
      .catch(() => {
        setAnswer('Unable to get answer. Please try again.');
        setLoadingAnswer(false);
      });
  };

  const segmentCounts = ['New', 'Active', 'At-Risk', 'Churned'].map(seg => ({
    name: seg,
    value: lifecycle.filter(u => u.lifecycleSegment === seg).length
  }));
  const COLORS = ['#6366f1', '#10b981', '#f59e0b', '#ef4444'];

  const SAMPLE_QUESTIONS = [
    'Which channel has the best ROI?',
    'Why are users churning?',
    'How can I improve retention?',
    'Which channel should I invest more in?',
    'What is the biggest growth opportunity?',
  ];

  if (loading) return <div className="loading">Loading GrowthLens...</div>;

  return (
    <div className="app">
      <header className="header">
        <div className="header-left">
          <h1>🔍 GrowthLens</h1>
          <p>AI-Powered Growth Analytics Copilot</p>
        </div>
        <nav className="nav">
          <button className={page === 'dashboard' ? 'active' : ''} onClick={() => setPage('dashboard')}>Dashboard</button>
          <button className={page === 'lifecycle' ? 'active' : ''} onClick={() => setPage('lifecycle')}>Lifecycle</button>
          <button className={page === 'atrisk' ? 'active' : ''} onClick={() => setPage('atrisk')}>
            At-Risk {atRisk.length > 0 && <span className="badge-count">{atRisk.length}</span>}
          </button>
          <button className={page === 'ask' ? 'active' : ''} onClick={() => setPage('ask')}>✨ Ask AI</button>
        </nav>
      </header>

      {/* Dashboard Page */}
      {page === 'dashboard' && (
        <div>
          <div className="cards">
            <div className="card">
              <div className="card-label">Total Channels</div>
              <div className="card-value">{channels.length}</div>
            </div>
            <div className="card">
              <div className="card-label">Total Users</div>
              <div className="card-value">{lifecycle.length}</div>
            </div>
            <div className="card">
              <div className="card-label">Total Revenue</div>
              <div className="card-value">
                ${channels.reduce((s, c) => s + parseFloat(c.totalRevenue), 0).toFixed(2)}
              </div>
            </div>
            <div className="card card-warning">
              <div className="card-label">At-Risk Users</div>
              <div className="card-value">{atRisk.length}</div>
            </div>
          </div>

          <div className="chart-container">
            <h2>Revenue by Acquisition Channel</h2>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={channels} margin={{ top: 10, right: 30, left: 0, bottom: 60 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="channelName" angle={-30} textAnchor="end" interval={0} />
                <YAxis />
                <Tooltip formatter={(v) => `$${v}`} />
                <Legend verticalAlign="top" />
                <Bar dataKey="totalRevenue" name="Revenue ($)" fill="#6366f1" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="chart-container">
            <h2>Retention Rate by Channel</h2>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={channels} margin={{ top: 10, right: 30, left: 0, bottom: 60 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="channelName" angle={-30} textAnchor="end" interval={0} />
                <YAxis unit="%" domain={[0, 100]} />
                <Tooltip formatter={(v) => `${v}%`} />
                <Legend verticalAlign="top" />
                <Bar dataKey="retentionRatePct" name="Retention Rate (%)" fill="#10b981" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="chart-container">
            <h2>Average LTV by Acquisition Channel</h2>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={ltv} margin={{ top: 10, right: 30, left: 0, bottom: 60 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="channelName" angle={-30} textAnchor="end" interval={0} />
                <YAxis />
                <Tooltip formatter={(v) => `$${parseFloat(v).toFixed(2)}`} />
                <Legend verticalAlign="top" />
                <Bar dataKey="avgLTV" name="Avg LTV ($)" fill="#f59e0b" radius={[4,4,0,0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="table-container">
            <h2>Channel Performance Details</h2>
            <table>
              <thead>
                <tr>
                  <th>Channel</th>
                  <th>Total Users</th>
                  <th>Active Users</th>
                  <th>Purchases</th>
                  <th>Revenue</th>
                  <th>Avg LTV</th>
                  <th>Retention Rate</th>
                </tr>
              </thead>
              <tbody>
                {channels.map((c, i) => {
                  const ltvData = ltv.find(l => l.channelName === c.channelName);
                  return (
                    <tr key={i}>
                      <td>{c.channelName}</td>
                      <td>{c.totalUsers}</td>
                      <td>{c.activeUsers}</td>
                      <td>{c.totalPurchases}</td>
                      <td>${parseFloat(c.totalRevenue).toFixed(2)}</td>
                      <td>${ltvData ? parseFloat(ltvData.avgLTV).toFixed(2) : '—'}</td>
                      <td>
                        <span className={`badge ${c.retentionRatePct >= 70 ? 'good' : c.retentionRatePct >= 40 ? 'medium' : 'bad'}`}>
                          {parseFloat(c.retentionRatePct).toFixed(1)}%
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Lifecycle Page */}
      {page === 'lifecycle' && (
        <div>
          <div className="charts-row">
            <div className="chart-container half">
              <h2>User Lifecycle Distribution</h2>
              <ResponsiveContainer width="100%" height={280}>
                <PieChart>
                  <Pie data={segmentCounts} cx="50%" cy="50%" outerRadius={100}
                    dataKey="value" label={({ name, value }) => `${name}: ${value}`}>
                    {segmentCounts.map((_, i) => <Cell key={i} fill={COLORS[i]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-container half">
              <h2>Segment Summary</h2>
              <div className="segment-cards">
                {segmentCounts.map((seg, i) => (
                  <div className="segment-card" key={i} style={{ borderColor: COLORS[i] }}>
                    <div className="segment-name" style={{ color: COLORS[i] }}>{seg.name}</div>
                    <div className="segment-count">{seg.value}</div>
                    <div className="segment-pct">
                      {lifecycle.length > 0 ? ((seg.value / lifecycle.length) * 100).toFixed(1) : 0}%
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="table-container">
            <h2>All Users</h2>
            <table>
              <thead>
                <tr>
                  <th>User</th>
                  <th>Channel</th>
                  <th>Segment</th>
                  <th>Sessions (30d)</th>
                  <th>Last Active</th>
                  <th>Total Spent</th>
                  <th>Reason</th>
                </tr>
              </thead>
              <tbody>
                {lifecycle.map((u, i) => (
                  <tr key={i}>
                    <td>{u.email}</td>
                    <td>{u.acquisitionChannel}</td>
                    <td>
                      <span className={`badge segment-${u.lifecycleSegment.toLowerCase().replace('-','')}`}>
                        {u.lifecycleSegment}
                      </span>
                    </td>
                    <td>{u.sessionsLast30Days}</td>
                    <td>{u.lastActiveDaysAgo === 9999 ? 'Never' : `${u.lastActiveDaysAgo}d ago`}</td>
                    <td>${u.totalSpent.toFixed(2)}</td>
                    <td>{u.reasonTag || '—'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* At-Risk Page */}
      {page === 'atrisk' && (
        <div>
          <div className="section-header">
            <h2>⚠️ At-Risk Users</h2>
            <p>These users have been inactive for 7-30 days. Act now to prevent churn.</p>
          </div>

          {atRisk.length === 0 ? (
            <div className="empty">No at-risk users right now 🎉</div>
          ) : (
            atRisk.map((u, i) => (
              <div className="risk-card" key={i}>
                <div className="risk-header">
                  <div>
                    <div className="risk-email">{u.email}</div>
                    <div className="risk-meta">
                      📣 {u.acquisitionChannel} &nbsp;·&nbsp;
                      🕐 Last active {u.lastActiveDaysAgo}d ago &nbsp;·&nbsp;
                      💰 ${u.totalSpent.toFixed(2)} spent
                      {u.reasonTag && <span className="reason-tag">⚠️ {u.reasonTag}</span>}
                    </div>
                  </div>
                  <button
                    className="btn-ai"
                    onClick={() => getRecommendation(u.userId)}
                    disabled={loadingRec[u.userId]}
                  >
                    {loadingRec[u.userId] ? 'Generating...' : '✨ Get AI Suggestions'}
                  </button>
                </div>

                {recommendations[u.userId] && (
                  <div className="suggestions">
                    <div className="suggestions-title">💡 AI Re-engagement Suggestions</div>
                    <div className="suggestions-text">
                      {recommendations[u.userId].split('\n').map((line, j) => (
                        line.trim() && <p key={j}>{line}</p>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      )}

      {/* Ask AI Page */}
      {page === 'ask' && (
        <div>
          <div className="section-header">
            <h2>✨ Ask AI</h2>
            <p>Ask any question about your growth data and get instant AI-powered insights.</p>
          </div>

          <div className="ask-container">
            <div className="ask-input-row">
              <input
                className="ask-input"
                type="text"
                placeholder="e.g. Which channel has the best ROI?"
                value={question}
                onChange={e => setQuestion(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && askAgent()}
              />
              <button className="btn-ai" onClick={askAgent} disabled={loadingAnswer}>
                {loadingAnswer ? 'Thinking...' : 'Ask →'}
              </button>
            </div>

            <div className="sample-questions">
              <p className="sample-label">Try asking:</p>
              <div className="sample-list">
                {SAMPLE_QUESTIONS.map((q, i) => (
                  <button key={i} className="sample-btn" onClick={() => setQuestion(q)}>
                    {q}
                  </button>
                ))}
              </div>
            </div>

            {loadingAnswer && (
              <div className="answer-loading">🤔 Analyzing your data...</div>
            )}

            {answer && (
              <div className="answer-container">
                <div className="answer-title">💡 AI Insight</div>
                <div className="answer-text">
                  {answer.split('\n').map((line, i) => (
                    line.trim() && <p key={i}>{line}</p>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
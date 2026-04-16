import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import './App.css';

function App() {
  const [channels, setChannels] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:8080/api/channels')
      .then(res => {
        setChannels(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="app">
      {/* Header */}
      <header className="header">
        <h1>🔍 GrowthLens</h1>
        <p>AI-Powered Growth Analytics Copilot</p>
      </header>

      {/* Summary Cards */}
      <div className="cards">
        <div className="card">
          <div className="card-label">Total Channels</div>
          <div className="card-value">{channels.length}</div>
        </div>
        <div className="card">
          <div className="card-label">Total Users</div>
          <div className="card-value">
            {channels.reduce((sum, c) => sum + c.totalUsers, 0)}
          </div>
        </div>
        <div className="card">
          <div className="card-label">Total Revenue</div>
          <div className="card-value">
            ${channels.reduce((sum, c) => sum + parseFloat(c.totalRevenue), 0).toFixed(2)}
          </div>
        </div>
        <div className="card">
          <div className="card-label">Avg Retention</div>
          <div className="card-value">
            {(channels.reduce((sum, c) => sum + parseFloat(c.retentionRatePct), 0) / channels.length).toFixed(1)}%
          </div>
        </div>
      </div>

      {/* Revenue by Channel Chart */}
      <div className="chart-container">
        <h2>Revenue by Acquisition Channel</h2>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={channels} margin={{ top: 10, right: 30, left: 0, bottom: 60 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="channelName" angle={-30} textAnchor="end" interval={0} />
            <YAxis />
            <Tooltip formatter={(value) => `$${value}`} />
            <Legend verticalAlign="top" />
            <Bar dataKey="totalRevenue" name="Revenue ($)" fill="#6366f1" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Retention Rate Chart */}
      <div className="chart-container">
        <h2>Retention Rate by Channel</h2>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={channels} margin={{ top: 10, right: 30, left: 0, bottom: 60 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="channelName" angle={-30} textAnchor="end" interval={0} />
            <YAxis unit="%" domain={[0, 100]} />
            <Tooltip formatter={(value) => `${value}%`} />
            <Legend verticalAlign="top" />
            <Bar dataKey="retentionRatePct" name="Retention Rate (%)" fill="#10b981" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* Data Table */}
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
              <th>Retention Rate</th>
            </tr>
          </thead>
          <tbody>
            {channels.map((c, i) => (
              <tr key={i}>
                <td>{c.channelName}</td>
                <td>{c.totalUsers}</td>
                <td>{c.activeUsers}</td>
                <td>{c.totalPurchases}</td>
                <td>${parseFloat(c.totalRevenue).toFixed(2)}</td>
                <td>
                  <span className={`badge ${c.retentionRatePct >= 70 ? 'good' : c.retentionRatePct >= 40 ? 'medium' : 'bad'}`}>
                    {parseFloat(c.retentionRatePct).toFixed(1)}%
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default App;
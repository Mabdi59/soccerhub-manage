import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Loading from '../components/Loading';
import { matchesAPI, teamsAPI, divisionsAPI, standingsAPI } from '../services/api';

const Overview = () => {
  const [stats, setStats] = useState({
    totalTeams: 0,
    totalMatches: 0,
    completedMatches: 0,
    upcomingMatches: 0,
  });
  const [recentMatches, setRecentMatches] = useState([]);
  const [topStandings, setTopStandings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [teamsRes, matchesRes, divisionsRes] = await Promise.all([
        teamsAPI.getAll(),
        matchesAPI.getAll(),
        divisionsAPI.getAll()
      ]);

      const matches = matchesRes.data;
      setStats({
        totalTeams: teamsRes.data.length,
        totalMatches: matches.length,
        completedMatches: matches.filter(m => m.status === 'COMPLETED').length,
        upcomingMatches: matches.filter(m => m.status === 'SCHEDULED').length,
      });

      setRecentMatches(matches.filter(m => m.status === 'COMPLETED').slice(0, 5));

      if (divisionsRes.data.length > 0) {
        const standingsRes = await standingsAPI.getByDivision(divisionsRes.data[0].id);
        setTopStandings(standingsRes.data.slice(0, 5));
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Layout title="Overview"><Loading /></Layout>;

  return (
    <Layout title="Overview">
      <div className="page-header">
        <h1 className="page-title">Dashboard Overview</h1>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Total Teams</div>
          <div className="stat-value">{stats.totalTeams}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Matches</div>
          <div className="stat-value">{stats.totalMatches}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Completed</div>
          <div className="stat-value">{stats.completedMatches}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Upcoming</div>
          <div className="stat-value">{stats.upcomingMatches}</div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Recent Results</h2>
        </div>
        {recentMatches.length === 0 ? (
          <p className="text-center">No recent matches</p>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Home Team</th>
                  <th>Score</th>
                  <th>Away Team</th>
                </tr>
              </thead>
              <tbody>
                {recentMatches.map((match) => (
                  <tr key={match.id}>
                    <td>{new Date(match.matchDate).toLocaleDateString()}</td>
                    <td>Team {match.homeTeamId}</td>
                    <td className="text-center">
                      <strong>{match.homeScore} - {match.awayScore}</strong>
                    </td>
                    <td>Team {match.awayTeamId}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {topStandings.length > 0 && (
        <div className="card">
          <div className="card-header">
            <h2 className="card-title">Top Standings</h2>
          </div>
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Pos</th>
                  <th>Team</th>
                  <th>P</th>
                  <th>W</th>
                  <th>D</th>
                  <th>L</th>
                  <th>Pts</th>
                </tr>
              </thead>
              <tbody>
                {topStandings.map((standing, index) => (
                  <tr key={standing.id}>
                    <td>{index + 1}</td>
                    <td>Team {standing.teamId}</td>
                    <td>{standing.played}</td>
                    <td>{standing.won}</td>
                    <td>{standing.drawn}</td>
                    <td>{standing.lost}</td>
                    <td><strong>{standing.points}</strong></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </Layout>
  );
};

export default Overview;

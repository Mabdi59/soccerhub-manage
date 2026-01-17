import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Loading from '../components/Loading';
import { standingsAPI, divisionsAPI, teamsAPI } from '../services/api';

const Standings = () => {
  const [standings, setStandings] = useState([]);
  const [divisions, setDivisions] = useState([]);
  const [teams, setTeams] = useState([]);
  const [selectedDivision, setSelectedDivision] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDivisions();
  }, []);

  useEffect(() => {
    if (selectedDivision) {
      fetchStandings();
    }
  }, [selectedDivision]);

  const fetchDivisions = async () => {
    try {
      const [divisionsRes, teamsRes] = await Promise.all([
        divisionsAPI.getAll(),
        teamsAPI.getAll()
      ]);
      setDivisions(divisionsRes.data);
      setTeams(teamsRes.data);
      
      if (divisionsRes.data.length > 0) {
        setSelectedDivision(divisionsRes.data[0].id);
      } else {
        setLoading(false);
      }
    } catch (error) {
      console.error('Error fetching divisions:', error);
      setLoading(false);
    }
  };

  const fetchStandings = async () => {
    setLoading(true);
    try {
      const response = await standingsAPI.getByDivision(selectedDivision);
      setStandings(response.data);
    } catch (error) {
      console.error('Error fetching standings:', error);
    } finally {
      setLoading(false);
    }
  };

  const getTeamName = (id) => teams.find(t => t.id === id)?.name || 'Unknown';

  return (
    <Layout title="Standings">
      <div className="page-header">
        <h1 className="page-title">League Standings</h1>
        {divisions.length > 0 && (
          <div className="form-group" style={{ marginBottom: 0, minWidth: '200px' }}>
            <select
              className="form-control"
              value={selectedDivision}
              onChange={(e) => setSelectedDivision(e.target.value)}
            >
              {divisions.map((division) => (
                <option key={division.id} value={division.id}>
                  {division.name}
                </option>
              ))}
            </select>
          </div>
        )}
      </div>

      {loading ? (
        <Loading />
      ) : (
        <div className="card">
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th style={{ width: '60px' }}>Pos</th>
                  <th>Team</th>
                  <th className="text-center">P</th>
                  <th className="text-center">W</th>
                  <th className="text-center">D</th>
                  <th className="text-center">L</th>
                  <th className="text-center">GF</th>
                  <th className="text-center">GA</th>
                  <th className="text-center">GD</th>
                  <th className="text-center"><strong>Pts</strong></th>
                </tr>
              </thead>
              <tbody>
                {standings.length === 0 ? (
                  <tr>
                    <td colSpan="10" className="text-center">No standings data available</td>
                  </tr>
                ) : (
                  standings.map((standing, index) => (
                    <tr key={standing.id} style={{ 
                      background: index < 3 ? 'rgba(16, 185, 129, 0.05)' : 'transparent' 
                    }}>
                      <td>
                        <strong>{index + 1}</strong>
                      </td>
                      <td>
                        <strong>{getTeamName(standing.teamId)}</strong>
                      </td>
                      <td className="text-center">{standing.played}</td>
                      <td className="text-center">{standing.won}</td>
                      <td className="text-center">{standing.drawn}</td>
                      <td className="text-center">{standing.lost}</td>
                      <td className="text-center">{standing.goalsFor}</td>
                      <td className="text-center">{standing.goalsAgainst}</td>
                      <td className="text-center" style={{ 
                        color: standing.goalDifference > 0 ? 'var(--secondary-color)' : 
                               standing.goalDifference < 0 ? 'var(--danger-color)' : 
                               'inherit' 
                      }}>
                        {standing.goalDifference > 0 ? '+' : ''}{standing.goalDifference}
                      </td>
                      <td className="text-center">
                        <strong style={{ fontSize: '16px', color: 'var(--primary-color)' }}>
                          {standing.points}
                        </strong>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {standings.length > 0 && (
            <div style={{ marginTop: '20px', padding: '16px', background: 'var(--gray-50)', borderRadius: '8px' }}>
              <p style={{ fontSize: '13px', color: 'var(--gray-600)', margin: 0 }}>
                <strong>Legend:</strong> P = Played, W = Won, D = Drawn, L = Lost, GF = Goals For, GA = Goals Against, GD = Goal Difference, Pts = Points
              </p>
              <p style={{ fontSize: '13px', color: 'var(--gray-600)', margin: '8px 0 0 0' }}>
                Points: Win = 3 pts, Draw = 1 pt, Loss = 0 pts
              </p>
            </div>
          )}
        </div>
      )}
    </Layout>
  );
};

export default Standings;

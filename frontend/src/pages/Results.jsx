import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Loading from '../components/Loading';
import { matchesAPI, teamsAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { formatDateTime } from '../utils/helpers';

const Results = () => {
  const { user, isReferee } = useAuth();
  const [matches, setMatches] = useState([]);
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [matchesRes, teamsRes] = await Promise.all([
        matchesAPI.getAll(),
        teamsAPI.getAll()
      ]);
      
      let matchesData = matchesRes.data;
      
      if (isReferee()) {
        matchesData = matchesData.filter(m => m.refereeId === user.id);
      }
      
      setMatches(matchesData);
      setTeams(teamsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleScoreUpdate = async (matchId, homeScore, awayScore) => {
    try {
      await matchesAPI.updateResult(matchId, { homeScore, awayScore });
      alert('Score updated successfully! Standings have been updated.');
      fetchData();
    } catch (error) {
      alert('Error updating score: ' + (error.response?.data?.message || error.message));
    }
  };

  const getTeamName = (id) => teams.find(t => t.id === id)?.name || 'Unknown';

  if (loading) return <Layout title={isReferee() ? "My Matches" : "Results"}><Loading /></Layout>;

  const canUpdateMatch = (match) => {
    if (isReferee()) {
      return match.status === 'SCHEDULED' || match.status === 'IN_PROGRESS';
    }
    return true;
  };

  return (
    <Layout title={isReferee() ? "My Matches" : "Results"}>
      <div className="page-header">
        <h1 className="page-title">{isReferee() ? "My Matches - Score Entry" : "Match Results"}</h1>
      </div>

      {matches.length === 0 ? (
        <div className="card">
          <div className="empty-state">
            <div className="empty-state-icon">⚽</div>
            <div className="empty-state-title">No matches found</div>
            <div className="empty-state-message">
              {isReferee() ? "You don't have any matches assigned yet." : "No matches available."}
            </div>
          </div>
        </div>
      ) : (
        matches.map((match) => (
          <MatchCard
            key={match.id}
            match={match}
            getTeamName={getTeamName}
            onUpdate={handleScoreUpdate}
            canUpdate={canUpdateMatch(match)}
          />
        ))
      )}
    </Layout>
  );
};

const MatchCard = ({ match, getTeamName, onUpdate, canUpdate }) => {
  const [homeScore, setHomeScore] = useState(match.homeScore ?? '');
  const [awayScore, setAwayScore] = useState(match.awayScore ?? '');
  const [isEditing, setIsEditing] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (homeScore === '' || awayScore === '') {
      alert('Please enter both scores');
      return;
    }
    await onUpdate(match.id, parseInt(homeScore), parseInt(awayScore));
    setIsEditing(false);
  };

  return (
    <div className="match-card">
      <div className="match-header">
        <div>
          <span style={{ fontSize: '13px', color: 'var(--gray-600)' }}>
            {formatDateTime(match.matchDate)}
          </span>
        </div>
        <span 
          className="badge"
          style={{ 
            background: match.status === 'COMPLETED' ? '#d1fae5' : '#dbeafe',
            color: match.status === 'COMPLETED' ? '#065f46' : '#1e40af'
          }}
        >
          {match.status}
        </span>
      </div>

      <div className="match-info">
        <div className="team">
          <div className="team-name">{getTeamName(match.homeTeamId)}</div>
          {match.status === 'COMPLETED' && (
            <div className="team-score">{match.homeScore}</div>
          )}
        </div>

        <div className="match-vs">VS</div>

        <div className="team">
          <div className="team-name">{getTeamName(match.awayTeamId)}</div>
          {match.status === 'COMPLETED' && (
            <div className="team-score">{match.awayScore}</div>
          )}
        </div>
      </div>

      {canUpdate && (
        <div style={{ marginTop: '20px', paddingTop: '20px', borderTop: '1px solid var(--gray-200)' }}>
          {!isEditing ? (
            <button 
              className="btn btn-primary btn-block"
              onClick={() => setIsEditing(true)}
            >
              {match.status === 'COMPLETED' ? 'Update Score' : 'Enter Score'}
            </button>
          ) : (
            <form onSubmit={handleSubmit}>
              <div className="score-input-group">
                <div>
                  <label style={{ fontSize: '13px', color: 'var(--gray-600)', marginBottom: '8px', display: 'block' }}>
                    Home Score
                  </label>
                  <input
                    type="number"
                    className="score-input"
                    value={homeScore}
                    onChange={(e) => setHomeScore(e.target.value)}
                    min="0"
                    required
                  />
                </div>

                <div>
                  <label style={{ fontSize: '13px', color: 'var(--gray-600)', marginBottom: '8px', display: 'block' }}>
                    Away Score
                  </label>
                  <input
                    type="number"
                    className="score-input"
                    value={awayScore}
                    onChange={(e) => setAwayScore(e.target.value)}
                    min="0"
                    required
                  />
                </div>
              </div>

              <div className="flex gap-2" style={{ marginTop: '16px' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setIsEditing(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-success" style={{ flex: 1 }}>
                  Submit Score
                </button>
              </div>
            </form>
          )}
        </div>
      )}
    </div>
  );
};

export default Results;

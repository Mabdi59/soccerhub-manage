import { useEffect, useMemo, useState } from 'react';
import Layout from '../components/Layout';
import Loading from '../components/Loading';
import Modal from '../components/Modal';
import { divisionsAPI, matchesAPI, standingsAPI, teamsAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { formatDateTime, getStatusColor } from '../utils/helpers';

const Playoffs = () => {
  const { isOrganizer } = useAuth();
  const [divisions, setDivisions] = useState([]);
  const [teams, setTeams] = useState([]);
  const [standings, setStandings] = useState([]);
  const [matches, setMatches] = useState([]);
  const [selectedDivisionId, setSelectedDivisionId] = useState('');
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);
  const [generateError, setGenerateError] = useState('');
  const [showGenerateModal, setShowGenerateModal] = useState(false);
  const [selectedMatch, setSelectedMatch] = useState(null);
  const [homeScore, setHomeScore] = useState('');
  const [awayScore, setAwayScore] = useState('');
  const [scoreError, setScoreError] = useState('');

  useEffect(() => {
    fetchBaseData();
  }, []);

  useEffect(() => {
    if (selectedDivisionId) {
      fetchDivisionData(selectedDivisionId);
    }
  }, [selectedDivisionId]);

  const fetchBaseData = async () => {
    try {
      const [divisionsRes, teamsRes] = await Promise.all([
        divisionsAPI.getAll(),
        teamsAPI.getAll()
      ]);
      setDivisions(divisionsRes.data);
      setTeams(teamsRes.data);
      if (divisionsRes.data.length > 0) {
        setSelectedDivisionId(divisionsRes.data[0].id);
      }
    } catch (error) {
      console.error('Error fetching playoff data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchDivisionData = async (divisionId) => {
    try {
      const [matchesRes, standingsRes] = await Promise.all([
        matchesAPI.getAll({ divisionId }),
        standingsAPI.getByDivision(divisionId)
      ]);
      setMatches(matchesRes.data);
      setStandings(standingsRes.data);
    } catch (error) {
      console.error('Error fetching division playoff data:', error);
    }
  };

  const handleGeneratePlayoffs = async () => {
    if (!selectedDivisionId) {
      setGenerateError('Please select a division.');
      return;
    }
    try {
      setGenerating(true);
      setGenerateError('');
      await divisionsAPI.generatePlayoffs(selectedDivisionId);
      setShowGenerateModal(false);
      fetchDivisionData(selectedDivisionId);
    } catch (error) {
      setGenerateError(error.response?.data?.message || 'Failed to generate playoffs.');
    } finally {
      setGenerating(false);
    }
  };

  const openScoreModal = (match) => {
    setSelectedMatch(match);
    setHomeScore(match.homeScore ?? '');
    setAwayScore(match.awayScore ?? '');
    setScoreError('');
  };

  const closeScoreModal = () => {
    setSelectedMatch(null);
    setHomeScore('');
    setAwayScore('');
    setScoreError('');
  };

  const handleScoreSubmit = async () => {
    if (homeScore === '' || awayScore === '') {
      setScoreError('Please enter both scores.');
      return;
    }
    try {
      await matchesAPI.updateResult(selectedMatch.id, {
        homeScore: parseInt(homeScore, 10),
        awayScore: parseInt(awayScore, 10),
      });
      closeScoreModal();
      fetchDivisionData(selectedDivisionId);
    } catch (error) {
      setScoreError(error.response?.data?.message || 'Failed to update score.');
    }
  };

  const teamLookup = useMemo(() => {
    const lookup = new Map();
    teams.forEach((team) => lookup.set(team.id, team.name));
    return lookup;
  }, [teams]);

  const playoffMatches = matches.filter((match) => match.playoffRound);
  const semifinals = playoffMatches.filter((match) => match.playoffRound === 'SEMIFINAL');
  const finalMatch = playoffMatches.find((match) => match.playoffRound === 'FINAL');
  const topSeeds = standings.slice(0, 4);

  const renderMatchCard = (match, placeholder) => {
    if (!match) {
      return (
        <div className="bracket-match placeholder">
          <div className="bracket-team">{placeholder}</div>
        </div>
      );
    }

    const winnerId = match.status === 'COMPLETED'
      ? (match.homeScore >= match.awayScore ? match.homeTeamId : match.awayTeamId)
      : null;
    const winnerName = winnerId ? teamLookup.get(winnerId) || 'TBD' : null;

    return (
      <div className="bracket-match clickable" onClick={() => openScoreModal(match)}>
        <div className="bracket-team">
          <span>{teamLookup.get(match.homeTeamId) || 'TBD'}</span>
          {match.homeScore != null && <strong>{match.homeScore}</strong>}
        </div>
        <div className="bracket-team">
          <span>{teamLookup.get(match.awayTeamId) || 'TBD'}</span>
          {match.awayScore != null && <strong>{match.awayScore}</strong>}
        </div>
        <div className="bracket-meta">
          <span>{formatDateTime(match.matchDate)}</span>
          <span
            className="badge"
            style={{ background: getStatusColor(match.status) + '33', color: getStatusColor(match.status) }}
          >
            {match.status}
          </span>
        </div>
        {winnerName && (
          <div className="winner-badge">
            Winner: {winnerName}
          </div>
        )}
      </div>
    );
  };

  if (loading) return <Layout title="Playoffs"><Loading /></Layout>;

  return (
    <Layout title="Playoffs">
      <div className="page-header">
        <h1 className="page-title">Playoffs</h1>
        <div className="page-actions">
          {isOrganizer() && (
            <button className="btn btn-secondary" onClick={() => setShowGenerateModal(true)}>
              Generate Playoffs
            </button>
          )}
        </div>
      </div>

      <div className="card">
        <div className="form-group">
          <label>Division</label>
          <select
            className="form-control"
            value={selectedDivisionId}
            onChange={(e) => setSelectedDivisionId(e.target.value)}
          >
            {divisions.map((division) => (
              <option key={division.id} value={division.id}>{division.name}</option>
            ))}
          </select>
        </div>
        <div className="seed-list">
          <h4>Top Seeds</h4>
          {topSeeds.length === 0 ? (
            <p className="text-muted">Standings not available yet.</p>
          ) : (
            <ol>
              {topSeeds.map((seed) => (
                <li key={seed.teamId}>{teamLookup.get(seed.teamId) || 'Unknown Team'}</li>
              ))}
            </ol>
          )}
        </div>
      </div>

      <div className="bracket-grid">
        <div className="bracket-column">
          <h3 className="bracket-title">Semifinals</h3>
          {semifinals.length === 0 ? (
            <>
              {renderMatchCard(null, 'Semifinal 1')}
              {renderMatchCard(null, 'Semifinal 2')}
            </>
          ) : (
            semifinals.map((match) => (
              <div key={match.id} className="bracket-slot">
                {renderMatchCard(match)}
              </div>
            ))
          )}
        </div>
        <div className="bracket-column">
          <h3 className="bracket-title">Final</h3>
          {renderMatchCard(finalMatch, 'Final')}
        </div>
      </div>

      <Modal
        isOpen={showGenerateModal}
        onClose={() => setShowGenerateModal(false)}
        title="Generate Playoffs"
        footer={
          <>
            <button className="btn btn-secondary" onClick={() => setShowGenerateModal(false)} disabled={generating}>
              Cancel
            </button>
            <button className="btn btn-primary" onClick={handleGeneratePlayoffs} disabled={generating}>
              {generating ? 'Generating...' : 'Generate'}
            </button>
          </>
        }
      >
        {generateError && <div className="alert alert-error">{generateError}</div>}
        <p>Generate the playoff bracket for the selected division using the top four teams.</p>
      </Modal>

      <Modal
        isOpen={!!selectedMatch}
        onClose={closeScoreModal}
        title="Enter Playoff Result"
        footer={
          <>
            <button className="btn btn-secondary" onClick={closeScoreModal}>
              Cancel
            </button>
            <button className="btn btn-primary" onClick={handleScoreSubmit}>
              Save Result
            </button>
          </>
        }
      >
        {scoreError && <div className="alert alert-error">{scoreError}</div>}
        {selectedMatch && (
          <>
            <p>
              {teamLookup.get(selectedMatch.homeTeamId) || 'TBD'} vs{' '}
              {teamLookup.get(selectedMatch.awayTeamId) || 'TBD'}
            </p>
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
          </>
        )}
      </Modal>
    </Layout>
  );
};

export default Playoffs;

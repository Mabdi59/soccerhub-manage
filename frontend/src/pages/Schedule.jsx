import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Modal from '../components/Modal';
import Loading from '../components/Loading';
import { matchesAPI, teamsAPI, divisionsAPI, venuesAPI } from '../services/api';
import { formatDateForInput, formatDateTime, getStatusColor } from '../utils/helpers';

const Schedule = () => {
  const [matches, setMatches] = useState([]);
  const [teams, setTeams] = useState([]);
  const [divisions, setDivisions] = useState([]);
  const [venues, setVenues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingMatch, setEditingMatch] = useState(null);
  const [formData, setFormData] = useState({
    divisionId: '',
    homeTeamId: '',
    awayTeamId: '',
    venueId: '',
    matchDate: '',
    status: 'SCHEDULED',
    refereeId: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [matchesRes, teamsRes, divisionsRes, venuesRes] = await Promise.all([
        matchesAPI.getAll(),
        teamsAPI.getAll(),
        divisionsAPI.getAll(),
        venuesAPI.getAll()
      ]);
      setMatches(matchesRes.data);
      setTeams(teamsRes.data);
      setDivisions(divisionsRes.data);
      setVenues(venuesRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = { ...formData, refereeId: formData.refereeId || null };
      if (editingMatch) {
        await matchesAPI.update(editingMatch.id, data);
      } else {
        await matchesAPI.create(data);
      }
      setShowModal(false);
      resetForm();
      fetchData();
    } catch (error) {
      alert('Error saving match: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleEdit = (match) => {
    setEditingMatch(match);
    setFormData({
      divisionId: match.divisionId,
      homeTeamId: match.homeTeamId,
      awayTeamId: match.awayTeamId,
      venueId: match.venueId,
      matchDate: formatDateForInput(match.matchDate),
      status: match.status,
      refereeId: match.refereeId || ''
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this match?')) return;
    try {
      await matchesAPI.delete(id);
      fetchData();
    } catch (error) {
      alert('Error deleting match: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleAdd = () => {
    setEditingMatch(null);
    resetForm();
    setShowModal(true);
  };

  const resetForm = () => {
    setFormData({
      divisionId: '',
      homeTeamId: '',
      awayTeamId: '',
      venueId: '',
      matchDate: '',
      status: 'SCHEDULED',
      refereeId: ''
    });
    setEditingMatch(null);
  };

  const getTeamName = (id) => teams.find(t => t.id === id)?.name || 'Unknown';
  const getDivisionName = (id) => divisions.find(d => d.id === id)?.name || 'Unknown';
  const getVenueName = (id) => venues.find(v => v.id === id)?.name || 'Unknown';

  if (loading) return <Layout title="Schedule"><Loading /></Layout>;

  return (
    <Layout title="Schedule">
      <div className="page-header">
        <h1 className="page-title">Match Schedule</h1>
        <button className="btn btn-primary" onClick={handleAdd}>+ Add Match</button>
      </div>

      <div className="card">
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>Date & Time</th>
                <th>Home Team</th>
                <th>Away Team</th>
                <th>Venue</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {matches.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center">No matches scheduled</td>
                </tr>
              ) : (
                matches.map((match) => (
                  <tr key={match.id}>
                    <td>{formatDateTime(match.matchDate)}</td>
                    <td>{getTeamName(match.homeTeamId)}</td>
                    <td>{getTeamName(match.awayTeamId)}</td>
                    <td>{getVenueName(match.venueId)}</td>
                    <td>
                      <span 
                        className="badge"
                        style={{ background: getStatusColor(match.status) + '33', color: getStatusColor(match.status) }}
                      >
                        {match.status}
                      </span>
                    </td>
                    <td>
                      <div className="table-actions">
                        <button className="icon-btn" onClick={() => handleEdit(match)}>✏️</button>
                        <button className="icon-btn delete" onClick={() => handleDelete(match.id)}>🗑️</button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <Modal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        title={editingMatch ? 'Edit Match' : 'Add Match'}
        footer={
          <>
            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
            <button className="btn btn-primary" onClick={handleSubmit}>Save</button>
          </>
        }
      >
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Division *</label>
            <select
              className="form-control"
              value={formData.divisionId}
              onChange={(e) => setFormData({ ...formData, divisionId: e.target.value })}
              required
            >
              <option value="">Select Division</option>
              {divisions.map((division) => (
                <option key={division.id} value={division.id}>{division.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Home Team *</label>
            <select
              className="form-control"
              value={formData.homeTeamId}
              onChange={(e) => setFormData({ ...formData, homeTeamId: e.target.value })}
              required
            >
              <option value="">Select Home Team</option>
              {teams.map((team) => (
                <option key={team.id} value={team.id}>{team.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Away Team *</label>
            <select
              className="form-control"
              value={formData.awayTeamId}
              onChange={(e) => setFormData({ ...formData, awayTeamId: e.target.value })}
              required
            >
              <option value="">Select Away Team</option>
              {teams.map((team) => (
                <option key={team.id} value={team.id}>{team.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Venue *</label>
            <select
              className="form-control"
              value={formData.venueId}
              onChange={(e) => setFormData({ ...formData, venueId: e.target.value })}
              required
            >
              <option value="">Select Venue</option>
              {venues.map((venue) => (
                <option key={venue.id} value={venue.id}>{venue.name}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Match Date & Time *</label>
            <input
              type="datetime-local"
              className="form-control"
              value={formData.matchDate}
              onChange={(e) => setFormData({ ...formData, matchDate: e.target.value })}
              required
            />
          </div>

          <div className="form-group">
            <label>Status *</label>
            <select
              className="form-control"
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              required
            >
              <option value="SCHEDULED">Scheduled</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
              <option value="POSTPONED">Postponed</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>

          <div className="form-group">
            <label>Referee ID (Optional)</label>
            <input
              type="number"
              className="form-control"
              value={formData.refereeId}
              onChange={(e) => setFormData({ ...formData, refereeId: e.target.value })}
              placeholder="Enter referee user ID"
            />
          </div>
        </form>
      </Modal>
    </Layout>
  );
};

export default Schedule;

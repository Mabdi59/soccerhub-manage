import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Modal from '../components/Modal';
import Loading from '../components/Loading';
import { teamsAPI, divisionsAPI } from '../services/api';

const Teams = () => {
  const [teams, setTeams] = useState([]);
  const [divisions, setDivisions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingTeam, setEditingTeam] = useState(null);
  const [formData, setFormData] = useState({ name: '', divisionId: '', logo: '' });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [teamsRes, divisionsRes] = await Promise.all([
        teamsAPI.getAll(),
        divisionsAPI.getAll()
      ]);
      setTeams(teamsRes.data);
      setDivisions(divisionsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingTeam) {
        await teamsAPI.update(editingTeam.id, formData);
      } else {
        await teamsAPI.create(formData);
      }
      setShowModal(false);
      setFormData({ name: '', divisionId: '', logo: '' });
      setEditingTeam(null);
      fetchData();
    } catch (error) {
      alert('Error saving team: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleEdit = (team) => {
    setEditingTeam(team);
    setFormData({ name: team.name, divisionId: team.divisionId, logo: team.logo || '' });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this team?')) return;
    try {
      await teamsAPI.delete(id);
      fetchData();
    } catch (error) {
      alert('Error deleting team: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleAdd = () => {
    setEditingTeam(null);
    setFormData({ name: '', divisionId: '', logo: '' });
    setShowModal(true);
  };

  const getDivisionName = (divisionId) => {
    return divisions.find(d => d.id === divisionId)?.name || 'N/A';
  };

  if (loading) return <Layout title="Teams"><Loading /></Layout>;

  return (
    <Layout title="Teams">
      <div className="page-header">
        <h1 className="page-title">Teams</h1>
        <button className="btn btn-primary" onClick={handleAdd}>+ Add Team</button>
      </div>

      <div className="card">
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Division</th>
                <th>Logo</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {teams.length === 0 ? (
                <tr>
                  <td colSpan="5" className="text-center">No teams found</td>
                </tr>
              ) : (
                teams.map((team) => (
                  <tr key={team.id}>
                    <td>{team.id}</td>
                    <td>{team.name}</td>
                    <td>{getDivisionName(team.divisionId)}</td>
                    <td>{team.logo ? '🖼️' : '-'}</td>
                    <td>
                      <div className="table-actions">
                        <button className="icon-btn" onClick={() => handleEdit(team)}>✏️</button>
                        <button className="icon-btn delete" onClick={() => handleDelete(team.id)}>🗑️</button>
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
        title={editingTeam ? 'Edit Team' : 'Add Team'}
        footer={
          <>
            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
            <button className="btn btn-primary" onClick={handleSubmit}>Save</button>
          </>
        }
      >
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Team Name *</label>
            <input
              type="text"
              className="form-control"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              required
            />
          </div>

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
                <option key={division.id} value={division.id}>
                  {division.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Logo URL</label>
            <input
              type="text"
              className="form-control"
              value={formData.logo}
              onChange={(e) => setFormData({ ...formData, logo: e.target.value })}
              placeholder="https://example.com/logo.png"
            />
          </div>
        </form>
      </Modal>
    </Layout>
  );
};

export default Teams;

import { useState } from 'react';
import Layout from '../components/Layout';
import { useAuth } from '../context/AuthContext';

const Settings = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    username: user?.username || '',
    email: user?.email || '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.newPassword && formData.newPassword !== formData.confirmPassword) {
      alert('New passwords do not match');
      return;
    }
    alert('Settings update functionality would be implemented here');
  };

  return (
    <Layout title="Settings">
      <div className="page-header">
        <h1 className="page-title">Settings</h1>
      </div>

      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Profile Information</h2>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              className="form-control"
              value={formData.username}
              onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              disabled
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              disabled
            />
          </div>

          <div className="form-group">
            <label>Role</label>
            <input
              type="text"
              className="form-control"
              value={user?.role || ''}
              disabled
            />
          </div>
        </form>
      </div>

      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Change Password</h2>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Current Password</label>
            <input
              type="password"
              className="form-control"
              value={formData.currentPassword}
              onChange={(e) => setFormData({ ...formData, currentPassword: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label>New Password</label>
            <input
              type="password"
              className="form-control"
              value={formData.newPassword}
              onChange={(e) => setFormData({ ...formData, newPassword: e.target.value })}
            />
          </div>

          <div className="form-group">
            <label>Confirm New Password</label>
            <input
              type="password"
              className="form-control"
              value={formData.confirmPassword}
              onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled>
            Update Password (Coming Soon)
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default Settings;

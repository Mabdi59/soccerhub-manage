import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Sidebar = () => {
  const location = useLocation();
  const { user, logout, isReferee } = useAuth();

  const isActive = (path) => location.pathname === path ? 'active' : '';

  const menuItems = isReferee() ? [
    { path: '/results', label: 'My Matches', icon: '⚽' },
  ] : [
    { path: '/', label: 'Overview', icon: '📊' },
    { path: '/teams', label: 'Teams', icon: '👥' },
    { path: '/schedule', label: 'Schedule', icon: '📅' },
    { path: '/results', label: 'Results', icon: '⚽' },
    { path: '/standings', label: 'Standings', icon: '🏆' },
    { path: '/settings', label: 'Settings', icon: '⚙️' },
  ];

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>⚽ SoccerHub</h2>
        <p>Tournament Manager</p>
      </div>
      
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`nav-item ${isActive(item.path)}`}
          >
            <span className="nav-icon">{item.icon}</span>
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>

      <div className="sidebar-footer">
        <div className="user-info" style={{ background: 'var(--gray-800)', marginBottom: '12px' }}>
          <div className="user-avatar">
            {user?.username?.[0]?.toUpperCase() || 'U'}
          </div>
          <div className="user-details">
            <div className="user-name">{user?.username}</div>
            <div className="user-role">{user?.role}</div>
          </div>
        </div>
        <button onClick={logout} className="btn btn-danger btn-block btn-sm">
          Logout
        </button>
      </div>
    </div>
  );
};

export default Sidebar;

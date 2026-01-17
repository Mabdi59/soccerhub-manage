import { useAuth } from '../context/AuthContext';

const Header = ({ title }) => {
  const { user } = useAuth();

  return (
    <header className="header">
      <h1 className="header-title">{title}</h1>
      <div className="header-actions">
        <div className="user-info">
          <div className="user-avatar">
            {user?.username?.[0]?.toUpperCase() || 'U'}
          </div>
          <div className="user-details">
            <div className="user-name">{user?.username}</div>
            <div className="user-role">{user?.role}</div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;

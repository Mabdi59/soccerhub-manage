import Sidebar from './Sidebar';
import Header from './Header';

const Layout = ({ title, children }) => {
  return (
    <div className="app">
      <Sidebar />
      <div className="main-content">
        <Header title={title} />
        <main className="content">{children}</main>
      </div>
    </div>
  );
};

export default Layout;

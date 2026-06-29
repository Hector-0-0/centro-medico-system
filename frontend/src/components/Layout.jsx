import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import ErrorBoundary from './ErrorBoundary';
import { useAuth } from '../context/AuthContext';
import { getNombre } from '../services/authService';
import { labelForPath } from './menu';
import './Layout.css';

/** Marco principal: sidebar + topbar + área de contenido (réplica MainFrame). */
export default function Layout() {
  const { rol } = useAuth();
  const { pathname } = useLocation();

  return (
    <div className="layout">
      <Sidebar />
      <div className="main">
        <header className="topbar">
          <div className="topbar__title">{labelForPath(pathname)}</div>
          <div className="topbar__right">
            <span className="topbar__user">{getNombre()}</span>
            <span className="topbar__badge">{rol}</span>
          </div>
        </header>
        <main className="content">
          <ErrorBoundary key={pathname}>
            <Outlet />
          </ErrorBoundary>
        </main>
      </div>
    </div>
  );
}

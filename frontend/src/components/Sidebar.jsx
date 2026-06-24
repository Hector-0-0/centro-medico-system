import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getNombre } from '../services/authService';
import { MENU } from './menu';

/** Barra lateral guinda con el menú propio del rol (réplica del desktop). */
export default function Sidebar() {
  const { rol, handleLogout } = useAuth();
  const items = MENU[rol] || [];

  return (
    <aside className="sidebar">
      <img className="sidebar__logo" src="/images/unii2.png" alt="UNI" />
      <div className="sidebar__brand">Centro Médico UNI</div>
      <div className="sidebar__user">{getNombre()}</div>
      <hr className="sidebar__sep" />

      <nav className="sidebar__nav">
        {items.map((it) => (
          <NavLink
            key={it.path}
            to={it.path}
            className={({ isActive }) =>
              'sidebar__link' + (isActive ? ' is-active' : '')
            }
          >
            {it.label}
          </NavLink>
        ))}
      </nav>

      <button className="sidebar__logout" onClick={handleLogout}>
        Cerrar Sesión
      </button>
    </aside>
  );
}

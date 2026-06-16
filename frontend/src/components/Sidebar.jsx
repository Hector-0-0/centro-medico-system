import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getNombre } from '../services/authService';

// Paleta institucional UNI (idéntica al proyecto desktop / Sidebar.java)
const GUINDA      = '#8B1414'; // fondo del sidebar
const ACTIVO_BG   = '#FAEDCF'; // botón activo
const HOVER_BG    = '#FFF1D3'; // botón hover
const TEXTO_CLARO = '#F9F5F0';

const s = {
  sidebar: {
    width: 280, minHeight: '100vh', background: GUINDA,
    display: 'flex', flexDirection: 'column',
    position: 'fixed', top: 0, left: 0,
  },
  header: { padding: '24px 20px 16px', textAlign: 'center' },
  logo: { width: 72, height: 'auto', marginBottom: 12 },
  titulo: { fontSize: 18, fontWeight: 700, color: '#fff' },
  nombre: { fontSize: 14, fontStyle: 'italic', color: '#fff', marginTop: 6 },
  separator: {
    height: 1, background: 'rgba(255,255,255,0.25)',
    margin: '12px 20px 8px',
  },
  nav: { flex: 1, padding: '8px 14px', overflowY: 'auto' },
  footer: { padding: '16px 18px 18px' },
};

// Menús por rol — mismas secciones que el Sidebar del desktop
const MENU_ADMIN = [
  { to: '/dashboard',    icono: '🏠', label: 'Panel principal' },
  { to: '/pacientes',    icono: '👥', label: 'Pacientes' },
  { to: '/medicos',      icono: '👨‍⚕️', label: 'Médicos' },
  { to: '/citas',        icono: '📅', label: 'Todas las Citas' },
  { to: '/historial',    icono: '📋', label: 'Historial médico' },
  { to: '/medicamentos', icono: '💊', label: 'Medicamentos' },
  { to: '/recetas',      icono: '🧾', label: 'Recetas' },
  { to: '/perfil',       icono: '👤', label: 'Mi Perfil' },
];

const MENU_MEDICO = [
  { to: '/dashboard',      icono: '🏠', label: 'Mi agenda' },
  { to: '/citas',          icono: '📅', label: 'Mis Citas' },
  { to: '/disponibilidad', icono: '🕐', label: 'Mi Disponibilidad' },
  { to: '/recetas',        icono: '🧾', label: 'Recetas' },
  { to: '/historial',      icono: '📋', label: 'Historial médico' },
  { to: '/pacientes',      icono: '👥', label: 'Pacientes' },
  { to: '/perfil',         icono: '👤', label: 'Mi Perfil' },
];

const MENU_RECEP = [
  { to: '/dashboard',    icono: '🏠', label: 'Panel principal' },
  { to: '/horarios',     icono: '🗓️', label: 'Horarios' },
  { to: '/pacientes',    icono: '👥', label: 'Pacientes' },
  { to: '/citas',        icono: '📅', label: 'Citas' },
  { to: '/medicamentos', icono: '💊', label: 'Gestión Stock' },
  { to: '/recetas',      icono: '🧾', label: 'Recetas (Farmacia)' },
  { to: '/perfil',       icono: '👤', label: 'Mi Perfil' },
];

const MENU_PACIENTE = [
  { to: '/dashboard',    icono: '🏠', label: 'Mi panel' },
  { to: '/horarios',     icono: '🗓️', label: 'Horarios' },
  { to: '/mis-citas',    icono: '📅', label: 'Mis Citas' },
  { to: '/mi-historial', icono: '📋', label: 'Mi historial' },
  { to: '/perfil',       icono: '👤', label: 'Mi Perfil' },
];

const MENU_POR_ROL = {
  ADMIN:         MENU_ADMIN,
  MEDICO:        MENU_MEDICO,
  RECEPCIONISTA: MENU_RECEP,
  PACIENTE:      MENU_PACIENTE,
};

export default function Sidebar() {
  const { rol, handleLogout } = useAuth();
  const menu = MENU_POR_ROL[rol] || MENU_ADMIN;
  const nombreCompleto = getNombre();

  return (
    <aside style={s.sidebar}>
      {/* Estilos de los botones del menú (hover / activo) — igual al desktop */}
      <style>{`
        .uni-nav a {
          display: flex; align-items: center; gap: 12px;
          padding: 0 16px; height: 42px; margin-bottom: 5px;
          font-size: 15px; color: ${TEXTO_CLARO}; text-decoration: none;
          border-radius: 8px; transition: background .12s, color .12s;
        }
        .uni-nav a:hover   { background: ${HOVER_BG};  color: ${GUINDA}; }
        .uni-nav a.active  { background: ${ACTIVO_BG}; color: ${GUINDA}; font-weight: 700; }
        .uni-salir {
          width: 100%; height: 40px; cursor: pointer; font-size: 14px; font-weight: 600;
          color: #fff; background: transparent; border: 1.5px solid rgba(255,255,255,0.55);
          border-radius: 8px; transition: background .12s, color .12s;
        }
        .uni-salir:hover { background: ${HOVER_BG}; color: ${GUINDA}; border-color: ${HOVER_BG}; }
      `}</style>

      <div style={s.header}>
        <img src="/images/unii2.png" alt="UNI" style={s.logo}
             onError={e => { e.currentTarget.src = '/images/logo-uni.png'; }} />
        <div style={s.titulo}>Centro Médico UNI</div>
        <div style={s.nombre}>{nombreCompleto}</div>
      </div>
      <div style={s.separator} />

      <nav className="uni-nav" style={s.nav}>
        {menu.map(({ to, icono, label }) => (
          <NavLink key={to} to={to}>
            <span style={{ width: 20, textAlign: 'center' }}>{icono}</span>
            {label}
          </NavLink>
        ))}
      </nav>

      <div style={s.footer}>
        <button className="uni-salir" onClick={handleLogout}>Cerrar Sesión</button>
      </div>
    </aside>
  );
}

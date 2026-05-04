import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getNombre } from '../services/authService';

const s = {
  sidebar: {
    width: 240, minHeight: '100vh', background: '#fff',
    borderRight: '1px solid #e2e8f0', display: 'flex',
    flexDirection: 'column', position: 'fixed', top: 0, left: 0,
  },
  header: { padding: '20px 20px 16px', borderBottom: '1px solid #e2e8f0' },
  titulo: { fontSize: 14, fontWeight: 700, color: '#1a73e8', lineHeight: 1.3 },
  subtitulo: { fontSize: 11, color: '#94a3b8', marginTop: 2 },
  nav: { flex: 1, padding: '10px 0', overflowY: 'auto' },
  seccion: { padding: '10px 20px 4px', fontSize: 10, fontWeight: 700, color: '#94a3b8', textTransform: 'uppercase', letterSpacing: 1 },
  footer: { padding: '14px 20px', borderTop: '1px solid #e2e8f0' },
  usuario: { fontSize: 13, color: '#374151', fontWeight: 600, marginBottom: 2 },
  rol: { fontSize: 11, color: '#94a3b8', textTransform: 'uppercase', letterSpacing: 0.8, marginBottom: 10 },
  btnSalir: { width: '100%', padding: '8px 0', background: '#fee2e2', color: '#dc2626', border: 'none', borderRadius: 6, fontSize: 13, cursor: 'pointer', fontWeight: 600 },
};

const linkStyle = ({ isActive }) => ({
  display: 'flex', alignItems: 'center', gap: 10,
  padding: '9px 20px', fontSize: 14,
  color: isActive ? '#1a73e8' : '#475569',
  textDecoration: 'none',
  background: isActive ? '#eff6ff' : 'transparent',
  borderRight: isActive ? '3px solid #1a73e8' : '3px solid transparent',
  fontWeight: isActive ? 600 : 400,
  transition: 'all 0.15s',
});

// Menú según rol
const MENU_ADMIN = [
  { to: '/dashboard',    icono: '🏠', label: 'Panel principal' },
  { to: '/pacientes',    icono: '👥', label: 'Pacientes' },
  { to: '/medicos',      icono: '👨‍⚕️', label: 'Médicos' },
  { to: '/citas',        icono: '📅', label: 'Citas' },
  { to: '/historial',    icono: '📋', label: 'Historial médico' },
  { to: '/medicamentos', icono: '💊', label: 'Medicamentos' },
];

const MENU_MEDICO = [
  { to: '/dashboard',  icono: '🏠', label: 'Mi agenda' },
  { to: '/citas',      icono: '📅', label: 'Citas del día' },
  { to: '/historial',  icono: '📋', label: 'Historial médico' },
  { to: '/pacientes',  icono: '👥', label: 'Pacientes' },
];

const MENU_RECEP = [
  { to: '/dashboard',    icono: '🏠', label: 'Panel principal' },
  { to: '/pacientes',    icono: '👥', label: 'Pacientes' },
  { to: '/citas',        icono: '📅', label: 'Citas' },
  { to: '/medicamentos', icono: '💊', label: 'Medicamentos' },
];

const MENU_PACIENTE = [
  { to: '/dashboard',  icono: '🏠', label: 'Mi panel' },
  { to: '/mis-citas',  icono: '📅', label: 'Mis citas' },
  { to: '/mi-historial', icono: '📋', label: 'Mi historial' },
];

const MENU_POR_ROL = {
  ADMIN:          MENU_ADMIN,
  MEDICO:         MENU_MEDICO,
  RECEPCIONISTA:  MENU_RECEP,
  PACIENTE:       MENU_PACIENTE,
};

const LABEL_ROL = {
  ADMIN: 'Administrador',
  MEDICO: 'Médico',
  RECEPCIONISTA: 'Recepcionista',
  PACIENTE: 'Paciente / Estudiante',
};

export default function Sidebar() {
  const { rol, handleLogout } = useAuth();
  const menu = MENU_POR_ROL[rol] || MENU_ADMIN;
  const nombreCompleto = getNombre();

  return (
    <aside style={s.sidebar}>
      <div style={s.header}>
        <div style={{ fontSize: 20, marginBottom: 6 }}>🏥</div>
        <div style={s.titulo}>Centro Médico UNI</div>
        <div style={s.subtitulo}>Sistema de Gestión</div>
      </div>

      <nav style={s.nav}>
        {menu.map(({ to, icono, label }) => (
          <NavLink key={to} to={to} style={linkStyle}>
            <span style={{ width: 20, textAlign: 'center' }}>{icono}</span>
            {label}
          </NavLink>
        ))}
      </nav>

      <div style={s.footer}>
        <div style={s.usuario}>{nombreCompleto}</div>
        <div style={s.rol}>{LABEL_ROL[rol] || rol}</div>
        <button style={s.btnSalir} onClick={handleLogout}>
          Cerrar sesión
        </button>
      </div>
    </aside>
  );
}
import Sidebar from './Sidebar';
import { useAuth } from '../context/AuthContext';
import { getNombre } from '../services/authService';

// Paleta institucional UNI (idéntica al desktop / MainFrame.java)
const CREMA  = '#F9F5F0'; // fondo del área central
const BORDE  = '#E8DDD8'; // borde inferior del topbar
const GUINDA = '#711610'; // badge de rol

const LABEL_ROL = {
  ADMIN: 'ADMINISTRADOR',
  MEDICO: 'MÉDICO',
  RECEPCIONISTA: 'RECEPCIONISTA',
  PACIENTE: 'PACIENTE',
};

const s = {
  wrapper: { display: 'flex', minHeight: '100vh' },
  body: { marginLeft: 280, flex: 1, minHeight: '100vh', background: CREMA, display: 'flex', flexDirection: 'column' },
  topbar: {
    height: 54, background: '#fff', borderBottom: `1px solid ${BORDE}`,
    display: 'flex', alignItems: 'center', padding: '0 24px', gap: 12,
    position: 'sticky', top: 0, zIndex: 50,
  },
  titulo: { flex: 1, fontSize: 16, fontWeight: 700, color: '#1e293b' },
  nombre: { fontSize: 12, fontWeight: 700, color: '#1e293b' },
  badge: {
    fontSize: 11, fontWeight: 700, color: '#fff', background: GUINDA,
    padding: '3px 10px', borderRadius: 4, letterSpacing: 0.4,
  },
  main: { flex: 1, padding: 28 },
};

/** Layout base: sidebar guinda + topbar blanco + área cream (igual al MainFrame del desktop). */
export default function Layout({ children, titulo = 'Inicio' }) {
  const { rol } = useAuth();
  const nombre = getNombre();

  return (
    <div style={s.wrapper}>
      <Sidebar />
      <div style={s.body}>
        <header style={s.topbar}>
          <div style={s.titulo}>{titulo}</div>
          <span style={s.nombre}>{nombre}</span>
          <span style={s.badge}>{LABEL_ROL[rol] || rol}</span>
        </header>
        <main style={s.main}>{children}</main>
      </div>
    </div>
  );
}

import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useEffect } from 'react';

const s = {
  page: { minHeight: '100vh', background: '#fff', display: 'flex', flexDirection: 'column' },
  nav: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    padding: '16px 48px', borderBottom: '1px solid #e2e8f0',
    background: '#fff', position: 'sticky', top: 0, zIndex: 100,
  },
  logo: { display: 'flex', alignItems: 'center', gap: 10 },
  logoIcon: { fontSize: 28 },
  logoText: { fontSize: 18, fontWeight: 700, color: '#1e293b' },
  logoSub: { fontSize: 12, color: '#64748b' },
  hero: {
    flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center',
    justifyContent: 'center', padding: '80px 24px 60px',
    background: 'linear-gradient(160deg, #eff6ff 0%, #fff 60%)',
    textAlign: 'center',
  },
  badge: {
    display: 'inline-block', padding: '6px 16px', background: '#dbeafe',
    color: '#1d4ed8', borderRadius: 20, fontSize: 13, fontWeight: 600,
    marginBottom: 24,
  },
  heroTit: { fontSize: 42, fontWeight: 800, color: '#0f172a', lineHeight: 1.2, maxWidth: 600, marginBottom: 16 },
  heroSub: { fontSize: 17, color: '#64748b', maxWidth: 500, lineHeight: 1.6, marginBottom: 48 },
  cards: {
    display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 20,
    maxWidth: 820, width: '100%',
  },
  card: (color) => ({
    background: '#fff', borderRadius: 16, padding: '32px 28px',
    boxShadow: '0 4px 20px rgba(0,0,0,0.08)', border: `2px solid ${color}20`,
    cursor: 'pointer', transition: 'all 0.2s', textAlign: 'center',
  }),
  cardIcon: { fontSize: 40, marginBottom: 16, display: 'block' },
  cardTit: (color) => ({ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 8, borderBottom: `3px solid ${color}`, paddingBottom: 8, display: 'inline-block' }),
  cardDesc: { fontSize: 14, color: '#64748b', lineHeight: 1.5, marginBottom: 20 },
  cardBtn: (color) => ({
    display: 'block', width: '100%', padding: '11px 0',
    background: color, color: '#fff', border: 'none',
    borderRadius: 8, fontSize: 14, fontWeight: 700, cursor: 'pointer',
  }),
  info: {
    background: '#f8fafc', borderTop: '1px solid #e2e8f0',
    padding: '48px 48px', display: 'grid',
    gridTemplateColumns: 'repeat(4, 1fr)', gap: 24, textAlign: 'center',
  },
  infoNum: { fontSize: 32, fontWeight: 800, color: '#1a73e8', marginBottom: 6 },
  infoLabel: { fontSize: 13, color: '#64748b' },
  footer: {
    padding: '20px 48px', borderTop: '1px solid #e2e8f0',
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    fontSize: 13, color: '#94a3b8',
  },
};

const PORTALES = [
  {
    icono: '🎓', titulo: 'Estudiante / Paciente',
    desc: 'Agenda tu cita médica, consulta tu historial y accede a tus recetas.',
    color: '#1a73e8', ruta: '/login/paciente',
    items: ['Agendar citas', 'Ver historial médico', 'Descargar recetas'],
  },
  {
    icono: '👨‍⚕️', titulo: 'Médico',
    desc: 'Gestiona tu agenda, atiende consultas y registra historiales médicos.',
    color: '#1d9e75', ruta: '/login/medico',
    items: ['Ver agenda del día', 'Registrar consultas', 'Emitir recetas'],
  },
  {
    icono: '🏥', titulo: 'Administración',
    desc: 'Administra pacientes, médicos, citas y el inventario de farmacia.',
    color: '#7c3aed', ruta: '/login/admin',
    items: ['Gestión de usuarios', 'Control de farmacia', 'Reportes'],
  },
];

export default function Portal() {
  const navigate = useNavigate();
  const { loggedIn, rol } = useAuth();

  // Si ya está logueado, redirigir al dashboard correspondiente
  useEffect(() => {
    if (loggedIn) navigate('/dashboard');
  }, [loggedIn, navigate]);

  return (
    <div style={s.page}>
      {/* Navbar */}
      <nav style={s.nav}>
        <div style={s.logo}>
          <span style={s.logoIcon}>🏥</span>
          <div>
            <div style={s.logoText}>Centro Médico UNI</div>
            <div style={s.logoSub}>Universidad Nacional de Ingeniería</div>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
          <span style={{ fontSize: 13, color: '#64748b' }}>¿Ya tienes cuenta?</span>
          <button
            onClick={() => navigate('/login')}
            style={{ padding: '9px 20px', background: '#1a73e8', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' }}
          >
            Iniciar sesión
          </button>
        </div>
      </nav>

      {/* Hero */}
      <section style={s.hero}>
        <span style={s.badge}>🏥 Sistema de Gestión Médica · UNI</span>
        <h1 style={s.heroTit}>Tu salud, nuestra prioridad</h1>
        <p style={s.heroSub}>
          Accede al sistema de atención médica de la Universidad Nacional de Ingeniería.
          Agenda citas, consulta tu historial y gestiona tu salud en un solo lugar.
        </p>

        <div style={s.cards}>
          {PORTALES.map(p => (
            <div
              key={p.ruta}
              style={s.card(p.color)}
              onMouseEnter={e => { e.currentTarget.style.transform = 'translateY(-4px)'; e.currentTarget.style.boxShadow = '0 8px 30px rgba(0,0,0,0.12)'; }}
              onMouseLeave={e => { e.currentTarget.style.transform = 'translateY(0)'; e.currentTarget.style.boxShadow = '0 4px 20px rgba(0,0,0,0.08)'; }}
            >
              <span style={s.cardIcon}>{p.icono}</span>
              <div style={{ marginBottom: 12 }}>
                <span style={s.cardTit(p.color)}>{p.titulo}</span>
              </div>
              <p style={s.cardDesc}>{p.desc}</p>
              <ul style={{ textAlign: 'left', marginBottom: 20, paddingLeft: 20 }}>
                {p.items.map(item => (
                  <li key={item} style={{ fontSize: 13, color: '#475569', marginBottom: 4 }}>✓ {item}</li>
                ))}
              </ul>
              <button style={s.cardBtn(p.color)} onClick={() => navigate(p.ruta)}>
                Ingresar como {p.titulo.split(' ')[0]}
              </button>
            </div>
          ))}
        </div>
      </section>

      {/* Stats */}
      <div style={s.info}>
        {[
          { num: '4', label: 'Especialidades médicas' },
          { num: '100%', label: 'Servicio universitario' },
          { num: '24/7', label: 'Historial disponible' },
          { num: 'UNI', label: 'Universidad Nacional de Ingeniería' },
        ].map(({ num, label }) => (
          <div key={label}>
            <div style={s.infoNum}>{num}</div>
            <div style={s.infoLabel}>{label}</div>
          </div>
        ))}
      </div>

      <footer style={s.footer}>
        <span>© 2026 Centro Médico UNI · Universidad Nacional de Ingeniería</span>
        <span>Lima, Perú</span>
      </footer>
    </div>
  );
}
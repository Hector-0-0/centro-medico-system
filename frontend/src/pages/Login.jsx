import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { useAuth } from '../context/AuthContext';

// Color institucional guinda de la UNI (igual que el proyecto desktop)
const GUINDA = '#711610';

const s = {
  page: {
    minHeight: '100vh', display: 'flex', flexDirection: 'column',
    backgroundImage: 'url(/images/banner-uni.jpeg)',
    backgroundSize: 'cover', backgroundPosition: 'center',
  },
  overlay: {
    flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center',
    background: 'rgba(15, 23, 42, 0.45)', padding: '40px 20px',
  },
  card: {
    background: '#fff', borderRadius: 24, padding: '40px 36px',
    width: 400, boxShadow: '0 24px 70px rgba(0,0,0,0.35)',
  },
  logo: { textAlign: 'center', marginBottom: 24 },
  logoImg: { width: 88, height: 'auto', marginBottom: 12 },
  titulo: { fontSize: 22, fontWeight: 800, color: GUINDA, textAlign: 'center' },
  subtitulo: { fontSize: 13, color: '#64748b', textAlign: 'center', marginTop: 4 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: {
    width: '100%', padding: '11px 14px', fontSize: 14, boxSizing: 'border-box',
    border: '1.5px solid #d1d5db', borderRadius: 12, outline: 'none',
  },
  error: {
    background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8,
    padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16,
  },
  btn: {
    width: '100%', padding: '13px', background: GUINDA, color: '#fff',
    border: 'none', borderRadius: 12, fontSize: 15, fontWeight: 700, cursor: 'pointer',
  },
  ayuda: {
    marginTop: 20, padding: '12px', background: '#fbece9',
    borderRadius: 8, fontSize: 12, color: GUINDA,
  },
  footer: {
    background: GUINDA, color: '#fff', display: 'grid',
    gridTemplateColumns: 'repeat(3, 1fr)', padding: '22px 16px',
  },
  col: { textAlign: 'center', padding: '0 12px' },
  colTit: { fontSize: 14, fontWeight: 700, letterSpacing: 0.6, marginBottom: 6 },
  colTxt: { fontSize: 12.5, color: 'rgba(255,255,255,0.8)', lineHeight: 1.6 },
};

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);
  const navigate = useNavigate();
  const { handleLogin } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCargando(true);
    setError('');
    try {
      const data = await login(form.username, form.password);
      handleLogin(data);
      navigate('/dashboard');
    } catch {
      setError('Usuario o contraseña incorrectos');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div style={s.page}>
      <div style={s.overlay}>
        <div style={s.card}>
          <div style={s.logo}>
            <img src="/images/logo-uni.png" alt="UNI" style={s.logoImg} />
            <div style={s.titulo}>Centro Médico UNI</div>
            <div style={s.subtitulo}>Sistema de Gestión Médica</div>
          </div>

          <form onSubmit={handleSubmit}>
            <div style={s.campo}>
              <label style={s.label}>Usuario</label>
              <input
                style={s.input} type="text" required autoFocus
                value={form.username}
                onChange={e => setForm(f => ({ ...f, username: e.target.value }))}
                placeholder="Ingresa tu usuario"
              />
            </div>
            <div style={s.campo}>
              <label style={s.label}>Contraseña</label>
              <input
                style={s.input} type="password" required
                value={form.password}
                onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
                placeholder="Ingresa tu contraseña"
              />
            </div>

            {error && <div style={s.error}>⚠️ {error}</div>}

            <button type="submit" style={s.btn} disabled={cargando}>
              {cargando ? 'Ingresando...' : 'Ingresar al sistema'}
            </button>
          </form>

          <div style={s.ayuda}>
            <strong>Credenciales de prueba:</strong><br />
            Admin: admin / admin123<br />
            Médico: medico1 / admin123
          </div>
        </div>
      </div>

      {/* Footer institucional (igual al desktop) */}
      <footer style={s.footer}>
        <div style={s.col}>
          <div style={s.colTit}>ENTÉRATE</div>
          <div style={s.colTxt}>Portal UNI<br />Unisalud</div>
        </div>
        <div style={{ ...s.col, borderLeft: '1px solid rgba(255,255,255,0.25)' }}>
          <div style={s.colTit}>CONTÁCTANOS</div>
          <div style={s.colTxt}>centromedico@uni.edu.pe<br />(01) 481-1070</div>
        </div>
        <div style={{ ...s.col, borderLeft: '1px solid rgba(255,255,255,0.25)' }}>
          <div style={s.colTit}>ENCUÉNTRANOS</div>
          <div style={s.colTxt}>Campus UNI<br />Av. Túpac Amaru 210, Rímac</div>
        </div>
      </footer>
    </div>
  );
}

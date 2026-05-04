import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { useAuth } from '../context/AuthContext';

const s = {
  page: {
    minHeight: '100vh', display: 'flex', alignItems: 'center',
    justifyContent: 'center', background: 'linear-gradient(135deg, #1a73e8 0%, #1557b0 100%)',
  },
  card: {
    background: '#fff', borderRadius: 16, padding: '40px 36px',
    width: 380, boxShadow: '0 20px 60px rgba(0,0,0,0.15)',
  },
  logo: { textAlign: 'center', marginBottom: 28 },
  logoIcon: { fontSize: 48, display: 'block', marginBottom: 8 },
  titulo: { fontSize: 18, fontWeight: 700, color: '#1e293b', textAlign: 'center' },
  subtitulo: { fontSize: 13, color: '#64748b', textAlign: 'center', marginTop: 4 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: {
    width: '100%', padding: '10px 12px', fontSize: 14,
    border: '1.5px solid #d1d5db', borderRadius: 8, outline: 'none',
    transition: 'border-color 0.2s',
  },
  error: {
    background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8,
    padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16,
  },
  btn: {
    width: '100%', padding: '12px', background: '#1a73e8', color: '#fff',
    border: 'none', borderRadius: 8, fontSize: 15, fontWeight: 600,
    cursor: 'pointer', transition: 'background 0.2s',
  },
  ayuda: {
    marginTop: 20, padding: '12px', background: '#f0f9ff',
    borderRadius: 8, fontSize: 12, color: '#0369a1',
  },
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
      <div style={s.card}>
        <div style={s.logo}>
          <span style={s.logoIcon}>🏥</span>
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
          Médico: medico1 / medico123
        </div>
      </div>
    </div>
  );
}

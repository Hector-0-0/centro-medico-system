import { useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { login } from '../services/authService';
import { useAuth } from '../context/AuthContext';

const CONFIG = {
  paciente: {
    titulo: 'Portal del Estudiante',
    subtitulo: 'Accede a tu historial y agenda citas',
    icono: '🎓',
    color: '#1a73e8',
    hint: { user: 'paciente1', pass: 'admin123' },
    rolesPermitidos: ['PACIENTE', 'ADMIN'],
  },
  medico: {
    titulo: 'Portal del Médico',
    subtitulo: 'Gestiona tu agenda y consultas',
    icono: '👨‍⚕️',
    color: '#1d9e75',
    hint: { user: 'medico1', pass: 'admin123' },
    rolesPermitidos: ['MEDICO', 'ADMIN'],
  },
  admin: {
    titulo: 'Portal Administrativo',
    subtitulo: 'Administración del Centro Médico',
    icono: '🏥',
    color: '#7c3aed',
    hint: { user: 'admin', pass: 'admin123' },
    rolesPermitidos: ['ADMIN', 'RECEPCIONISTA'],
  },
};

export default function LoginRol() {
  const { tipo } = useParams(); // 'paciente' | 'medico' | 'admin'
  const cfg = CONFIG[tipo] || CONFIG.admin;
  const navigate = useNavigate();
  const { handleLogin } = useAuth();

  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCargando(true);
    setError('');
    try {
      const data = await login(form.username, form.password);
      // Verificar que el rol sea el correcto para este portal
      if (!cfg.rolesPermitidos.includes(data.rol)) {
        setError(`Este portal es para ${cfg.titulo.toLowerCase()}. Tu cuenta es de tipo ${data.rol}.`);
        localStorage.clear();
        return;
      }
      handleLogin(data);
      navigate('/dashboard');
    } catch {
      setError('Usuario o contraseña incorrectos');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh', display: 'flex',
      background: `linear-gradient(135deg, ${cfg.color}15 0%, #fff 60%)`,
    }}>
      {/* Panel izquierdo informativo */}
      <div style={{
        width: 380, background: cfg.color, display: 'flex', flexDirection: 'column',
        justifyContent: 'center', padding: '48px 40px', color: '#fff',
      }}>
        <div style={{ fontSize: 56, marginBottom: 24 }}>{cfg.icono}</div>
        <h2 style={{ fontSize: 26, fontWeight: 800, marginBottom: 12, lineHeight: 1.3 }}>{cfg.titulo}</h2>
        <p style={{ fontSize: 15, opacity: 0.85, lineHeight: 1.6, marginBottom: 32 }}>{cfg.subtitulo}</p>
        <div style={{ borderTop: '1px solid rgba(255,255,255,0.2)', paddingTop: 24 }}>
          <div style={{ fontSize: 13, opacity: 0.7, marginBottom: 8 }}>Centro Médico</div>
          <div style={{ fontSize: 16, fontWeight: 700 }}>Universidad Nacional de Ingeniería</div>
        </div>
        <Link to="/" style={{ marginTop: 'auto', paddingTop: 40, color: 'rgba(255,255,255,0.7)', fontSize: 13, textDecoration: 'none', display: 'flex', alignItems: 'center', gap: 6 }}>
          ← Volver al portal principal
        </Link>
      </div>

      {/* Formulario */}
      <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 40 }}>
        <div style={{ width: '100%', maxWidth: 400 }}>
          <h1 style={{ fontSize: 26, fontWeight: 700, color: '#0f172a', marginBottom: 6 }}>
            Iniciar sesión
          </h1>
          <p style={{ fontSize: 14, color: '#64748b', marginBottom: 32 }}>
            Ingresa tus credenciales para continuar
          </p>

          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: 18 }}>
              <label style={{ display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 }}>
                Usuario
              </label>
              <input
                type="text" required autoFocus
                value={form.username}
                onChange={e => setForm(f => ({ ...f, username: e.target.value }))}
                placeholder="Ingresa tu usuario"
                style={{ width: '100%', padding: '11px 14px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' }}
              />
            </div>
            <div style={{ marginBottom: 18 }}>
              <label style={{ display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 }}>
                Contraseña
              </label>
              <input
                type="password" required
                value={form.password}
                onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
                placeholder="Ingresa tu contraseña"
                style={{ width: '100%', padding: '11px 14px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' }}
              />
            </div>

            {error && (
              <div style={{ background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 14px', fontSize: 13, color: '#dc2626', marginBottom: 18 }}>
                ⚠️ {error}
              </div>
            )}

            <button
              type="submit" disabled={cargando}
              style={{ width: '100%', padding: '12px', background: cfg.color, color: '#fff', border: 'none', borderRadius: 8, fontSize: 15, fontWeight: 700, cursor: 'pointer' }}
            >
              {cargando ? 'Ingresando...' : 'Ingresar'}
            </button>
          </form>

          {/* Hint de credenciales */}
          <div style={{ marginTop: 24, padding: '14px 16px', background: '#f0fdf4', border: '1px solid #bbf7d0', borderRadius: 8 }}>
            <div style={{ fontSize: 12, fontWeight: 700, color: '#166534', marginBottom: 4 }}>Credenciales de prueba:</div>
            <div style={{ fontSize: 12, color: '#166534' }}>Usuario: <strong>{cfg.hint.user}</strong> · Contraseña: <strong>{cfg.hint.pass}</strong></div>
          </div>

          {/* Otros portales */}
          <div style={{ marginTop: 28, paddingTop: 24, borderTop: '1px solid #e2e8f0' }}>
            <div style={{ fontSize: 12, color: '#94a3b8', marginBottom: 10, textAlign: 'center' }}>¿Buscas otro portal?</div>
            <div style={{ display: 'flex', gap: 8, justifyContent: 'center' }}>
              {Object.entries(CONFIG).filter(([key]) => key !== tipo).map(([key, c]) => (
                <Link key={key} to={`/login/${key}`}
                  style={{ padding: '6px 14px', border: `1.5px solid ${c.color}`, borderRadius: 6, fontSize: 12, color: c.color, textDecoration: 'none', fontWeight: 600 }}>
                  {c.icono} {key.charAt(0).toUpperCase() + key.slice(1)}
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
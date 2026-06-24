import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { useAuth } from '../context/AuthContext';
import { landingPath } from '../components/menu';
import './Login.css';

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);
  const navigate = useNavigate();
  const { handleLogin } = useAuth();

  const handleChange = (e) =>
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setCargando(true);
    setError('');
    try {
      const data = await login(form.username.trim(), form.password);
      handleLogin(data);
      navigate(landingPath(data.rol));
    } catch {
      setError('Usuario o contraseña incorrectos');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div
      className="login"
      style={{ backgroundImage: "url('/images/banner-uni.jpeg')" }}
    >
      <div className="login__overlay">
        <form className="login__card" onSubmit={handleSubmit}>
          <div className="login__brand">
            <img
              className="login__logo"
              src="/images/logo-uni.png"
              alt="Universidad Nacional de Ingeniería"
            />
            <h1 className="login__title">Centro Médico UNI</h1>
            <p className="login__subtitle">Universidad Nacional de Ingeniería</p>
          </div>

          <div className="login__field">
            <label className="login__label" htmlFor="username">Usuario</label>
            <input
              id="username"
              name="username"
              className="login__input"
              type="text"
              autoComplete="username"
              placeholder="Ingresa tu usuario"
              value={form.username}
              onChange={handleChange}
              required
              autoFocus
            />
          </div>

          <div className="login__field">
            <label className="login__label" htmlFor="password">Contraseña</label>
            <input
              id="password"
              name="password"
              className="login__input"
              type="password"
              autoComplete="current-password"
              placeholder="Ingresa tu contraseña"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          {error && (
            <div className="login__error" role="alert">{error}</div>
          )}

          <button className="login__btn" type="submit" disabled={cargando}>
            {cargando ? 'Ingresando…' : 'Ingresar'}
          </button>

          <div className="login__help">
            <strong>Credenciales de prueba:</strong><br />
            Admin: ADM001 / adm123<br />
            Doctor: D001 / pass123<br />
            Estudiante: U001 / 1234<br />
            Farmacia: FAR001 / far123
          </div>
        </form>
      </div>

      <footer className="login__footer">
        <div className="login__col">
          <div className="login__col-title">ENTÉRATE</div>
          <div className="login__col-text">Portal UNI<br />Unisalud</div>
        </div>
        <div className="login__col">
          <div className="login__col-title">CONTÁCTANOS</div>
          <div className="login__col-text">centromedico@uni.edu.pe<br />(01) 481-1070</div>
        </div>
        <div className="login__col">
          <div className="login__col-title">ENCUÉNTRANOS</div>
          <div className="login__col-text">Campus UNI<br />Av. Túpac Amaru 210, Rímac</div>
        </div>
      </footer>
    </div>
  );
}

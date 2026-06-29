import axios from 'axios';

/**
 * Instancia central de Axios con la URL base del backend.
 * Agrega el token JWT automáticamente en cada petición.
 */
// URL base de la API. Por defecto es relativa ("/api"): en producción nginx
// hace de proxy hacia el backend (mismo origen → sin CORS); en desarrollo el
// proxy de CRA (campo "proxy" en package.json) la reenvía a localhost:8080.
// Se puede sobreescribir con REACT_APP_API_URL si el backend vive en otro host.
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '/api',
});

// Interceptor de REQUEST: adjunta el token JWT si existe
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor de RESPONSE: si el token expiró/es inválido (401) cierra la
// sesión y vuelve al login. El 403 (sin permiso para ese recurso) NO cierra
// sesión: se deja pasar para que la página lo muestre con mensajeError().
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const enLogin = window.location.pathname === '/login' || window.location.pathname === '/';
    if (error.response?.status === 401 && !enLogin) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

/**
 * Extrae un mensaje legible de un error de Axios. Prioriza el primer error de
 * validación de campo (backend: { campos: { campo: mensaje } }), luego el
 * mensaje general, y por último un texto por defecto.
 */
export const mensajeError = (err, porDefecto = 'Ocurrió un error. Inténtalo de nuevo.') => {
  const data = err?.response?.data;
  if (!data) return porDefecto;
  if (data.campos && typeof data.campos === 'object') {
    const primero = Object.values(data.campos)[0];
    if (primero) return primero;
  }
  return data.error || porDefecto;
};

export default api;

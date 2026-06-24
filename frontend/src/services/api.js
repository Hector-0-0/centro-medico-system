import axios from 'axios';

/**
 * Instancia central de Axios con la URL base del backend.
 * Agrega el token JWT automáticamente en cada petición.
 */
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Interceptor de REQUEST: adjunta el token JWT si existe
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor de RESPONSE: redirige al login si el token expiró
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
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

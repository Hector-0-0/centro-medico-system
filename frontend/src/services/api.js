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

export default api;

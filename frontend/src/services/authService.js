import api from './api';

/**
 * Inicia sesión y guarda el token + perfil en localStorage.
 */
export const login = async (username, password) => {
  const { data } = await api.post('/auth/login', { username, password });
  localStorage.setItem('token',    data.token);
  localStorage.setItem('username', data.username);
  localStorage.setItem('rol',      data.rol);
  if (data.nombre) localStorage.setItem('nombre', data.nombre);
  return data;
};

/**
 * Carga el perfil del usuario autenticado (GET /api/auth/me) y guarda el nombre.
 */
export const cargarPerfil = async () => {
  try {
    const { data } = await api.get('/auth/me');
    if (data.nombre) localStorage.setItem('nombre', data.nombre);
    return data;
  } catch (e) {
    console.error('Error cargando perfil:', e);
    return null;
  }
};

export const logout = () => {
  localStorage.clear();
  window.location.href = '/';
};

export const isLoggedIn  = () => !!localStorage.getItem('token');
export const getRol      = () => localStorage.getItem('rol');
export const getUsername = () => localStorage.getItem('username');
export const getNombre   = () => localStorage.getItem('nombre') || getUsername();
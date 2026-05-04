import api from './api';

/**
 * Inicia sesión y guarda el token + perfil en localStorage.
 */
export const login = async (username, password) => {
  const { data } = await api.post('/auth/login', { username, password });
  localStorage.setItem('token',     data.token);
  localStorage.setItem('username',  data.username);
  localStorage.setItem('rol',       data.rol);
  localStorage.setItem('usuarioId', data.usuarioId);
  return data;
};

/**
 * Carga el perfil completo del usuario autenticado (incluye pacienteId o medicoId).
 * Llama a GET /api/auth/me y guarda todo en localStorage.
 */
export const cargarPerfil = async () => {
  try {
    const { data } = await api.get('/auth/me');
    if (data.pacienteId) localStorage.setItem('pacienteId', data.pacienteId);
    if (data.medicoId)   localStorage.setItem('medicoId',   data.medicoId);
    if (data.nombre)     localStorage.setItem('nombre',     data.nombre);
    if (data.apellido)   localStorage.setItem('apellido',   data.apellido);
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
export const getPacienteId = () => {
  const id = localStorage.getItem('pacienteId');
  return id ? Number(id) : null;
};
export const getMedicoId = () => {
  const id = localStorage.getItem('medicoId');
  return id ? Number(id) : null;
};
export const getNombre = () => {
  const nombre   = localStorage.getItem('nombre')   || '';
  const apellido = localStorage.getItem('apellido') || '';
  return `${nombre} ${apellido}`.trim() || getUsername();
};
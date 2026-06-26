import api from './api';

/** Perfil del usuario autenticado (datos + estadísticas de citas). */
export const obtenerPerfil = () =>
  api.get('/perfil').then((r) => r.data);

/** Editar el perfil del estudiante (email, carrera, fecha de nacimiento, foto). */
export const actualizarPerfil = (data) =>
  api.put('/perfil', data).then((r) => r.data);

/** Editar el perfil del médico (consultorio, foto). */
export const actualizarPerfilDoctor = (data) =>
  api.put('/perfil/doctor', data).then((r) => r.data);

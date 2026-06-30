import api from './api';

/** Perfil del usuario autenticado (datos + estadísticas de citas). */
export const obtenerPerfil = () =>
  api.get('/perfil').then((r) => r.data);

/** Editar el perfil del estudiante (email, carrera, fecha de nacimiento, foto). */
export const actualizarPerfil = (data) =>
  api.put('/perfil', data).then((r) => r.data);

/** Editar el perfil del médico (consultorio, contacto, foto, credenciales). */
export const actualizarPerfilDoctor = (data) =>
  api.put('/perfil/doctor', data).then((r) => r.data);

/** Editar el perfil de un empleado ADMIN/FARMACIA (contacto, foto, credenciales). */
export const actualizarPerfilEmpleado = (data) =>
  api.put('/perfil/empleado', data).then((r) => r.data);

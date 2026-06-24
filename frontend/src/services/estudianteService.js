import api from './api';

/** Pacientes (tabla `estudiantes`). Solo ADMIN en el backend. */
export const listarEstudiantes = () =>
  api.get('/estudiantes').then((r) => r.data);

export const crearEstudiante = (data) =>
  api.post('/estudiantes', data).then((r) => r.data);

export const eliminarEstudiante = (id) =>
  api.delete(`/estudiantes/${id}`);

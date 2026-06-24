import api from './api';

/** Todas las citas (panel ADMIN). Solo ADMIN en el backend. */
export const listarCitas = () =>
  api.get('/citas').then((r) => r.data);

/** Citas del médico autenticado (panel "Mis Citas"). Solo DOCTOR. */
export const listarMisCitas = () =>
  api.get('/citas/mias').then((r) => r.data);

/** Citas del estudiante autenticado (panel "Mis Citas"). Solo ESTUDIANTE. */
export const listarCitasEstudiante = () =>
  api.get('/citas/estudiante').then((r) => r.data);

/** Detalle de la atención de una cita propia (diagnósticos + comentarios). */
export const obtenerAtencion = (idCita) =>
  api.get(`/citas/${idCita}/atencion`).then((r) => r.data);

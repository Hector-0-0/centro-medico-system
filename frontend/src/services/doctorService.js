import api from './api';

/** Médicos (tabla `doctores`). Solo ADMIN en el backend. */
export const listarDoctores = () =>
  api.get('/doctores').then((r) => r.data);

export const crearDoctor = (data) =>
  api.post('/doctores', data).then((r) => r.data);

export const eliminarDoctor = (id) =>
  api.delete(`/doctores/${id}`);

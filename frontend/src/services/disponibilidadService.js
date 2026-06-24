import api from './api';

/** Disponibilidad del médico autenticado. Solo DOCTOR. */
export const listarDisponibilidad = () =>
  api.get('/disponibilidades').then((r) => r.data);

export const guardarDisponibilidad = (dias) =>
  api.post('/disponibilidades', { dias }).then((r) => r.data);

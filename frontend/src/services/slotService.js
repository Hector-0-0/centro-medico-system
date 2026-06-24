import api from './api';

/** Horarios (slots) de los médicos. Solo ESTUDIANTE. */
export const listarSlots = () =>
  api.get('/slots').then((r) => r.data);

/** Agendar una cita en un slot disponible. Solo ESTUDIANTE. */
export const agendarCita = (payload) =>
  api.post('/citas/agendar', payload).then((r) => r.data);

import api from './api';

/** Búsqueda de códigos CIE-10 (mín. 2 caracteres). Solo DOCTOR. */
export const buscarCie = (q) =>
  api.get('/cie', { params: { q } }).then((r) => r.data);

/** Medicamentos con stock disponible (para recetar). Solo DOCTOR. */
export const medicamentosConStock = () =>
  api.get('/medicamentos/con-stock').then((r) => r.data);

/** Registra la atención de una cita (consulta médica). Solo DOCTOR. */
export const atenderCita = (idCita, payload) =>
  api.post(`/citas/${idCita}/atender`, payload).then((r) => r.data);

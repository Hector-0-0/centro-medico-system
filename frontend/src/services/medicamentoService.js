import api from './api';

/** Inventario de medicamentos (Ver Stock / Gestión de Stock). */
export const listarMedicamentos = () =>
  api.get('/medicamentos').then((r) => r.data);

/** Registrar un medicamento nuevo. Solo FARMACIA. */
export const crearMedicamento = (data) =>
  api.post('/medicamentos', data).then((r) => r.data);

/** Ajustar el stock de un medicamento. Solo FARMACIA. */
export const actualizarStock = (id, stock) =>
  api.put(`/medicamentos/${id}/stock`, { stock }).then((r) => r.data);

/** Movimientos de stock (auditoría) de un medicamento. DOCTOR o FARMACIA. */
export const listarMovimientos = (id) =>
  api.get(`/medicamentos/${id}/movimientos`).then((r) => r.data);

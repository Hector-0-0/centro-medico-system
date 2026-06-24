import api from './api';

/** Recetas de farmacia. Solo FARMACIA. */
export const listarRecetasPendientes = () =>
  api.get('/recetas/pendientes').then((r) => r.data);

export const entregarReceta = (id) =>
  api.post(`/recetas/${id}/entregar`).then((r) => r.data);

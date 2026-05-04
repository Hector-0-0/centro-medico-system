import api from './api';

// ─── PACIENTES ────────────────────────────────────────────────────────────────
export const pacienteService = {
  listar: (buscar = '') => api.get(`/pacientes${buscar ? `?buscar=${buscar}` : ''}`),
  buscarPorId: (id) => api.get(`/pacientes/${id}`),
  buscarPorDni: (dni) => api.get(`/pacientes/dni/${dni}`),
  registrar: (data) => api.post('/pacientes', data),
  actualizar: (id, data) => api.put(`/pacientes/${id}`, data),
  eliminar: (id) => api.delete(`/pacientes/${id}`),
};

// ─── MÉDICOS ─────────────────────────────────────────────────────────────────
export const medicoService = {
  listar: (params = {}) => api.get('/medicos', { params }),
  buscarPorId: (id) => api.get(`/medicos/${id}`),
  registrar: (data) => api.post('/medicos', data),
  actualizar: (id, data) => api.put(`/medicos/${id}`, data),
  eliminar: (id) => api.delete(`/medicos/${id}`),
};

// ─── ESPECIALIDADES ───────────────────────────────────────────────────────────
export const especialidadService = {
  listar: () => api.get('/especialidades'),
  registrar: (data) => api.post('/especialidades', data),
  eliminar: (id) => api.delete(`/especialidades/${id}`),
};

// ─── CITAS ────────────────────────────────────────────────────────────────────
export const citaService = {
  listar: () => api.get('/citas'),
  listarHoy: () => api.get('/citas/hoy'),
  buscarPorId: (id) => api.get(`/citas/${id}`),
  listarPorPaciente: (id) => api.get(`/citas/paciente/${id}`),
  listarPorMedico: (id) => api.get(`/citas/medico/${id}`),
  crear: (data) => api.post('/citas', data),
  cancelar: (id) => api.put(`/citas/${id}/cancelar`),
  atender: (id) => api.put(`/citas/${id}/atender`),
  reprogramar: (id, data) => api.put(`/citas/${id}/reprogramar`, data),
};

// ─── HISTORIALES ──────────────────────────────────────────────────────────────
export const historialService = {
  listarPorPaciente: (id) => api.get(`/historiales/paciente/${id}`),
  buscarPorCita: (id) => api.get(`/historiales/cita/${id}`),
  registrar: (data) => api.post('/historiales', data),
  actualizar: (id, data) => api.put(`/historiales/${id}`, data),
};

// ─── MEDICAMENTOS ─────────────────────────────────────────────────────────────
export const medicamentoService = {
  listar: () => api.get('/medicamentos'),
  stockBajo: () => api.get('/medicamentos/stock-bajo'),
  registrar: (data) => api.post('/medicamentos', data),
  actualizar: (id, data) => api.put(`/medicamentos/${id}`, data),
  actualizarStock: (id, cantidad) => api.patch(`/medicamentos/${id}/stock`, { cantidad }),
  desactivar: (id) => api.delete(`/medicamentos/${id}`),
};

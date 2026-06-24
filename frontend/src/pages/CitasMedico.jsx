import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarMisCitas } from '../services/citaService';

/** Fecha de hoy en español, con la primera letra en mayúscula. */
function fechaHoy() {
  const txt = new Date().toLocaleDateString('es', {
    weekday: 'long',
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  });
  return txt.charAt(0).toUpperCase() + txt.slice(1);
}

const ESTADOS = ['Todos', 'PENDIENTE', 'ATENDIDA', 'CANCELADA'];

/** Mis Citas del médico — réplica del CitaPanel del desktop (solo lectura). */
export default function CitasMedico() {
  const [lista, setLista] = useState([]);
  const [estado, setEstado] = useState('Todos');
  const navigate = useNavigate();

  useEffect(() => {
    listarMisCitas()
      .then(setLista)
      .catch(() => setLista([]));
  }, []);

  const filtradas = estado === 'Todos' ? lista : lista.filter((c) => c.estado === estado);

  // Doble clic en una cita PENDIENTE abre la atención (igual que el desktop).
  const atender = (c) => {
    if (c.estado !== 'PENDIENTE') {
      alert('Solo se pueden atender citas PENDIENTES.');
      return;
    }
    navigate(`/citas-medico/${c.id}/atender`, { state: { cita: c } });
  };

  return (
    <div>
      <h1 className="panel__title">Mis Citas del Día</h1>
      <p className="panel__hint" style={{ marginBottom: 16 }}>
        {fechaHoy()} — doble clic en una cita PENDIENTE para atenderla.
      </p>

      <div className="toolbar">
        <label className="toolbar__label">
          Estado
          <select
            className="toolbar__select"
            value={estado}
            onChange={(e) => setEstado(e.target.value)}
          >
            {ESTADOS.map((es) => <option key={es} value={es}>{es}</option>)}
          </select>
        </label>
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Paciente</th>
              <th>Día</th>
              <th>Hora</th>
              <th>Motivo</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {filtradas.length === 0 ? (
              <tr>
                <td className="table__empty" colSpan={6}>No tienes citas registradas</td>
              </tr>
            ) : (
              filtradas.map((c) => (
                <tr key={c.id} onDoubleClick={() => atender(c)}>
                  <td>{String(c.id).padStart(4, '0')}</td>
                  <td>{c.nombreEstudiante || c.idEstudiante}</td>
                  <td>{c.diaSemana || '—'}</td>
                  <td>
                    {c.horaInicio}
                    {c.horaFin ? ` - ${c.horaFin}` : ''}
                  </td>
                  <td>{c.motivo}</td>
                  <td>{c.estado}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

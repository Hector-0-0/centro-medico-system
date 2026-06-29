import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarMisCitas, cancelarCita } from '../services/citaService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';
import { useDialog } from '../components/Dialog';

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
  const { datos, cargando, error, recargar } = useCargar(listarMisCitas);
  const lista = datos || [];
  const [estado, setEstado] = useState('Todos');
  const navigate = useNavigate();
  const { alerta, confirmar } = useDialog();

  const filtradas = estado === 'Todos' ? lista : lista.filter((c) => c.estado === estado);

  // Doble clic en una cita PENDIENTE abre la atención (igual que el desktop).
  const atender = (c) => {
    if (c.estado !== 'PENDIENTE') {
      alerta('Solo se pueden atender citas PENDIENTES.');
      return;
    }
    navigate(`/citas-medico/${c.id}/atender`, { state: { cita: c } });
  };

  // Cancelar una cita pendiente: libera el turno para que vuelva a estar disponible.
  const cancelar = async (c) => {
    if (!(await confirmar(
      `¿Cancelar la cita de ${c.nombreEstudiante || c.idEstudiante}? El turno volverá a quedar disponible.`,
      { peligro: true, textoOk: 'Cancelar cita', textoCancelar: 'Volver' },
    ))) return;
    try {
      await cancelarCita(c.id);
      recargar();
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo cancelar la cita.'));
    }
  };

  return (
    <div>
      <h1 className="panel__title">Mis Citas del Día</h1>
      <p className="panel__hint" style={{ marginBottom: 16 }}>
        {fechaHoy()} — doble clic (o Enter) en una cita PENDIENTE para atenderla.
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
              <th></th>
            </tr>
          </thead>
          <tbody>
            {cargando || error || filtradas.length === 0 ? (
              <FilaTablaEstado
                colSpan={7}
                cargando={cargando}
                error={error}
                onReintentar={recargar}
                vacio="No tienes citas registradas"
              />
            ) : (
              filtradas.map((c) => (
                <tr
                  key={c.id}
                  onDoubleClick={() => atender(c)}
                  tabIndex={0}
                  onKeyDown={(ev) => {
                    if (ev.key === 'Enter') {
                      ev.preventDefault();
                      atender(c);
                    }
                  }}
                >
                  <td>{String(c.id).padStart(4, '0')}</td>
                  <td>{c.nombreEstudiante || c.idEstudiante}</td>
                  <td>{c.diaSemana || '—'}</td>
                  <td>
                    {c.horaInicio}
                    {c.horaFin ? ` - ${c.horaFin}` : ''}
                  </td>
                  <td>{c.motivo}</td>
                  <td>{c.estado}</td>
                  <td>
                    {c.estado === 'PENDIENTE' && (
                      <button
                        className="btn btn--danger btn--sm"
                        onClick={(e) => { e.stopPropagation(); cancelar(c); }}
                      >
                        Cancelar
                      </button>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

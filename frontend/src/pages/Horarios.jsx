import { useEffect, useMemo, useState } from 'react';
import { listarSlots, agendarCita } from '../services/slotService';
import { mensajeError } from '../services/api';

const distintos = (arr) => [...new Set(arr)];

/** Horarios — réplica del DashboardPanel del desktop (ver slots + agendar). */
export default function Horarios() {
  const [slots, setSlots] = useState([]);
  const [especialidad, setEspecialidad] = useState('Todas');
  const [estado, setEstado] = useState('Todos'); // Todos | Disponibles | Ocupados
  const [modalAbierto, setModalAbierto] = useState(false);

  const cargar = async () => {
    try {
      setSlots(await listarSlots());
    } catch {
      setSlots([]);
    }
  };

  useEffect(() => {
    cargar();
  }, []);

  const especialidades = useMemo(
    () => ['Todas', ...distintos(slots.map((s) => s.especialidad)).sort()],
    [slots],
  );

  const filtrados = slots.filter(
    (s) =>
      (especialidad === 'Todas' || s.especialidad === especialidad) &&
      (estado === 'Todos' ||
        (estado === 'Disponibles' && s.disponible) ||
        (estado === 'Ocupados' && !s.disponible)),
  );

  return (
    <div>
      <h1 className="panel__title">Horarios Disponibles</h1>

      <div className="toolbar">
        <label className="toolbar__label">
          Especialidad
          <select
            className="toolbar__select"
            value={especialidad}
            onChange={(e) => setEspecialidad(e.target.value)}
          >
            {especialidades.map((esp) => <option key={esp} value={esp}>{esp}</option>)}
          </select>
        </label>
        <label className="toolbar__label">
          Estado
          <select
            className="toolbar__select"
            value={estado}
            onChange={(e) => setEstado(e.target.value)}
          >
            {['Todos', 'Disponibles', 'Ocupados'].map((es) => (
              <option key={es} value={es}>{es}</option>
            ))}
          </select>
        </label>
        <button className="btn btn--primary" onClick={() => setModalAbierto(true)}>
          + Agendar Cita
        </button>
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Especialidad</th>
              <th>Médico</th>
              <th>Día</th>
              <th>Hora</th>
              <th>Consultorio</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {filtrados.length === 0 ? (
              <tr>
                <td className="table__empty" colSpan={6}>Sin horarios</td>
              </tr>
            ) : (
              filtrados.map((s) => (
                <tr key={s.id}>
                  <td>{s.especialidad}</td>
                  <td>{s.nombreDoctor}</td>
                  <td>{s.diaSemana}</td>
                  <td>{s.horaInicio} - {s.horaFin}</td>
                  <td>{s.consultorio}</td>
                  <td>{s.disponible ? 'Disponible' : 'Ocupado'}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {modalAbierto && (
        <AgendarModal
          slots={slots.filter((s) => s.disponible)}
          onClose={() => setModalAbierto(false)}
          onSaved={() => {
            setModalAbierto(false);
            cargar();
          }}
        />
      )}
    </div>
  );
}

function AgendarModal({ slots, onClose, onSaved }) {
  const [especialidad, setEspecialidad] = useState('');
  const [medico, setMedico] = useState('');
  const [dia, setDia] = useState('');
  const [idSlot, setIdSlot] = useState('');
  const [motivo, setMotivo] = useState('');
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const especialidades = distintos(slots.map((s) => s.especialidad)).sort();
  const medicos = distintos(
    slots.filter((s) => s.especialidad === especialidad).map((s) => s.nombreDoctor),
  ).sort();
  const dias = distintos(
    slots
      .filter((s) => s.especialidad === especialidad && s.nombreDoctor === medico)
      .map((s) => s.diaSemana),
  );
  const horas = slots.filter(
    (s) => s.especialidad === especialidad && s.nombreDoctor === medico && s.diaSemana === dia,
  );

  const guardar = async (e) => {
    e.preventDefault();
    setError('');
    if (!idSlot) {
      setError('Selecciona especialidad, médico, día y hora.');
      return;
    }
    if (!motivo.trim()) {
      setError('Ingresa el motivo de la consulta.');
      return;
    }
    setGuardando(true);
    try {
      await agendarCita({ idSlot: Number(idSlot), motivo: motivo.trim() });
      onSaved();
    } catch (err) {
      setError(mensajeError(err, 'No se pudo agendar la cita.'));
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="modal__overlay" onClick={onClose}>
      <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={guardar}>
        <div className="modal__header">Agendar Cita</div>
        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}

          <label className="field">
            <span className="field__label">Especialidad</span>
            <select
              className="toolbar__select"
              value={especialidad}
              onChange={(e) => { setEspecialidad(e.target.value); setMedico(''); setDia(''); setIdSlot(''); }}
            >
              <option value="">Selecciona...</option>
              {especialidades.map((esp) => <option key={esp} value={esp}>{esp}</option>)}
            </select>
          </label>

          <label className="field">
            <span className="field__label">Médico</span>
            <select
              className="toolbar__select"
              value={medico}
              disabled={!especialidad}
              onChange={(e) => { setMedico(e.target.value); setDia(''); setIdSlot(''); }}
            >
              <option value="">Selecciona...</option>
              {medicos.map((m) => <option key={m} value={m}>{m}</option>)}
            </select>
          </label>

          <label className="field">
            <span className="field__label">Día</span>
            <select
              className="toolbar__select"
              value={dia}
              disabled={!medico}
              onChange={(e) => { setDia(e.target.value); setIdSlot(''); }}
            >
              <option value="">Selecciona...</option>
              {dias.map((d) => <option key={d} value={d}>{d}</option>)}
            </select>
          </label>

          <label className="field">
            <span className="field__label">Hora</span>
            <select
              className="toolbar__select"
              value={idSlot}
              disabled={!dia}
              onChange={(e) => setIdSlot(e.target.value)}
            >
              <option value="">Selecciona...</option>
              {horas.map((s) => (
                <option key={s.id} value={s.id}>{s.horaInicio} - {s.horaFin}</option>
              ))}
            </select>
          </label>

          <label className="field">
            <span className="field__label">Motivo</span>
            <textarea
              className="textarea"
              value={motivo}
              onChange={(e) => setMotivo(e.target.value)}
              placeholder="Motivo de la consulta..."
            />
          </label>
        </div>
        <div className="modal__footer">
          <button type="button" className="btn btn--ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn btn--primary" disabled={guardando}>
            {guardando ? 'Agendando…' : 'Agendar'}
          </button>
        </div>
      </form>
    </div>
  );
}

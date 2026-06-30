import { useMemo, useState } from 'react';
import { listarSlots, agendarCita } from '../services/slotService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';

const distintos = (arr) => [...new Set(arr)];

/** Horarios — réplica del DashboardPanel del desktop (ver slots + agendar). */
export default function Horarios() {
  const { datos, cargando, error, recargar } = useCargar(listarSlots);
  const slots = datos || [];
  const [especialidad, setEspecialidad] = useState('Todas');
  const [medico, setMedico] = useState('Todos');
  const [ocultarOcupados, setOcultarOcupados] = useState(false);
  const [modalAbierto, setModalAbierto] = useState(false);
  const [slotAgendar, setSlotAgendar] = useState(null);

  const especialidades = useMemo(
    () => ['Todas', ...distintos(slots.map((s) => s.especialidad)).sort()],
    [slots],
  );

  const medicos = useMemo(
    () => ['Todos', ...distintos(slots.map((s) => s.nombreDoctor)).sort()],
    [slots],
  );

  // Agrupar slots por (especialidad, medico, diaSemana)
  const grupos = useMemo(() => {
    let filtrados = slots;

    if (especialidad !== 'Todas') {
      filtrados = filtrados.filter((s) => s.especialidad === especialidad);
    }
    if (medico !== 'Todos') {
      filtrados = filtrados.filter((s) => s.nombreDoctor === medico);
    }

    const gruposMap = {};
    filtrados.forEach((s) => {
      const key = `${s.especialidad}|${s.nombreDoctor}|${s.diaSemana}`;
      if (!gruposMap[key]) {
        gruposMap[key] = {
          especialidad: s.especialidad,
          nombreDoctor: s.nombreDoctor,
          consultorio: s.consultorio,
          diaSemana: s.diaSemana,
          slots: [],
        };
      }
      gruposMap[key].slots.push(s);
    });

    let gruposArr = Object.values(gruposMap);

    // Ordenar por especialidad, luego doctor, luego día
    const ordenDias = { Lunes: 1, Martes: 2, Miercoles: 3, Jueves: 4, Viernes: 5, Sabado: 6, Domingo: 7 };
    gruposArr.sort((a, b) => {
      if (a.especialidad !== b.especialidad) return a.especialidad.localeCompare(b.especialidad);
      if (a.nombreDoctor !== b.nombreDoctor) return a.nombreDoctor.localeCompare(b.nombreDoctor);
      return (ordenDias[a.diaSemana] || 0) - (ordenDias[b.diaSemana] || 0);
    });

    return gruposArr;
  }, [slots, especialidad, medico]);

  // Resetear médico cuando cambia especialidad
  const cambiarEspecialidad = (val) => {
    setEspecialidad(val);
    setMedico('Todos');
  };

  const abrirAgendar = (slot) => {
    setSlotAgendar(slot);
    setModalAbierto(true);
  };

  return (
    <div>
      <h1 className="panel__title">Horarios Disponibles</h1>

      <div className="toolbar">
        <label className="toolbar__label">
          Especialidad
          <select
            className="toolbar__select"
            value={especialidad}
            onChange={(e) => cambiarEspecialidad(e.target.value)}
          >
            {especialidades.map((esp) => <option key={esp} value={esp}>{esp}</option>)}
          </select>
        </label>
        <label className="toolbar__label">
          Médico
          <select
            className="toolbar__select"
            value={medico}
            onChange={(e) => setMedico(e.target.value)}
          >
            {medicos.map((m) => <option key={m} value={m}>{m}</option>)}
          </select>
        </label>
        <label className="toolbar__label toggle-label">
          <input
            type="checkbox"
            checked={ocultarOcupados}
            onChange={(e) => setOcultarOcupados(e.target.checked)}
          />
          Ocultar ocupados
        </label>
      </div>

      {cargando || error || grupos.length === 0 ? (
        <div className="slot-empty">
          {cargando ? 'Cargando horarios…' : error ? 'Error al cargar horarios' : 'No hay horarios que coincidan con los filtros.'}
        </div>
      ) : (
        <div className="slot-cards">
          {grupos.map((g, idx) => {
            const disponibles = g.slots.filter((s) => s.disponible).length;

            // Separar disponibles y ocupados
            const disponiblesList = g.slots.filter((s) => s.disponible);
            const ocupadosList = g.slots.filter((s) => !s.disponible);

            return (
              <div key={idx} className="slot-card">
                <div className="slot-card__header">
                  <div className="slot-card__header-left">
                    <span className="slot-card__especialidad">{g.especialidad}</span>
                    <span className="slot-card__nombre">{g.nombreDoctor}</span>
                  </div>
                  <div className="slot-card__header-right">
                    <span className="slot-card__dia">{g.diaSemana}</span>
                    <span className="slot-card__consultorio">Cons. {g.consultorio}</span>
                    <span className="slot-card__disponibles">
                      {disponibles} {disponibles === 1 ? 'disponible' : 'disponibles'}
                    </span>
                  </div>
                </div>
                <div className="slot-card__chips">
                  {disponiblesList.map((s) => (
                    <button
                      key={s.id}
                      className="chip chip--disponible"
                      onClick={() => abrirAgendar(s)}
                      title="Agendar cita"
                    >
                      {s.horaInicio}–{s.horaFin}
                    </button>
                  ))}
                  {!ocultarOcupados && ocupadosList.map((s) => (
                    <span
                      key={s.id}
                      className="chip chip--ocupado"
                      title="Ocupado"
                    >
                      {s.horaInicio}–{s.horaFin}
                    </span>
                  ))}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {modalAbierto && (
        <AgendarModal
          slot={slotAgendar}
          onClose={() => { setModalAbierto(false); setSlotAgendar(null); }}
          onSaved={() => {
            setModalAbierto(false);
            setSlotAgendar(null);
            recargar();
          }}
        />
      )}
    </div>
  );
}

function AgendarModal({ slot, onClose, onSaved }) {
  const [motivo, setMotivo] = useState('');
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const guardar = async (e) => {
    e.preventDefault();
    setError('');
    if (!motivo.trim()) {
      setError('Ingresa el motivo de la consulta.');
      return;
    }
    setGuardando(true);
    try {
      await agendarCita({ idSlot: slot.id, motivo: motivo.trim() });
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

          <p className="perfil__linea">
            <strong>{slot?.especialidad}</strong> — {slot?.nombreDoctor}
          </p>
          <p className="perfil__linea">
            {slot?.diaSemana} {slot?.horaInicio} – {slot?.horaFin} · Cons. {slot?.consultorio}
          </p>

          <label className="field">
            <span className="field__label">Motivo</span>
            <textarea
              className="textarea"
              value={motivo}
              onChange={(e) => setMotivo(e.target.value)}
              placeholder="Motivo de la consulta..."
              autoFocus
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

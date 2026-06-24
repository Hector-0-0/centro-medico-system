import { useEffect, useState } from 'react';
import {
  listarDisponibilidad,
  guardarDisponibilidad,
} from '../services/disponibilidadService';

const DIAS = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes'];

// Opciones de hora en pasos de 30 min (08:00 – 20:00).
const HORAS = [];
for (let m = 8 * 60; m <= 20 * 60; m += 30) {
  HORAS.push(`${String(Math.floor(m / 60)).padStart(2, '0')}:${String(m % 60).padStart(2, '0')}`);
}

const ESTADO_INICIAL = () =>
  DIAS.reduce(
    (acc, d) => ({ ...acc, [d]: { activo: false, inicio: '08:00', fin: '09:00' } }),
    {},
  );

/** Disponibilidad del médico — réplica del DisponibilidadPanel del desktop. */
export default function Disponibilidad() {
  const [actuales, setActuales] = useState([]);
  const [dias, setDias] = useState(ESTADO_INICIAL);
  const [mensaje, setMensaje] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    try {
      const lista = await listarDisponibilidad();
      setActuales(lista);
      // Prellenar el formulario con lo ya guardado.
      const base = ESTADO_INICIAL();
      lista.forEach((d) => {
        if (base[d.diaSemana]) {
          base[d.diaSemana] = { activo: true, inicio: d.horaInicio, fin: d.horaFin };
        }
      });
      setDias(base);
    } catch {
      setActuales([]);
    }
  };

  useEffect(() => {
    cargar();
  }, []);

  const setDia = (dia, campo, valor) =>
    setDias((d) => ({ ...d, [dia]: { ...d[dia], [campo]: valor } }));

  const guardar = async () => {
    setMensaje('');
    const seleccionados = DIAS.filter((d) => dias[d].activo).map((d) => ({
      diaSemana: d,
      horaInicio: dias[d].inicio,
      horaFin: dias[d].fin,
    }));

    if (seleccionados.length === 0) {
      setMensaje('Selecciona al menos un día para guardar.');
      return;
    }
    if (seleccionados.some((d) => d.horaInicio >= d.horaFin)) {
      setMensaje('La hora de inicio debe ser menor que la de fin.');
      return;
    }

    setGuardando(true);
    try {
      const r = await guardarDisponibilidad(seleccionados);
      if (r.rechazados === 0) {
        setMensaje(`Disponibilidad guardada: ${r.guardados} día(s).`);
      } else {
        setMensaje(
          `Guardados: ${r.guardados}. Rechazados: ${r.rechazados} ` +
            '(rango inválido o citas pendientes en ese día).',
        );
      }
      cargar();
    } catch (err) {
      setMensaje(err.response?.data?.error || 'No se pudo guardar la disponibilidad.');
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div>
      <h1 className="panel__title">Mi Disponibilidad</h1>

      {mensaje && <div className="modal__error" style={{ marginBottom: 16 }}>{mensaje}</div>}

      {/* Disponibilidad actual */}
      <div className="card-section">
        <h2 className="card-section__title">Horarios registrados</h2>
        <div className="table-wrap">
          <table className="table">
            <thead>
              <tr>
                <th>Día</th>
                <th>Inicio</th>
                <th>Fin</th>
              </tr>
            </thead>
            <tbody>
              {actuales.length === 0 ? (
                <tr>
                  <td className="table__empty" colSpan={3}>Sin horarios registrados</td>
                </tr>
              ) : (
                actuales.map((d) => (
                  <tr key={d.id}>
                    <td>{d.diaSemana}</td>
                    <td>{d.horaInicio}</td>
                    <td>{d.horaFin}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Editar disponibilidad */}
      <div className="card-section">
        <h2 className="card-section__title">Configurar días</h2>
        <p className="panel__hint" style={{ marginBottom: 12 }}>
          Marca los días que atiendes y su rango horario. Guardar regenera los turnos de 30 min.
        </p>
        {DIAS.map((dia) => (
          <div key={dia} className="row-inline" style={{ marginBottom: 10 }}>
            <label className="toolbar__label" style={{ minWidth: 130 }}>
              <input
                type="checkbox"
                checked={dias[dia].activo}
                onChange={(e) => setDia(dia, 'activo', e.target.checked)}
              />
              {dia}
            </label>
            <select
              className="toolbar__select"
              value={dias[dia].inicio}
              disabled={!dias[dia].activo}
              onChange={(e) => setDia(dia, 'inicio', e.target.value)}
            >
              {HORAS.map((h) => <option key={h} value={h}>{h}</option>)}
            </select>
            <span style={{ alignSelf: 'center', color: '#64748b' }}>—</span>
            <select
              className="toolbar__select"
              value={dias[dia].fin}
              disabled={!dias[dia].activo}
              onChange={(e) => setDia(dia, 'fin', e.target.value)}
            >
              {HORAS.map((h) => <option key={h} value={h}>{h}</option>)}
            </select>
          </div>
        ))}

        <button
          className="btn btn--primary"
          style={{ marginTop: 8 }}
          onClick={guardar}
          disabled={guardando}
        >
          {guardando ? 'Guardando…' : 'Guardar disponibilidad'}
        </button>
      </div>
    </div>
  );
}

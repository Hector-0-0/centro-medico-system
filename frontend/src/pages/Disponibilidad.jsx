import { useEffect, useState } from 'react';
import {
  listarDisponibilidad,
  guardarDisponibilidad,
} from '../services/disponibilidadService';
import { mensajeError } from '../services/api';
import { useDialog } from '../components/Dialog';

const DIAS = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes'];

const HORAS = [];
for (let m = 7 * 60; m <= 20 * 60; m += 30) {
  HORAS.push(`${String(Math.floor(m / 60)).padStart(2, '0')}:${String(m % 60).padStart(2, '0')}`);
}

const ESTADO_INICIAL = () =>
  DIAS.reduce(
    (acc, d) => ({ ...acc, [d]: { activo: false, inicio: '08:00', fin: '09:00' } }),
    {},
  );

export default function Disponibilidad() {
  const [actuales, setActuales] = useState([]);
  const [dias, setDias] = useState(ESTADO_INICIAL);
  const [guardando, setGuardando] = useState(false);
  const [cargando, setCargando] = useState(true);
  const [errorCarga, setErrorCarga] = useState('');
  const { alerta } = useDialog();

  const cargar = async () => {
    setCargando(true);
    setErrorCarga('');
    try {
      const lista = await listarDisponibilidad();
      setActuales(lista);
      const base = ESTADO_INICIAL();
      const porDia = {};
      lista.forEach((d) => {
        if (!porDia[d.diaSemana]) porDia[d.diaSemana] = [];
        porDia[d.diaSemana].push(d);
      });
      DIAS.forEach((dia) => {
        if (porDia[dia]) {
          const ultimo = porDia[dia][porDia[dia].length - 1];
          base[dia] = { activo: true, inicio: ultimo.horaInicio, fin: ultimo.horaFin };
        }
      });
      setDias(base);
    } catch (err) {
      setActuales([]);
      setErrorCarga(mensajeError(err, 'No se pudo cargar la disponibilidad.'));
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => { cargar(); }, []);

  const setDia = (dia, campo, valor) =>
    setDias((d) => ({ ...d, [dia]: { ...d[dia], [campo]: valor } }));

  const guardar = async () => {
    const invalido = DIAS.some((d) => dias[d].activo && dias[d].inicio >= dias[d].fin);
    if (invalido) {
      await alerta('En los días que atiendes, la hora de inicio debe ser menor que la de fin.');
      return;
    }

    const payload = DIAS.map((d) => ({
      diaSemana: d,
      horaInicio: dias[d].inicio,
      horaFin: dias[d].fin,
      activo: dias[d].activo,
    }));

    setGuardando(true);
    try {
      const r = await guardarDisponibilidad(payload);
      const rechazos = r.detalleRechazados || [];
      if (rechazos.length === 0) {
        await alerta('Disponibilidad actualizada correctamente.', { titulo: 'Listo' });
      } else {
        const detalle = rechazos.map((x) => `• ${x.diaSemana}: ${x.motivo}`).join('\n');
        await alerta(
          `Algunos días no se pudieron aplicar:\n${detalle}`,
          { titulo: 'Disponibilidad parcial' },
        );
      }
      cargar();
    } catch (err) {
      await alerta(mensajeError(err, 'No se pudo guardar la disponibilidad.'));
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div>
      <h1 className="panel__title">Mi Disponibilidad</h1>
      <p className="panel__hint" style={{ marginBottom: 16 }}>
        Marca los días que atiendes y configura tu rango horario.
        Guardar regenera los turnos de 30 min. No podrás modificar un día con citas pendientes.
      </p>

      <div className="cards-container">
        {DIAS.map((dia) => {
          const info = dias[dia];
          return (
            <div key={dia} className={`card-dia ${!info.activo ? 'card-dia--collapsed' : ''}`}>
              <div className="card-dia__header">
                <label className="card-dia__checkbox">
                  <input
                    type="checkbox"
                    checked={info.activo}
                    onChange={(e) => setDia(dia, 'activo', e.target.checked)}
                  />
                  <span className="card-dia__nombre">{dia}</span>
                </label>
              </div>

              {info.activo && (
                <div className="card-dia__bloques">
                  <div className="card-dia__bloque">
                    <select
                      className="toolbar__select"
                      value={info.inicio}
                      onChange={(e) => setDia(dia, 'inicio', e.target.value)}
                    >
                      {HORAS.map((h) => <option key={h} value={h}>{h}</option>)}
                    </select>
                    <span className="card-dia__separador">—</span>
                    <select
                      className="toolbar__select"
                      value={info.fin}
                      onChange={(e) => setDia(dia, 'fin', e.target.value)}
                    >
                      {HORAS.map((h) => <option key={h} value={h}>{h}</option>)}
                    </select>
                  </div>
                  {info.activo && info.inicio >= info.fin && (
                    <div className="card-dia__error">⚠️ La hora de inicio debe ser menor que la de fin</div>
                  )}
                </div>
              )}
            </div>
          );
        })}
      </div>

      <div className="form-actions" style={{ marginTop: 16 }}>
        <button className="btn btn--primary" onClick={guardar} disabled={guardando}>
          {guardando ? 'Guardando…' : 'Guardar disponibilidad'}
        </button>
      </div>
    </div>
  );
}

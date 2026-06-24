import { useEffect, useState } from 'react';
import { listarCitasEstudiante, obtenerAtencion } from '../services/citaService';

/** Mis Citas del estudiante — réplica del HistorialPanel del desktop. */
export default function MisCitas() {
  const [lista, setLista] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [detalle, setDetalle] = useState(null); // { cita, atencion }

  useEffect(() => {
    listarCitasEstudiante()
      .then(setLista)
      .catch(() => setLista([]));
  }, []);

  const q = busqueda.trim().toLowerCase();
  const filtradas = q
    ? lista.filter(
        (c) =>
          (c.especialidad || '').toLowerCase().includes(q) ||
          (c.nombreDoctor || '').toLowerCase().includes(q) ||
          (c.motivo || '').toLowerCase().includes(q) ||
          (c.estado || '').toLowerCase().includes(q),
      )
    : lista;

  const verDetalle = async (cita) => {
    try {
      const atencion = await obtenerAtencion(cita.id);
      setDetalle({ cita, atencion });
    } catch {
      setDetalle({ cita, atencion: null });
    }
  };

  return (
    <div>
      <h1 className="panel__title">Mis Citas</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por especialidad, médico, motivo o estado..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      <p className="panel__hint" style={{ marginBottom: 12 }}>
        Doble clic en una cita para ver el detalle de la atención.
      </p>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Especialidad</th>
              <th>Médico</th>
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
                <tr key={c.id} onDoubleClick={() => verDetalle(c)}>
                  <td>{c.especialidad || '—'}</td>
                  <td>{c.nombreDoctor || c.idDoctor}</td>
                  <td>{c.diaSemana || '—'}</td>
                  <td>{c.horaInicio}{c.horaFin ? ` - ${c.horaFin}` : ''}</td>
                  <td>{c.motivo}</td>
                  <td>{c.estado}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {detalle && (
        <DetalleModal data={detalle} onClose={() => setDetalle(null)} />
      )}
    </div>
  );
}

function DetalleModal({ data, onClose }) {
  const { cita, atencion } = data;
  return (
    <div className="modal__overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal__header">Detalle de la cita</div>
        <div className="modal__body">
          <p className="perfil__linea">
            <strong>{cita.especialidad}</strong> — {cita.nombreDoctor || cita.idDoctor}
          </p>
          <p className="perfil__linea">
            {cita.diaSemana} {cita.horaInicio}{cita.horaFin ? ` - ${cita.horaFin}` : ''} · {cita.estado}
          </p>

          {!atencion || !atencion.atendida ? (
            <p className="panel__hint">Cita no atendida aún.</p>
          ) : (
            <>
              <h3 className="card-section__title" style={{ marginTop: 8 }}>Diagnósticos</h3>
              {atencion.diagnosticos.length === 0 ? (
                <p className="panel__hint">(Sin diagnóstico registrado)</p>
              ) : (
                <ul style={{ margin: 0, paddingLeft: 18 }}>
                  {atencion.diagnosticos.map((d, i) => (
                    <li key={i} style={{ marginBottom: 4 }}>
                      <strong>{d.codigo}</strong> — {d.descripcion}
                      {d.observacion ? ` (${d.observacion})` : ''}
                    </li>
                  ))}
                </ul>
              )}

              <h3 className="card-section__title" style={{ marginTop: 12 }}>Comentarios</h3>
              <p className="perfil__linea">{atencion.comentarios || '(Sin comentarios)'}</p>
            </>
          )}
        </div>
        <div className="modal__footer">
          <button className="btn btn--primary" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  );
}

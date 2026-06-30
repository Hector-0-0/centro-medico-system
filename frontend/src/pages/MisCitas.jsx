import { useState } from 'react';
import { listarCitasEstudiante, obtenerAtencion, cancelarCita } from '../services/citaService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';
import { useDialog } from '../components/Dialog';

// Pestañas por estado de la cita.
const TABS = [
  { key: 'PENDIENTE', label: 'Pendientes' },
  { key: 'ATENDIDA',  label: 'Atendidas' },
  { key: 'CANCELADA', label: 'Canceladas' },
];

/** Mis Citas del estudiante — réplica del HistorialPanel del desktop, con pestañas. */
export default function MisCitas() {
  const { datos, cargando, error, recargar } = useCargar(listarCitasEstudiante);
  const lista = datos || [];
  const [tab, setTab] = useState('PENDIENTE');
  const [busqueda, setBusqueda] = useState('');
  const [detalle, setDetalle] = useState(null); // { cita, atencion }
  const { alerta, confirmar } = useDialog();

  const cuenta = (estado) => lista.filter((c) => c.estado === estado).length;

  const q = busqueda.trim().toLowerCase();
  const filtradas = lista
    .filter((c) => c.estado === tab)
    .filter((c) =>
      !q ||
      (c.especialidad || '').toLowerCase().includes(q) ||
      (c.nombreDoctor || '').toLowerCase().includes(q) ||
      (c.motivo || '').toLowerCase().includes(q),
    );

  const verDetalle = async (cita) => {
    try {
      const atencion = await obtenerAtencion(cita.id);
      setDetalle({ cita, atencion });
    } catch {
      setDetalle({ cita, atencion: null });
    }
  };

  const cancelar = async (cita) => {
    if (!(await confirmar(
      `¿Cancelar tu cita de ${cita.especialidad || ''} con ${cita.nombreDoctor || cita.idDoctor}?`,
      { peligro: true, textoOk: 'Cancelar cita', textoCancelar: 'Volver' },
    ))) return;
    try {
      await cancelarCita(cita.id);
      recargar();
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo cancelar la cita.'));
    }
  };

  const esPendiente = tab === 'PENDIENTE';

  return (
    <div>
      <h1 className="panel__title">Mis Citas</h1>

      <div className="toolbar">
        {TABS.map((t) => (
          <button
            key={t.key}
            className={`btn ${tab === t.key ? 'btn--primary' : 'btn--ghost'}`}
            onClick={() => setTab(t.key)}
          >
            {t.label} ({cuenta(t.key)})
          </button>
        ))}
        <input
          className="toolbar__search"
          placeholder="Buscar por especialidad, médico o motivo..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      <p className="panel__hint" style={{ marginBottom: 12 }}>
        Doble clic (o Enter) en una cita para ver el detalle de la atención.
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
              {esPendiente && <th></th>}
            </tr>
          </thead>
          <tbody>
            {cargando || error || filtradas.length === 0 ? (
              <FilaTablaEstado
                colSpan={esPendiente ? 7 : 6}
                cargando={cargando}
                error={error}
                onReintentar={recargar}
                vacio="No tienes citas en esta categoría"
              />
            ) : (
              filtradas.map((c) => (
                <tr
                  key={c.id}
                  onDoubleClick={() => verDetalle(c)}
                  tabIndex={0}
                  onKeyDown={(ev) => {
                    if (ev.key === 'Enter') {
                      ev.preventDefault();
                      verDetalle(c);
                    }
                  }}
                >
                  <td>{c.especialidad || '—'}</td>
                  <td>{c.nombreDoctor || c.idDoctor}</td>
                  <td>{c.diaSemana || '—'}</td>
                  <td>{c.horaInicio}{c.horaFin ? ` - ${c.horaFin}` : ''}</td>
                  <td>{c.motivo}</td>
                  <td>{c.estado}</td>
                  {esPendiente && (
                    <td>
                      <button
                        className="btn btn--danger btn--sm"
                        onClick={(e) => { e.stopPropagation(); cancelar(c); }}
                      >
                        Cancelar
                      </button>
                    </td>
                  )}
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
  const descargarPdf = async () => {
    try {
      const response = await fetch(`/api/citas/${cita.id}/receta-pdf`, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      if (!response.ok) throw new Error('Error al descargar PDF');
      const blob = await response.blob();
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `receta-cita-${cita.id}.pdf`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch {
      // Silencioso — el modal de error nativo no es necesario aquí
    }
  };

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

              {atencion.idReceta && (
                <div style={{ marginTop: 12 }}>
                  <button className="btn btn--primary" onClick={descargarPdf}>
                    📄 Descargar Receta (PDF)
                  </button>
                </div>
              )}
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

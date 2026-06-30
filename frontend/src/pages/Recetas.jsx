import { useState } from 'react';
import {
  listarRecetasPendientes,
  obtenerDetalleReceta,
  entregarReceta,
} from '../services/recetaService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';
import { useDialog } from '../components/Dialog';

export default function Recetas() {
  const { datos, cargando, error, recargar } = useCargar(listarRecetasPendientes);
  const lista = datos || [];
  const [busqueda, setBusqueda] = useState('');
  const [seleccion, setSeleccion] = useState(null);
  const [detalle, setDetalle] = useState(null);
  const { alerta, confirmar } = useDialog();

  const cargar = () => recargar().then(() => { setSeleccion(null); setDetalle(null); });

  const q = busqueda.trim().toLowerCase();
  const filtradas = q
    ? lista.filter(
        (r) =>
          (r.nombrePaciente || '').toLowerCase().includes(q) ||
          String(r.id).includes(q),
      )
    : lista;

  const verDetalle = async (id) => {
    try {
      const d = await obtenerDetalleReceta(id);
      setDetalle(d);
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo cargar el detalle.'));
    }
  };

  return (
    <div>
      <h1 className="panel__title">Recetas Pendientes</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por paciente o N° de receta..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      <p className="panel__hint" style={{ marginBottom: 12 }}>
        Doble clic (o Enter) en una receta para ver detalle y confirmar entrega.
      </p>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>ID Receta</th>
              <th>Paciente</th>
              <th>Cita</th>
              <th>Diagnóstico</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {cargando || error || filtradas.length === 0 ? (
              <FilaTablaEstado
                colSpan={5}
                cargando={cargando}
                error={error}
                onReintentar={recargar}
                vacio="Sin recetas pendientes"
              />
            ) : (
              filtradas.map((r) => (
                <tr
                  key={r.id}
                  className={seleccion === r.id ? 'is-selected' : ''}
                  onClick={() => setSeleccion(r.id)}
                  onDoubleClick={() => verDetalle(r.id)}
                  tabIndex={0}
                  aria-selected={seleccion === r.id}
                  onKeyDown={(ev) => {
                    if (ev.key === 'Enter' || ev.key === ' ') {
                      ev.preventDefault();
                      setSeleccion(r.id);
                    }
                    if (ev.key === 'Enter' && (ev.ctrlKey || ev.metaKey)) {
                      ev.preventDefault();
                      verDetalle(r.id);
                    }
                  }}
                >
                  <td>{r.id}</td>
                  <td>{r.nombrePaciente || '—'}</td>
                  <td>{String(r.idCita).padStart(4, '0')}</td>
                  <td>{r.diagnostico || '—'}</td>
                  <td>{r.estado}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {detalle && (
        <DetalleRecetaModal data={detalle} onClose={() => setDetalle(null)} onEntrega={cargar} />
      )}
    </div>
  );
}

function DetalleRecetaModal({ data, onClose, onEntrega }) {
  const { alerta, confirmar } = useDialog();
  const [entregando, setEntregando] = useState(false);

  const colorStock = (actual, despues) => {
    if (despues < 0) return '#b91c1c';
    if (despues === 0) return '#b45309';
    if (actual <= 5) return '#b45309';
    return '#15803d';
  };

  const confirmarEntrega = async () => {
    if (!(await confirmar(
      `¿Confirmar entrega de la receta #${data.id} a ${data.nombrePaciente}?`,
      { textoOk: 'Confirmar entrega' },
    ))) return;
    setEntregando(true);
    try {
      await entregarReceta(data.id);
      onEntrega();
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo confirmar la entrega.'));
    } finally {
      setEntregando(false);
    }
  };

  const todosSinStock = data.items.length > 0 && data.items.every((i) => i.stockActual === 0);

  return (
    <div className="modal__overlay" onClick={onClose}>
      <div className="modal modal--wide" onClick={(e) => e.stopPropagation()}>
        <div className="modal__header">Detalle de Receta #{data.id}</div>
        <div className="modal__body">
          <p className="perfil__linea">
            <strong>Paciente:</strong> {data.nombrePaciente}
          </p>
          <p className="perfil__linea">
            <strong>Cita:</strong> #{String(data.idCita).padStart(4, '0')}
          </p>
          {data.diagnostico && (
            <p className="perfil__linea">
              <strong>Diagnóstico:</strong> {data.diagnostico}
            </p>
          )}

          <h3 className="card-section__title" style={{ marginTop: 8 }}>Medicamentos</h3>
          <div className="table-wrap">
            <table className="table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Medicamento</th>
                  <th>Dosis</th>
                  <th>Duración</th>
                  <th>Stock actual</th>
                  <th>Stock después</th>
                </tr>
              </thead>
              <tbody>
                {data.items.length === 0 ? (
                  <tr>
                    <td className="table__empty" colSpan={6}>Sin medicamentos</td>
                  </tr>
                ) : (
                  data.items.map((item) => (
                    <tr key={item.idMedicamento}>
                      <td>{item.idMedicamento}</td>
                      <td>{item.nombre}</td>
                      <td>{item.dosis}</td>
                      <td>{item.duracion}</td>
                      <td style={{ fontWeight: 700, color: item.stockActual === 0 ? '#b91c1c' : '#1e293b' }}>
                        {item.stockActual}
                        {item.stockActual === 0 && ' (AGOTADO)'}
                      </td>
                      <td style={{ fontWeight: 700, color: colorStock(item.stockActual, item.stockDespues) }}>
                        {item.stockDespues}
                        {item.stockDespues <= 0 && ' (SIN STOCK)'}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
        <div className="modal__footer">
          <button className="btn btn--ghost" onClick={onClose}>Cancelar</button>
          <button
            className="btn btn--primary"
            onClick={confirmarEntrega}
            disabled={entregando || todosSinStock}
            title={todosSinStock ? 'Todos los medicamentos están agotados' : ''}
          >
            {entregando ? 'Entregando…' : 'Confirmar Entrega'}
          </button>
        </div>
      </div>
    </div>
  );
}

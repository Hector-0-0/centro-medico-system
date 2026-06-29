import { useState } from 'react';
import {
  listarRecetasPendientes,
  entregarReceta,
} from '../services/recetaService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';
import { useDialog } from '../components/Dialog';

/** Recetas Pendientes — réplica del FarmaciaRecetasPanel del desktop. */
export default function Recetas() {
  const { datos, cargando, error, recargar } = useCargar(listarRecetasPendientes);
  const lista = datos || [];
  const [busqueda, setBusqueda] = useState('');
  const [seleccion, setSeleccion] = useState(null);
  const { alerta, confirmar } = useDialog();

  const cargar = () => recargar().then(() => setSeleccion(null));

  const q = busqueda.trim().toLowerCase();
  const filtradas = q
    ? lista.filter(
        (r) =>
          (r.nombrePaciente || '').toLowerCase().includes(q) ||
          String(r.id).includes(q),
      )
    : lista;

  const confirmarEntrega = async () => {
    if (!seleccion) {
      alerta('Selecciona una receta para confirmar la entrega.');
      return;
    }
    if (!(await confirmar(`¿Confirmar entrega de la receta #${seleccion}?`, { textoOk: 'Confirmar' }))) return;
    try {
      await entregarReceta(seleccion);
      cargar();
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo confirmar la entrega.'));
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
        <button className="btn btn--primary" onClick={confirmarEntrega}>
          Confirmar Entrega
        </button>
      </div>

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
                  tabIndex={0}
                  aria-selected={seleccion === r.id}
                  onKeyDown={(ev) => {
                    if (ev.key === 'Enter' || ev.key === ' ') {
                      ev.preventDefault();
                      setSeleccion(r.id);
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
    </div>
  );
}

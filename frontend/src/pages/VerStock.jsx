import { useState } from 'react';
import { listarMedicamentos } from '../services/medicamentoService';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';

/** Ver Stock — réplica del MedicamentoPanel (read-only) del desktop. */
export default function VerStock() {
  const { datos, cargando, error, recargar } = useCargar(listarMedicamentos);
  const lista = datos || [];
  const [busqueda, setBusqueda] = useState('');

  const q = busqueda.trim().toLowerCase();
  const filtrados = q
    ? lista.filter(
        (m) =>
          m.nombre.toLowerCase().includes(q) ||
          m.id.toLowerCase().includes(q) ||
          (m.tipo || '').toLowerCase().includes(q),
      )
    : lista;

  return (
    <div>
      <h1 className="panel__title">Stock de Medicamentos</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por nombre, código o tipo..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Dosis (mg)</th>
              <th>Stock</th>
              <th>Tipo</th>
            </tr>
          </thead>
          <tbody>
            {cargando || error || filtrados.length === 0 ? (
              <FilaTablaEstado
                colSpan={5}
                cargando={cargando}
                error={error}
                onReintentar={recargar}
                vacio="Sin medicamentos"
              />
            ) : (
              filtrados.map((m) => (
                <tr key={m.id}>
                  <td>{m.id}</td>
                  <td>{m.nombre}</td>
                  <td>{m.dosisMg != null ? m.dosisMg : '—'}</td>
                  <td style={{ color: m.stock === 0 ? '#b91c1c' : undefined, fontWeight: m.stock === 0 ? 700 : undefined }}>
                    {m.stock}
                  </td>
                  <td>{m.tipo}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

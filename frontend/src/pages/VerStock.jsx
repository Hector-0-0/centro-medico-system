import { useEffect, useState } from 'react';
import { listarMedicamentos } from '../services/medicamentoService';

/** Ver Stock — réplica del MedicamentoPanel (read-only) del desktop. */
export default function VerStock() {
  const [lista, setLista] = useState([]);
  const [busqueda, setBusqueda] = useState('');

  useEffect(() => {
    listarMedicamentos()
      .then(setLista)
      .catch(() => setLista([]));
  }, []);

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
            {filtrados.length === 0 ? (
              <tr>
                <td className="table__empty" colSpan={5}>Sin medicamentos</td>
              </tr>
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

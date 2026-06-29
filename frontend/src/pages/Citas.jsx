import { useMemo, useState } from 'react';
import { listarCitas } from '../services/citaService';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';

const ESTADOS = ['Todos', 'PENDIENTE', 'ATENDIDA', 'CANCELADA'];

/** Todas las Citas — réplica del AdminCitasPanel del desktop (solo lectura + filtros). */
export default function Citas() {
  const { datos, cargando, error, recargar } = useCargar(listarCitas);
  const lista = datos || [];
  const [especialidad, setEspecialidad] = useState('Todas');
  const [estado, setEstado] = useState('Todos');

  // Especialidades disponibles según las citas cargadas.
  const especialidades = useMemo(() => {
    const set = new Set(lista.map((c) => c.especialidad).filter(Boolean));
    return ['Todas', ...[...set].sort()];
  }, [lista]);

  const filtradas = lista.filter(
    (c) =>
      (especialidad === 'Todas' || c.especialidad === especialidad) &&
      (estado === 'Todos' || c.estado === estado),
  );

  return (
    <div>
      <h1 className="panel__title">Todas las Citas</h1>

      <div className="toolbar">
        <label className="toolbar__label">
          Especialidad
          <select
            className="toolbar__select"
            value={especialidad}
            onChange={(e) => setEspecialidad(e.target.value)}
          >
            {especialidades.map((esp) => (
              <option key={esp} value={esp}>{esp}</option>
            ))}
          </select>
        </label>

        <label className="toolbar__label">
          Estado
          <select
            className="toolbar__select"
            value={estado}
            onChange={(e) => setEstado(e.target.value)}
          >
            {ESTADOS.map((es) => (
              <option key={es} value={es}>{es}</option>
            ))}
          </select>
        </label>
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Paciente</th>
              <th>Médico</th>
              <th>Motivo</th>
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
                vacio="Sin citas"
              />
            ) : (
              filtradas.map((c) => (
                <tr key={c.id}>
                  <td>{String(c.id).padStart(4, '0')}</td>
                  <td>{c.nombreEstudiante || c.idEstudiante}</td>
                  <td>{c.nombreDoctor || c.idDoctor}</td>
                  <td>{c.motivo}</td>
                  <td>{c.estado}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

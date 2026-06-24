import { useEffect, useState } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import {
  buscarCie,
  medicamentosConStock,
  atenderCita,
} from '../services/atencionService';
import { listarMisCitas } from '../services/citaService';
import { mensajeError } from '../services/api';

/** Atender Cita — réplica del AtenderCitaPanel del desktop (consulta médica). */
export default function AtenderCita() {
  const { id } = useParams();
  const idCita = Number(id);
  const navigate = useNavigate();
  const location = useLocation();

  const [cita, setCita] = useState(location.state?.cita || null);
  const [comentarios, setComentarios] = useState('');

  // Diagnósticos CIE
  const [busqueda, setBusqueda] = useState('');
  const [resultados, setResultados] = useState([]);
  const [diagnosticos, setDiagnosticos] = useState([]);

  // Receta
  const [medicamentos, setMedicamentos] = useState([]);
  const [medSel, setMedSel] = useState('');
  const [dosis, setDosis] = useState('');
  const [duracion, setDuracion] = useState('');
  const [receta, setReceta] = useState([]);

  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  // Si se entró directo por URL, recuperar la cita desde "Mis Citas".
  useEffect(() => {
    if (!cita) {
      listarMisCitas()
        .then((cs) => setCita(cs.find((c) => c.id === idCita) || null))
        .catch(() => {});
    }
  }, [cita, idCita]);

  // Medicamentos con stock para el combo de receta.
  useEffect(() => {
    medicamentosConStock()
      .then(setMedicamentos)
      .catch(() => setMedicamentos([]));
  }, []);

  // Búsqueda CIE en vivo (mín. 2 caracteres, con debounce).
  useEffect(() => {
    const q = busqueda.trim();
    if (q.length < 2) {
      setResultados([]);
      return;
    }
    const t = setTimeout(() => {
      buscarCie(q).then(setResultados).catch(() => setResultados([]));
    }, 300);
    return () => clearTimeout(t);
  }, [busqueda]);

  const agregarDiagnostico = (cie) => {
    if (diagnosticos.some((d) => d.idCie === cie.id)) return;
    setDiagnosticos((ds) => [
      ...ds,
      { idCie: cie.id, codigo: cie.codigo, descripcion: cie.descripcion, observacion: '' },
    ]);
    setBusqueda('');
    setResultados([]);
  };

  const editarObservacion = (idCie, valor) =>
    setDiagnosticos((ds) =>
      ds.map((d) => (d.idCie === idCie ? { ...d, observacion: valor } : d)),
    );

  const quitarDiagnostico = (idCie) =>
    setDiagnosticos((ds) => ds.filter((d) => d.idCie !== idCie));

  const agregarMedicamento = () => {
    if (!medSel) {
      setError('Selecciona un medicamento.');
      return;
    }
    if (!dosis.trim() || !duracion.trim()) {
      setError('Ingresa la dosis y la duración del medicamento.');
      return;
    }
    if (receta.some((r) => r.idMedicamento === medSel)) {
      setError('Ese medicamento ya está en la receta.');
      return;
    }
    const med = medicamentos.find((m) => m.id === medSel);
    setReceta((rs) => [
      ...rs,
      { idMedicamento: med.id, nombre: med.nombre, dosis: dosis.trim(), duracion: duracion.trim() },
    ]);
    setMedSel('');
    setDosis('');
    setDuracion('');
    setError('');
  };

  const quitarMedicamento = (idMed) =>
    setReceta((rs) => rs.filter((r) => r.idMedicamento !== idMed));

  const guardar = async () => {
    setError('');
    if (diagnosticos.length === 0) {
      setError('Debe agregar al menos un diagnóstico CIE.');
      return;
    }
    setGuardando(true);
    try {
      await atenderCita(idCita, {
        comentarios: comentarios.trim(),
        diagnosticos: diagnosticos.map((d) => ({ idCie: d.idCie, observacion: d.observacion })),
        receta: receta.map((r) => ({
          idMedicamento: r.idMedicamento,
          dosis: r.dosis,
          duracion: r.duracion,
        })),
      });
      navigate('/citas-medico');
    } catch (err) {
      setError(mensajeError(err, 'No se pudo guardar la consulta.'));
    } finally {
      setGuardando(false);
    }
  };

  const paciente = cita ? cita.nombreEstudiante || cita.idEstudiante : `Cita #${idCita}`;

  return (
    <div>
      <h1 className="panel__title">Atender Cita</h1>
      <p className="panel__hint" style={{ marginBottom: 16 }}>
        Paciente: <strong>{paciente}</strong>
        {cita?.motivo ? ` — ${cita.motivo}` : ''}
      </p>

      {error && <div className="modal__error" style={{ marginBottom: 16 }}>{error}</div>}

      {/* ── Diagnósticos CIE ── */}
      <div className="card-section">
        <h2 className="card-section__title">Diagnósticos (CIE-10)</h2>
        <input
          className="toolbar__search"
          style={{ width: '100%' }}
          placeholder="Buscar código o enfermedad (mín. 2 letras)..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        {busqueda.trim().length >= 2 && (
          <ul className="cie-results">
            {resultados.length === 0 ? (
              <li className="cie-results__empty">Sin resultados</li>
            ) : (
              resultados.map((cie) => (
                <li key={cie.id}>
                  <span>
                    <strong>{cie.codigo}</strong> — {cie.descripcion}
                  </span>
                  <button className="btn btn--ghost" onClick={() => agregarDiagnostico(cie)}>
                    Agregar
                  </button>
                </li>
              ))
            )}
          </ul>
        )}

        <div className="table-wrap" style={{ marginTop: 12 }}>
          <table className="table">
            <thead>
              <tr>
                <th>Código</th>
                <th>Descripción</th>
                <th>Observación</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {diagnosticos.length === 0 ? (
                <tr>
                  <td className="table__empty" colSpan={4}>Agrega al menos un diagnóstico</td>
                </tr>
              ) : (
                diagnosticos.map((d) => (
                  <tr key={d.idCie}>
                    <td>{d.codigo}</td>
                    <td>{d.descripcion}</td>
                    <td>
                      <input
                        className="field__input"
                        style={{ width: '100%' }}
                        value={d.observacion}
                        onChange={(e) => editarObservacion(d.idCie, e.target.value)}
                        placeholder="Observación (opcional)"
                      />
                    </td>
                    <td>
                      <button className="btn btn--danger" onClick={() => quitarDiagnostico(d.idCie)}>
                        Quitar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* ── Receta ── */}
      <div className="card-section">
        <h2 className="card-section__title">Receta (opcional)</h2>
        <div className="row-inline">
          <label className="field">
            <span className="field__label">Medicamento</span>
            <select
              className="toolbar__select"
              value={medSel}
              onChange={(e) => setMedSel(e.target.value)}
            >
              <option value="">Selecciona...</option>
              {medicamentos.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.nombre} (stock: {m.stock})
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span className="field__label">Dosis</span>
            <input className="field__input" value={dosis} onChange={(e) => setDosis(e.target.value)} />
          </label>
          <label className="field">
            <span className="field__label">Duración</span>
            <input className="field__input" value={duracion} onChange={(e) => setDuracion(e.target.value)} />
          </label>
          <button className="btn btn--ghost" onClick={agregarMedicamento}>Agregar</button>
        </div>

        <div className="table-wrap" style={{ marginTop: 12 }}>
          <table className="table">
            <thead>
              <tr>
                <th>Medicamento</th>
                <th>Dosis</th>
                <th>Duración</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {receta.length === 0 ? (
                <tr>
                  <td className="table__empty" colSpan={4}>Sin medicamentos</td>
                </tr>
              ) : (
                receta.map((r) => (
                  <tr key={r.idMedicamento}>
                    <td>{r.nombre}</td>
                    <td>{r.dosis}</td>
                    <td>{r.duracion}</td>
                    <td>
                      <button
                        className="btn btn--danger"
                        onClick={() => quitarMedicamento(r.idMedicamento)}
                      >
                        Quitar
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* ── Comentarios ── */}
      <div className="card-section">
        <h2 className="card-section__title">Comentarios</h2>
        <textarea
          className="textarea"
          value={comentarios}
          onChange={(e) => setComentarios(e.target.value)}
          placeholder="Indicaciones, observaciones de la consulta..."
        />
      </div>

      <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
        <button className="btn btn--ghost" onClick={() => navigate('/citas-medico')}>
          Cancelar
        </button>
        <button className="btn btn--primary" onClick={guardar} disabled={guardando}>
          {guardando ? 'Guardando…' : 'Guardar consulta'}
        </button>
      </div>
    </div>
  );
}

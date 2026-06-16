import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import ThOrden from '../components/ThOrden';
import { useOrden } from '../components/useOrden';
import { citaService, pacienteService, medicoService, historialService } from '../services/servicios';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());
const ESTADOS = ['TODOS', 'PENDIENTE', 'ATENDIDA', 'CANCELADA', 'REPROGRAMADA'];

const COLORES = { PENDIENTE: ['#fbece9','#711610'], ATENDIDA: ['#dcfce7','#166534'], CANCELADA: ['#fee2e2','#dc2626'], REPROGRAMADA: ['#fef3c7','#92400e'] };

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  badge: (estado) => ({ display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: COLORES[estado]?.[0] || '#f1e9e2', color: COLORES[estado]?.[1] || '#475569' }),
  btnAcc: (color) => ({ padding: '5px 10px', border: 'none', borderRadius: 6, fontSize: 11, fontWeight: 600, cursor: 'pointer', marginRight: 4, background: color === 'azul' ? '#fbece9' : color === 'rojo' ? '#fee2e2' : color === 'verde' ? '#dcfce7' : '#f1e9e2', color: color === 'azul' ? '#711610' : color === 'rojo' ? '#dc2626' : color === 'verde' ? '#166534' : '#475569' }),
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 500, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none' },
  select: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff' },
  textarea: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', resize: 'vertical', minHeight: 70, boxSizing: 'border-box' },
  fichaPac: { background: '#f9f5f0', border: '1px solid #e8ddd8', borderRadius: 10, padding: '12px 16px', marginBottom: 18, fontSize: 13, color: '#374151' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
};

const FORM_VACIO = { pacienteId: '', medicoId: '', fechaHora: '', motivo: '', consultorio: '' };

export default function Citas() {
  const [citas, setCitas] = useState([]);
  const [pacientes, setPacientes] = useState([]);
  const [medicos, setMedicos] = useState([]);
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState(FORM_VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);
  const [reprog, setReprog] = useState(null); // { cita, fechaHora }
  const [atencion, setAtencion] = useState(null); // { cita, diagnostico, tratamiento, receta, observaciones }
  const [buscar, setBuscar] = useState('');
  const [estadoFiltro, setEstadoFiltro] = useState('TODOS');
  const orden = useOrden();

  const cargar = async () => {
    try {
      const [resC, resP, resM] = await Promise.all([citaService.listar(), pacienteService.listar(), medicoService.listar()]);
      setCitas(resC.data); setPacientes(resP.data); setMedicos(resM.data);
    } catch (e) { console.error(e); }
  };

  useEffect(() => { cargar(); }, []);

  const guardar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      await citaService.crear({ ...form, pacienteId: Number(form.pacienteId), medicoId: Number(form.medicoId) });
      setModal(false); cargar();
    } catch (err) { setError(err.response?.data?.error || 'Error al guardar'); }
    finally { setGuardando(false); }
  };

  const cancelar = async (id) => {
    if (!window.confirm('¿Cancelar esta cita?')) return;
    try { await citaService.cancelar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error'); }
  };

  const abrirAtender = (cita) => {
    setError('');
    setAtencion({ cita, diagnostico: '', tratamiento: '', receta: '', observaciones: '' });
  };

  // Atender = registrar la consulta (diagnóstico/tratamiento/receta) — esto
  // marca la cita como ATENDIDA en el backend (igual que el desktop).
  const guardarAtencion = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      await historialService.registrar({
        citaId: atencion.cita.id,
        diagnostico: atencion.diagnostico,
        tratamiento: atencion.tratamiento,
        receta: atencion.receta,
        observaciones: atencion.observaciones,
      });
      setAtencion(null); cargar();
    } catch (err) {
      setError(err.response?.data?.error || err.response?.data?.message || 'Error al guardar la consulta');
    } finally { setGuardando(false); }
  };

  const reprogramar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      await citaService.reprogramar(reprog.cita.id, {
        pacienteId: reprog.cita.paciente?.id,
        medicoId:   reprog.cita.medico?.id,
        fechaHora:  reprog.fechaHora,
      });
      setReprog(null); cargar();
    } catch (err) { setError(err.response?.data?.error || 'Error al reprogramar'); }
    finally { setGuardando(false); }
  };

  const formatFecha = (f) => new Date(f).toLocaleString('es-PE', { dateStyle: 'short', timeStyle: 'short' });

  const visibles = orden.ordenar(
    citas.filter(c =>
      (estadoFiltro === 'TODOS' || c.estado === estadoFiltro) &&
      (!buscar || incluye(buscar, c.paciente?.nombre, c.paciente?.apellido, c.medico?.nombre, c.medico?.apellido, c.motivo))
    ),
    {
      fecha: c => new Date(c.fechaHora).getTime(),
      paciente: c => `${c.paciente?.nombre} ${c.paciente?.apellido}`,
      medico: c => `${c.medico?.nombre} ${c.medico?.apellido}`,
      motivo: c => c.motivo,
      estado: c => c.estado,
    }
  );

  return (
    <Layout titulo="Citas médicas">
      <div style={s.header}>
        <div style={s.titulo}>📅 Citas médicas</div>
        <button style={s.btnPrimario} onClick={() => { setForm(FORM_VACIO); setError(''); setModal(true); }}>+ Nueva cita</button>
      </div>

      <div style={{ display: 'flex', gap: 10, marginBottom: 16, alignItems: 'center' }}>
        <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por paciente, médico o motivo..." ancho={360} />
        <select style={{ ...s.select, width: 'auto' }} value={estadoFiltro} onChange={e => setEstadoFiltro(e.target.value)}>
          {ESTADOS.map(es => <option key={es} value={es}>{es === 'TODOS' ? 'Todos los estados' : es}</option>)}
        </select>
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <ThOrden label="Fecha y hora" col="fecha" orden={orden} style={s.th} />
            <ThOrden label="Paciente" col="paciente" orden={orden} style={s.th} />
            <ThOrden label="Médico" col="medico" orden={orden} style={s.th} />
            <ThOrden label="Motivo" col="motivo" orden={orden} style={s.th} />
            <ThOrden label="Estado" col="estado" orden={orden} style={s.th} />
            <th style={s.th}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {visibles.length === 0 ? (
            <tr><td colSpan={6} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No se encontraron citas</td></tr>
          ) : visibles.map(c => (
            <tr key={c.id}>
              <td style={s.td}>{formatFecha(c.fechaHora)}</td>
              <td style={s.td}>{c.paciente?.nombre} {c.paciente?.apellido}</td>
              <td style={s.td}>Dr. {c.medico?.nombre} {c.medico?.apellido}</td>
              <td style={s.td}>{c.motivo || '—'}</td>
              <td style={s.td}><span style={s.badge(c.estado)}>{c.estado}</span></td>
              <td style={s.td}>
                {(c.estado === 'PENDIENTE' || c.estado === 'REPROGRAMADA') && <>
                  <button style={s.btnAcc('verde')} onClick={() => abrirAtender(c)}>Atender</button>
                  <button style={s.btnAcc('azul')} onClick={() => setReprog({ cita: c, fechaHora: c.fechaHora?.slice(0, 16) || '' })}>Reprogramar</button>
                  <button style={s.btnAcc('rojo')} onClick={() => cancelar(c.id)}>Cancelar</button>
                </>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {atencion && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setAtencion(null)}>
          <div style={{ ...s.modal, width: 560, maxHeight: '92vh', overflowY: 'auto' }}>
            <div style={s.modalTit}>Atender cita — registrar consulta</div>
            <div style={s.fichaPac}>
              <strong>{atencion.cita.paciente?.nombre} {atencion.cita.paciente?.apellido}</strong>
              {atencion.cita.paciente?.dni ? ` · DNI ${atencion.cita.paciente.dni}` : ''}<br />
              Dr. {atencion.cita.medico?.nombre} {atencion.cita.medico?.apellido}
              {atencion.cita.medico?.especialidad?.nombre ? ` · ${atencion.cita.medico.especialidad.nombre}` : ''}
              {atencion.cita.paciente?.alergias ? <><br /><span style={{ color: '#b91c1c' }}>⚠️ Alergias: {atencion.cita.paciente.alergias}</span></> : ''}
            </div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardarAtencion}>
              <div style={s.campo}>
                <label style={s.label}>Diagnóstico *</label>
                <textarea style={s.textarea} required value={atencion.diagnostico}
                          onChange={e => setAtencion(a => ({ ...a, diagnostico: e.target.value }))}
                          placeholder="Diagnóstico de la consulta..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Tratamiento</label>
                <textarea style={s.textarea} value={atencion.tratamiento}
                          onChange={e => setAtencion(a => ({ ...a, tratamiento: e.target.value }))}
                          placeholder="Tratamiento indicado..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Receta</label>
                <textarea style={s.textarea} value={atencion.receta}
                          onChange={e => setAtencion(a => ({ ...a, receta: e.target.value }))}
                          placeholder="Medicamentos y dosis..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Observaciones</label>
                <textarea style={s.textarea} value={atencion.observaciones}
                          onChange={e => setAtencion(a => ({ ...a, observaciones: e.target.value }))}
                          placeholder="Observaciones adicionales..." />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setAtencion(null)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Guardando...' : 'Guardar consulta'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {reprog && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setReprog(null)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Reprogramar cita</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={reprogramar}>
              <div style={{ ...s.campo, fontSize: 14, color: '#374151' }}>
                <strong>{reprog.cita.paciente?.nombre} {reprog.cita.paciente?.apellido}</strong>
                {' '}con Dr. {reprog.cita.medico?.nombre} {reprog.cita.medico?.apellido}
              </div>
              <div style={s.campo}>
                <label style={s.label}>Nueva fecha y hora *</label>
                <input style={s.input} type="datetime-local" required value={reprog.fechaHora}
                       onChange={e => setReprog(r => ({ ...r, fechaHora: e.target.value }))} />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setReprog(null)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Guardando...' : 'Reprogramar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Nueva cita</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.campo}>
                <label style={s.label}>Paciente *</label>
                <select style={s.select} required value={form.pacienteId} onChange={e => setForm(f => ({ ...f, pacienteId: e.target.value }))}>
                  <option value="">— Seleccionar paciente —</option>
                  {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido} ({p.dni})</option>)}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Médico *</label>
                <select style={s.select} required value={form.medicoId} onChange={e => setForm(f => ({ ...f, medicoId: e.target.value }))}>
                  <option value="">— Seleccionar médico —</option>
                  {medicos.map(m => <option key={m.id} value={m.id}>Dr. {m.nombre} {m.apellido} — {m.especialidad?.nombre}</option>)}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Fecha y hora *</label>
                <input style={s.input} type="datetime-local" required value={form.fechaHora} onChange={e => setForm(f => ({ ...f, fechaHora: e.target.value }))} />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Motivo</label>
                <input style={s.input} placeholder="Motivo de la consulta" value={form.motivo} onChange={e => setForm(f => ({ ...f, motivo: e.target.value }))} />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Consultorio</label>
                <input style={s.input} placeholder="Ej: Consultorio 3" value={form.consultorio} onChange={e => setForm(f => ({ ...f, consultorio: e.target.value }))} />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Guardando...' : 'Crear cita'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

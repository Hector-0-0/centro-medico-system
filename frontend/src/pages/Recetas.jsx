import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import { recetaService, pacienteService, medicamentoService } from '../services/servicios';
import { getRol, getMedicoId } from '../services/authService';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 20 },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2', verticalAlign: 'top' },
  badge: (entregada) => ({ display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: entregada ? '#dcfce7' : '#fef3c7', color: entregada ? '#166534' : '#92400e' }),
  btnAcc: { padding: '5px 12px', border: 'none', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer', background: '#dcfce7', color: '#166534' },
  meds: { margin: 0, paddingLeft: 18, fontSize: 13, color: '#475569' },
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 620, maxHeight: '90vh', overflowY: 'auto', boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' },
  campo: { marginBottom: 16 },
  lineaHead: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  linea: { display: 'grid', gridTemplateColumns: '2fr 1.2fr 1.2fr 0.8fr auto', gap: 8, marginBottom: 8, alignItems: 'center' },
  btnLink: { background: 'none', border: 'none', color: '#711610', fontWeight: 600, cursor: 'pointer', fontSize: 13 },
  btnX: { background: '#fee2e2', color: '#dc2626', border: 'none', borderRadius: 6, width: 30, height: 34, cursor: 'pointer', fontWeight: 700 },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
};

const LINEA_VACIA = { medicamentoId: '', dosis: '', duracion: '', cantidad: 1 };

export default function Recetas() {
  const rol = getRol();
  const esMedico = rol === 'MEDICO';
  const medicoId = getMedicoId();

  const [recetas, setRecetas] = useState([]);
  const [buscar, setBuscar] = useState('');
  const [pacientes, setPacientes] = useState([]);
  const [medicamentos, setMedicamentos] = useState([]);
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState({ pacienteId: '', diagnostico: '', detalles: [{ ...LINEA_VACIA }] });
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    try {
      if (esMedico && medicoId) setRecetas((await recetaService.listarPorMedico(medicoId)).data);
      else setRecetas((await recetaService.listar()).data);
      if (esMedico) {
        setPacientes((await pacienteService.listar()).data);
        setMedicamentos((await medicamentoService.listar()).data);
      }
    } catch (e) { console.error(e); }
  };
  useEffect(() => { cargar(); /* eslint-disable-next-line */ }, []);

  const abrirNueva = () => { setForm({ pacienteId: '', diagnostico: '', detalles: [{ ...LINEA_VACIA }] }); setError(''); setModal(true); };
  const setLinea = (i, campo, valor) => setForm(f => ({ ...f, detalles: f.detalles.map((d, j) => j === i ? { ...d, [campo]: valor } : d) }));
  const addLinea = () => setForm(f => ({ ...f, detalles: [...f.detalles, { ...LINEA_VACIA }] }));
  const delLinea = (i) => setForm(f => ({ ...f, detalles: f.detalles.filter((_, j) => j !== i) }));

  const emitir = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      await recetaService.emitir({
        pacienteId: Number(form.pacienteId),
        medicoId: Number(medicoId),
        diagnostico: form.diagnostico,
        detalles: form.detalles
          .filter(d => d.medicamentoId)
          .map(d => ({ medicamentoId: Number(d.medicamentoId), dosis: d.dosis, duracion: d.duracion, cantidad: Number(d.cantidad) || 1 })),
      });
      setModal(false); cargar();
    } catch (err) { setError(err.response?.data?.error || err.response?.data?.message || 'Error al emitir la receta'); }
    finally { setGuardando(false); }
  };

  const entregar = async (id) => {
    if (!window.confirm('¿Entregar esta receta? Se descontará el stock de los medicamentos.')) return;
    try { await recetaService.entregar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error al entregar'); }
  };

  const fmt = (f) => new Date(f).toLocaleString('es-PE', { dateStyle: 'short', timeStyle: 'short' });

  const visibles = recetas.filter(r => !buscar || incluye(buscar,
    r.paciente?.nombre, r.paciente?.apellido, r.medico?.nombre, r.medico?.apellido, r.diagnostico,
    ...(r.detalles || []).map(d => d.medicamento?.nombre)));

  return (
    <Layout titulo={esMedico ? 'Mis Recetas' : 'Recetas — Farmacia'}>
      <div style={s.header}>
        <div><div style={s.titulo}>💊 {esMedico ? 'Mis recetas' : 'Recetas pendientes de entrega'}</div></div>
        {esMedico && <button style={s.btnPrimario} onClick={abrirNueva}>+ Nueva receta</button>}
      </div>
      <div style={s.subtitulo}>{esMedico ? 'Emite recetas para tus pacientes.' : 'Entrega las recetas y descuenta el stock de farmacia.'}</div>

      <div style={{ marginBottom: 16 }}>
        <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por paciente, médico, diagnóstico o medicamento..." ancho={420} />
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <th style={s.th}>Fecha</th>
            <th style={s.th}>Paciente</th>
            {!esMedico && <th style={s.th}>Médico</th>}
            <th style={s.th}>Diagnóstico</th>
            <th style={s.th}>Medicamentos</th>
            <th style={s.th}>Estado</th>
            {!esMedico && <th style={s.th}>Acción</th>}
          </tr>
        </thead>
        <tbody>
          {visibles.length === 0 ? (
            <tr><td colSpan={esMedico ? 5 : 7} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No se encontraron recetas</td></tr>
          ) : visibles.map(r => (
            <tr key={r.id}>
              <td style={s.td}>{fmt(r.fecha)}</td>
              <td style={s.td}>{r.paciente?.nombre} {r.paciente?.apellido}</td>
              {!esMedico && <td style={s.td}>Dr. {r.medico?.nombre} {r.medico?.apellido}</td>}
              <td style={s.td}>{r.diagnostico || '—'}</td>
              <td style={s.td}>
                <ul style={s.meds}>
                  {r.detalles?.map(d => (
                    <li key={d.id}>{d.medicamento?.nombre} × {d.cantidad}{d.dosis ? ` · ${d.dosis}` : ''}{d.duracion ? ` · ${d.duracion}` : ''}</li>
                  ))}
                </ul>
              </td>
              <td style={s.td}><span style={s.badge(r.estado === 'ENTREGADA')}>{r.estado}</span></td>
              {!esMedico && <td style={s.td}>{r.estado === 'PENDIENTE' && <button style={s.btnAcc} onClick={() => entregar(r.id)}>Entregar</button>}</td>}
            </tr>
          ))}
        </tbody>
      </table>

      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Nueva receta</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={emitir}>
              <div style={s.campo}>
                <label style={s.label}>Paciente *</label>
                <select style={{ ...s.input, background: '#fff' }} required value={form.pacienteId}
                        onChange={e => setForm(f => ({ ...f, pacienteId: e.target.value }))}>
                  <option value="">— Seleccionar paciente —</option>
                  {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido} ({p.dni})</option>)}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Diagnóstico</label>
                <input style={s.input} placeholder="Diagnóstico" value={form.diagnostico}
                       onChange={e => setForm(f => ({ ...f, diagnostico: e.target.value }))} />
              </div>

              <div style={s.lineaHead}>
                <label style={{ ...s.label, marginBottom: 0 }}>Medicamentos *</label>
                <button type="button" style={s.btnLink} onClick={addLinea}>+ Agregar medicamento</button>
              </div>
              {form.detalles.map((d, i) => (
                <div key={i} style={s.linea}>
                  <select style={{ ...s.input, background: '#fff' }} value={d.medicamentoId} onChange={e => setLinea(i, 'medicamentoId', e.target.value)}>
                    <option value="">— Medicamento —</option>
                    {medicamentos.map(m => <option key={m.id} value={m.id}>{m.nombre} (stock: {m.stockActual})</option>)}
                  </select>
                  <input style={s.input} placeholder="Dosis" value={d.dosis} onChange={e => setLinea(i, 'dosis', e.target.value)} />
                  <input style={s.input} placeholder="Duración" value={d.duracion} onChange={e => setLinea(i, 'duracion', e.target.value)} />
                  <input style={s.input} type="number" min={1} placeholder="Cant." value={d.cantidad} onChange={e => setLinea(i, 'cantidad', e.target.value)} />
                  <button type="button" style={s.btnX} onClick={() => delLinea(i)} disabled={form.detalles.length === 1}>×</button>
                </div>
              ))}

              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Emitiendo...' : 'Emitir receta'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

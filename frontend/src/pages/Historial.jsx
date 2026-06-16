import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import { historialService, pacienteService, citaService } from '../services/servicios';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  select: { padding: '10px 14px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff', minWidth: 260 },
  cards: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(380px, 1fr))', gap: 16 },
  card: { background: '#fff', borderRadius: 12, padding: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.08)', border: '1px solid #e8ddd8' },
  cardTit: { fontSize: 15, fontWeight: 700, color: '#1e293b', marginBottom: 4 },
  cardSub: { fontSize: 13, color: '#64748b', marginBottom: 16 },
  seccion: { marginBottom: 12 },
  seccionTit: { fontSize: 12, fontWeight: 700, color: '#711610', textTransform: 'uppercase', letterSpacing: 0.5, marginBottom: 4 },
  seccionTxt: { fontSize: 14, color: '#374151', lineHeight: 1.5 },
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 540, maxHeight: '90vh', overflowY: 'auto', boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  inputMod: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff' },
  textarea: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', resize: 'vertical', minHeight: 80 },
  selectMod: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
};

const FORM_VACIO = { citaId: '', diagnostico: '', tratamiento: '', receta: '', observaciones: '' };

export default function Historial() {
  const [historiales, setHistoriales] = useState([]);
  const [pacientes, setPacientes] = useState([]);
  const [citasPendientes, setCitasPendientes] = useState([]);
  const [pacienteSeleccionado, setPacienteSeleccionado] = useState('');
  const [buscar, setBuscar] = useState('');
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState(FORM_VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  useEffect(() => {
    pacienteService.listar().then(r => setPacientes(r.data));
    citaService.listar().then(r => setCitasPendientes(r.data.filter(c => c.estado === 'PENDIENTE' || c.estado === 'REPROGRAMADA')));
  }, []);

  useEffect(() => {
    if (!pacienteSeleccionado) { setHistoriales([]); return; }
    historialService.listarPorPaciente(pacienteSeleccionado)
      .then(r => setHistoriales(r.data))
      .catch(() => setHistoriales([]));
  }, [pacienteSeleccionado]);

  const guardar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      await historialService.registrar({ ...form, citaId: Number(form.citaId) });
      setModal(false); setForm(FORM_VACIO);
      if (pacienteSeleccionado) historialService.listarPorPaciente(pacienteSeleccionado).then(r => setHistoriales(r.data));
      citaService.listar().then(r => setCitasPendientes(r.data.filter(c => c.estado === 'PENDIENTE' || c.estado === 'REPROGRAMADA')));
    } catch (err) { setError(err.response?.data?.error || 'Error al registrar'); }
    finally { setGuardando(false); }
  };

  const formatFecha = (f) => new Date(f).toLocaleDateString('es-PE', { dateStyle: 'long' });

  return (
    <Layout titulo="Historial médico">
      <div style={s.header}>
        <div style={s.titulo}>📋 Historial médico</div>
        <div style={{ display: 'flex', gap: 12 }}>
          <select style={s.select} value={pacienteSeleccionado} onChange={e => setPacienteSeleccionado(e.target.value)}>
            <option value="">— Ver historial de paciente —</option>
            {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido} ({p.dni})</option>)}
          </select>
          <button style={s.btnPrimario} onClick={() => { setForm(FORM_VACIO); setError(''); setModal(true); }}>+ Registrar consulta</button>
        </div>
      </div>

      {!pacienteSeleccionado && (
        <div style={{ background: '#fff', borderRadius: 12, padding: 60, textAlign: 'center', color: '#94a3b8' }}>
          Selecciona un paciente para ver su historial médico
        </div>
      )}

      {pacienteSeleccionado && historiales.length === 0 && (
        <div style={{ background: '#fff', borderRadius: 12, padding: 60, textAlign: 'center', color: '#94a3b8' }}>
          Este paciente no tiene historial médico registrado
        </div>
      )}

      {pacienteSeleccionado && historiales.length > 0 && (
        <div style={{ marginBottom: 16 }}>
          <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por diagnóstico, tratamiento o médico..." ancho={400} />
        </div>
      )}

      <div style={s.cards}>
        {historiales.filter(h => !buscar || incluye(buscar, h.diagnostico, h.tratamiento, h.receta, h.observaciones, h.cita?.medico?.nombre, h.cita?.medico?.apellido)).map(h => (
          <div key={h.id} style={s.card}>
            <div style={s.cardTit}>
              Consulta del {formatFecha(h.fechaRegistro)}
            </div>
            <div style={s.cardSub}>
              Dr. {h.cita?.medico?.nombre} {h.cita?.medico?.apellido} · {h.cita?.medico?.especialidad?.nombre}
            </div>
            {h.diagnostico && <div style={s.seccion}><div style={s.seccionTit}>Diagnóstico</div><div style={s.seccionTxt}>{h.diagnostico}</div></div>}
            {h.tratamiento && <div style={s.seccion}><div style={s.seccionTit}>Tratamiento</div><div style={s.seccionTxt}>{h.tratamiento}</div></div>}
            {h.receta && <div style={s.seccion}><div style={s.seccionTit}>Receta</div><div style={s.seccionTxt}>{h.receta}</div></div>}
            {h.observaciones && <div style={s.seccion}><div style={s.seccionTit}>Observaciones</div><div style={s.seccionTxt}>{h.observaciones}</div></div>}
          </div>
        ))}
      </div>

      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Registrar consulta médica</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.campo}>
                <label style={s.label}>Cita a atender *</label>
                <select style={s.selectMod} required value={form.citaId} onChange={e => setForm(f => ({ ...f, citaId: e.target.value }))}>
                  <option value="">— Seleccionar cita —</option>
                  {citasPendientes.map(c => (
                    <option key={c.id} value={c.id}>
                      {c.paciente?.nombre} {c.paciente?.apellido} · {new Date(c.fechaHora).toLocaleDateString('es-PE')} · Dr. {c.medico?.nombre} {c.medico?.apellido}
                    </option>
                  ))}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Diagnóstico *</label>
                <textarea style={s.textarea} required value={form.diagnostico} onChange={e => setForm(f => ({ ...f, diagnostico: e.target.value }))} placeholder="Ingresa el diagnóstico médico..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Tratamiento</label>
                <textarea style={s.textarea} value={form.tratamiento} onChange={e => setForm(f => ({ ...f, tratamiento: e.target.value }))} placeholder="Tratamiento indicado..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Receta</label>
                <textarea style={s.textarea} value={form.receta} onChange={e => setForm(f => ({ ...f, receta: e.target.value }))} placeholder="Medicamentos recetados y dosis..." />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Observaciones</label>
                <textarea style={s.textarea} value={form.observaciones} onChange={e => setForm(f => ({ ...f, observaciones: e.target.value }))} placeholder="Observaciones adicionales..." />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Guardando...' : 'Registrar consulta'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

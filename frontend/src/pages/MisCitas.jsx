import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { citaService, medicoService, especialidadService } from '../services/servicios';
import { getPacienteId } from '../services/authService';

const COLORES = {
  PENDIENTE:    ['#e0f2fe', '#0369a1'],
  ATENDIDA:     ['#dcfce7', '#166534'],
  CANCELADA:    ['#fee2e2', '#dc2626'],
  REPROGRAMADA: ['#fef3c7', '#92400e'],
};

const s = {
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b', marginBottom: 6 },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 28 },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 24 },
  btnPrimario: { padding: '10px 20px', background: '#1a73e8', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 600, color: '#64748b', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e2e8f0', background: '#f8fafc' },
  td: { padding: '14px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1f5f9' },
  badge: (estado) => ({ display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: COLORES[estado]?.[0] || '#f1f5f9', color: COLORES[estado]?.[1] || '#475569' }),
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 500, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' },
  select: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff', boxSizing: 'border-box' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1f5f9', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
  vacio: { background: '#fff', borderRadius: 12, padding: '60px 24px', textAlign: 'center', color: '#94a3b8', boxShadow: '0 1px 3px rgba(0,0,0,0.08)' },
};

export default function MisCitas() {
  const pacienteId = getPacienteId();
  const [citas, setCitas]             = useState([]);
  const [medicos, setMedicos]         = useState([]);
  const [especialidades, setEsp]      = useState([]);
  const [espSeleccionada, setEspSel]  = useState('');
  const [modal, setModal]             = useState(false);
  const [form, setForm]               = useState({ medicoId: '', fechaHora: '', motivo: '', consultorio: '' });
  const [error, setError]             = useState('');
  const [guardando, setGuardando]     = useState(false);
  const [cargando, setCargando]       = useState(true);

  const cargar = async () => {
    if (!pacienteId) { setCargando(false); return; }
    try {
      const { data } = await citaService.listarPorPaciente(pacienteId);
      setCitas(data);
    } catch (e) { console.error(e); }
    finally { setCargando(false); }
  };

  useEffect(() => {
    cargar();
    especialidadService.listar().then(r => setEsp(r.data));
  }, []);

  useEffect(() => {
    if (!espSeleccionada) { setMedicos([]); return; }
    medicoService.listar({ especialidadId: espSeleccionada }).then(r => setMedicos(r.data));
    setForm(f => ({ ...f, medicoId: '' }));
  }, [espSeleccionada]);

  const abrirModal = () => { setForm({ medicoId: '', fechaHora: '', motivo: '', consultorio: '' }); setError(''); setEspSel(''); setModal(true); };

  const guardar = async (e) => {
    e.preventDefault();
    if (!pacienteId) { setError('No se encontró tu registro de paciente. Contacta con administración.'); return; }
    setGuardando(true); setError('');
    try {
      await citaService.crear({ ...form, pacienteId, medicoId: Number(form.medicoId) });
      setModal(false);
      cargar();
    } catch (err) {
      setError(err.response?.data?.error || 'Error al agendar la cita');
    } finally { setGuardando(false); }
  };

  const cancelar = async (id) => {
    if (!window.confirm('¿Cancelar esta cita?')) return;
    try { await citaService.cancelar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error al cancelar'); }
  };

  const formatFecha = (f) => new Date(f).toLocaleString('es-PE', { dateStyle: 'medium', timeStyle: 'short' });

  if (!pacienteId) return (
    <Layout>
      <div style={{ background: '#fef3c7', borderRadius: 12, padding: 32, border: '1px solid #fcd34d' }}>
        <div style={{ fontSize: 18, fontWeight: 700, color: '#92400e', marginBottom: 8 }}>⚠️ Perfil incompleto</div>
        <p style={{ color: '#78350f' }}>Tu cuenta no tiene un perfil de paciente asociado. Por favor contacta con administración para que completen tu registro.</p>
      </div>
    </Layout>
  );

  return (
    <Layout>
      <div style={s.header}>
        <div>
          <div style={s.titulo}>📅 Mis citas</div>
          <div style={s.subtitulo}>Agenda y gestiona tus consultas médicas</div>
        </div>
        <button style={s.btnPrimario} onClick={abrirModal}>+ Agendar cita</button>
      </div>

      {cargando ? (
        <div style={s.vacio}>Cargando tus citas...</div>
      ) : citas.length === 0 ? (
        <div style={s.vacio}>
          <div style={{ fontSize: 40, marginBottom: 12 }}>📅</div>
          <div style={{ fontSize: 16, fontWeight: 600, marginBottom: 8, color: '#475569' }}>No tienes citas registradas</div>
          <p style={{ marginBottom: 20 }}>Agenda tu primera consulta médica</p>
          <button style={s.btnPrimario} onClick={abrirModal}>Agendar mi primera cita</button>
        </div>
      ) : (
        <table style={s.tabla}>
          <thead>
            <tr>
              <th style={s.th}>Fecha y hora</th>
              <th style={s.th}>Médico</th>
              <th style={s.th}>Especialidad</th>
              <th style={s.th}>Motivo</th>
              <th style={s.th}>Estado</th>
              <th style={s.th}>Acción</th>
            </tr>
          </thead>
          <tbody>
            {citas.map(c => (
              <tr key={c.id}>
                <td style={s.td}><strong>{formatFecha(c.fechaHora)}</strong></td>
                <td style={s.td}>Dr. {c.medico?.nombre} {c.medico?.apellido}</td>
                <td style={s.td}>{c.medico?.especialidad?.nombre}</td>
                <td style={s.td}>{c.motivo || '—'}</td>
                <td style={s.td}><span style={s.badge(c.estado)}>{c.estado}</span></td>
                <td style={s.td}>
                  {c.estado === 'PENDIENTE' && (
                    <button onClick={() => cancelar(c.id)}
                      style={{ padding: '5px 12px', background: '#fee2e2', color: '#dc2626', border: 'none', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                      Cancelar
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* Modal nueva cita */}
      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Agendar nueva cita</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.campo}>
                <label style={s.label}>Especialidad</label>
                <select style={s.select} required value={espSeleccionada} onChange={e => setEspSel(e.target.value)}>
                  <option value="">— Seleccionar especialidad —</option>
                  {especialidades.map(e => <option key={e.id} value={e.id}>{e.nombre}</option>)}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Médico *</label>
                <select style={s.select} required value={form.medicoId} onChange={e => setForm(f => ({ ...f, medicoId: e.target.value }))} disabled={!espSeleccionada}>
                  <option value="">— {espSeleccionada ? 'Seleccionar médico' : 'Primero elige una especialidad'} —</option>
                  {medicos.map(m => <option key={m.id} value={m.id}>Dr. {m.nombre} {m.apellido}</option>)}
                </select>
              </div>
              <div style={s.campo}>
                <label style={s.label}>Fecha y hora *</label>
                <input style={s.input} type="datetime-local" required
                  min={new Date().toISOString().slice(0, 16)}
                  value={form.fechaHora} onChange={e => setForm(f => ({ ...f, fechaHora: e.target.value }))} />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Motivo de consulta</label>
                <input style={s.input} placeholder="¿Por qué necesitas la cita?" value={form.motivo} onChange={e => setForm(f => ({ ...f, motivo: e.target.value }))} />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Agendando...' : 'Confirmar cita'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

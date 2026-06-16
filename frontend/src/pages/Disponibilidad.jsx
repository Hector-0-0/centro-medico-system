import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { disponibilidadService } from '../services/servicios';
import { getMedicoId } from '../services/authService';

const DIAS = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 20 },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  btnAcc: (color) => ({ padding: '5px 12px', border: 'none', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer', background: color === 'rojo' ? '#fee2e2' : '#fbece9', color: color === 'rojo' ? '#dc2626' : '#711610' }),
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 460, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  grilla: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 },
  campoFull: { gridColumn: '1 / -1' },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
  aviso: { background: '#fffbeb', border: '1px solid #fcd34d', borderRadius: 10, padding: 20, color: '#78350f', fontSize: 14 },
};

const FORM_VACIO = { diaSemana: 'LUNES', horaInicio: '08:00', horaFin: '12:00', consultorio: '' };

export default function Disponibilidad() {
  const medicoId = getMedicoId();
  const [horarios, setHorarios] = useState([]);
  const [modal, setModal] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(FORM_VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    if (!medicoId) return;
    try { setHorarios((await disponibilidadService.listarPorMedico(medicoId)).data); }
    catch (e) { console.error(e); }
  };
  useEffect(() => { cargar(); /* eslint-disable-next-line */ }, []);

  const abrirNuevo = () => { setEditando(null); setForm(FORM_VACIO); setError(''); setModal(true); };
  const abrirEditar = (h) => {
    setEditando(h.id);
    setForm({ diaSemana: h.diaSemana, horaInicio: h.horaInicio?.slice(0, 5), horaFin: h.horaFin?.slice(0, 5), consultorio: h.consultorio || '' });
    setError(''); setModal(true);
  };

  const guardar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      const data = { ...form, medicoId: Number(medicoId) };
      if (editando) await disponibilidadService.actualizar(editando, data);
      else await disponibilidadService.crear(data);
      setModal(false); cargar();
    } catch (err) { setError(err.response?.data?.error || 'Error al guardar'); }
    finally { setGuardando(false); }
  };

  const eliminar = async (id) => {
    if (!window.confirm('¿Eliminar este horario?')) return;
    try { await disponibilidadService.eliminar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error'); }
  };

  const hora = (h) => (h ? h.slice(0, 5) : '');

  if (!medicoId) return (
    <Layout titulo="Mi Disponibilidad">
      <div style={s.aviso}>Esta sección es solo para médicos con perfil vinculado.</div>
    </Layout>
  );

  return (
    <Layout titulo="Mi Disponibilidad">
      <div style={s.header}>
        <div><div style={s.titulo}>🕐 Mi Disponibilidad</div></div>
        <button style={s.btnPrimario} onClick={abrirNuevo}>+ Nuevo horario</button>
      </div>
      <div style={s.subtitulo}>Define los días y horas en que atiendes; los pacientes podrán agendar sobre ellos.</div>

      <table style={s.tabla}>
        <thead>
          <tr><th style={s.th}>Día</th><th style={s.th}>Hora inicio</th><th style={s.th}>Hora fin</th><th style={s.th}>Consultorio</th><th style={s.th}>Acciones</th></tr>
        </thead>
        <tbody>
          {horarios.length === 0 ? (
            <tr><td colSpan={5} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>Aún no tienes horarios registrados</td></tr>
          ) : horarios.map(h => (
            <tr key={h.id}>
              <td style={s.td}>{h.diaSemana}</td>
              <td style={s.td}>{hora(h.horaInicio)}</td>
              <td style={s.td}>{hora(h.horaFin)}</td>
              <td style={s.td}>{h.consultorio || '—'}</td>
              <td style={s.td}>
                <button style={{ ...s.btnAcc('azul'), marginRight: 6 }} onClick={() => abrirEditar(h)}>Editar</button>
                <button style={s.btnAcc('rojo')} onClick={() => eliminar(h.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>{editando ? 'Editar horario' : 'Nuevo horario'}</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.grilla}>
                <div style={s.campoFull}>
                  <label style={s.label}>Día de la semana *</label>
                  <select style={{ ...s.input, background: '#fff' }} value={form.diaSemana}
                          onChange={e => setForm(f => ({ ...f, diaSemana: e.target.value }))}>
                    {DIAS.map(d => <option key={d} value={d}>{d}</option>)}
                  </select>
                </div>
                <div>
                  <label style={s.label}>Hora inicio *</label>
                  <input style={s.input} type="time" required value={form.horaInicio}
                         onChange={e => setForm(f => ({ ...f, horaInicio: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Hora fin *</label>
                  <input style={s.input} type="time" required value={form.horaFin}
                         onChange={e => setForm(f => ({ ...f, horaFin: e.target.value }))} />
                </div>
                <div style={s.campoFull}>
                  <label style={s.label}>Consultorio</label>
                  <input style={s.input} placeholder="Ej: Consultorio 1" value={form.consultorio}
                         onChange={e => setForm(f => ({ ...f, consultorio: e.target.value }))} />
                </div>
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setModal(false)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Guardando...' : 'Guardar'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

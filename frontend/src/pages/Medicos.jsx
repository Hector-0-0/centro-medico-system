import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import ThOrden from '../components/ThOrden';
import { useOrden } from '../components/useOrden';
import { medicoService, especialidadService } from '../services/servicios';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  badge: { display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: '#fbece9', color: '#711610' },
  btnAccion: (color) => ({ padding: '5px 12px', border: 'none', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer', marginRight: 6, background: color === 'azul' ? '#fbece9' : '#fee2e2', color: color === 'azul' ? '#711610' : '#dc2626' }),
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 480, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  grilla: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 },
  campoFull: { gridColumn: '1 / -1' },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none' },
  select: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', background: '#fff' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
};

const FORM_VACIO = { cmp: '', nombre: '', apellido: '', telefono: '', especialidadId: '' };

export default function Medicos() {
  const [medicos, setMedicos] = useState([]);
  const [especialidades, setEspecialidades] = useState([]);
  const [buscar, setBuscar] = useState('');
  const orden = useOrden();
  const [modal, setModal] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(FORM_VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    try {
      const [resM, resE] = await Promise.all([medicoService.listar(), especialidadService.listar()]);
      setMedicos(resM.data);
      setEspecialidades(resE.data);
    } catch (e) { console.error(e); }
  };

  useEffect(() => { cargar(); }, []);

  const abrirNuevo = () => { setEditando(null); setForm(FORM_VACIO); setError(''); setModal(true); };
  const abrirEditar = (m) => {
    setEditando(m.id);
    setForm({ cmp: m.cmp, nombre: m.nombre, apellido: m.apellido, telefono: m.telefono || '', especialidadId: m.especialidad?.id || '' });
    setError(''); setModal(true);
  };

  const guardar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      const payload = { ...form, especialidadId: Number(form.especialidadId) };
      if (editando) await medicoService.actualizar(editando, payload);
      else await medicoService.registrar(payload);
      setModal(false); cargar();
    } catch (err) { setError(err.response?.data?.error || 'Error al guardar'); }
    finally { setGuardando(false); }
  };

  const eliminar = async (id) => {
    if (!window.confirm('¿Eliminar este médico?')) return;
    try { await medicoService.eliminar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error al eliminar'); }
  };

  const visibles = orden.ordenar(
    medicos.filter(m => !buscar || incluye(buscar, m.cmp, m.nombre, m.apellido, m.especialidad?.nombre, m.telefono)),
    { cmp: m => m.cmp, nombre: m => `${m.nombre} ${m.apellido}`, especialidad: m => m.especialidad?.nombre, telefono: m => m.telefono }
  );

  return (
    <Layout titulo="Médicos">
      <div style={s.header}>
        <div style={s.titulo}>👨‍⚕️ Médicos</div>
        <button style={s.btnPrimario} onClick={abrirNuevo}>+ Nuevo médico</button>
      </div>

      <div style={{ marginBottom: 16 }}>
        <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por nombre, CMP o especialidad..." ancho={360} />
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <ThOrden label="CMP" col="cmp" orden={orden} style={s.th} />
            <ThOrden label="Nombre" col="nombre" orden={orden} style={s.th} />
            <ThOrden label="Especialidad" col="especialidad" orden={orden} style={s.th} />
            <ThOrden label="Teléfono" col="telefono" orden={orden} style={s.th} />
            <th style={s.th}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {visibles.length === 0 ? (
            <tr><td colSpan={5} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No se encontraron médicos</td></tr>
          ) : visibles.map(m => (
            <tr key={m.id}>
              <td style={s.td}>{m.cmp}</td>
              <td style={s.td}><strong>Dr. {m.nombre} {m.apellido}</strong></td>
              <td style={s.td}><span style={s.badge}>{m.especialidad?.nombre}</span></td>
              <td style={s.td}>{m.telefono || '—'}</td>
              <td style={s.td}>
                <button style={s.btnAccion('azul')} onClick={() => abrirEditar(m)}>Editar</button>
                <button style={s.btnAccion('rojo')} onClick={() => eliminar(m.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>{editando ? 'Editar médico' : 'Nuevo médico'}</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.grilla}>
                <div>
                  <label style={s.label}>CMP *</label>
                  <input style={s.input} required value={form.cmp} onChange={e => setForm(f => ({ ...f, cmp: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Teléfono</label>
                  <input style={s.input} value={form.telefono} onChange={e => setForm(f => ({ ...f, telefono: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Nombre *</label>
                  <input style={s.input} required value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Apellido *</label>
                  <input style={s.input} required value={form.apellido} onChange={e => setForm(f => ({ ...f, apellido: e.target.value }))} />
                </div>
                <div style={s.campoFull}>
                  <label style={s.label}>Especialidad *</label>
                  <select style={s.select} required value={form.especialidadId} onChange={e => setForm(f => ({ ...f, especialidadId: e.target.value }))}>
                    <option value="">— Seleccionar especialidad —</option>
                    {especialidades.map(e => <option key={e.id} value={e.id}>{e.nombre}</option>)}
                  </select>
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

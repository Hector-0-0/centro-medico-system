import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { pacienteService } from '../services/servicios';

/* ─── Estilos ─────────────────────────────────────────────────────────────── */
const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  btnPrimario: {
    padding: '10px 20px', background: '#711610', color: '#fff',
    border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer',
  },
  buscador: {
    padding: '10px 14px', border: '1.5px solid #d1d5db', borderRadius: 8,
    fontSize: 14, width: 300, outline: 'none',
  },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  btnAccion: (color) => ({
    padding: '5px 12px', border: 'none', borderRadius: 6, fontSize: 12,
    fontWeight: 600, cursor: 'pointer', marginRight: 6,
    background: color === 'azul' ? '#fbece9' : color === 'rojo' ? '#fee2e2' : '#f0fdf4',
    color: color === 'azul' ? '#711610' : color === 'rojo' ? '#dc2626' : '#166534',
  }),
  overlay: {
    position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
    display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000,
  },
  modal: {
    background: '#fff', borderRadius: 16, padding: 32, width: 520,
    maxHeight: '90vh', overflowY: 'auto', boxShadow: '0 20px 60px rgba(0,0,0,0.2)',
  },
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

const FORM_VACIO = { dni: '', nombre: '', apellido: '', fechaNacimiento: '', telefono: '', direccion: '', grupoSanguineo: '', alergias: '' };
const GRUPOS = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

export default function Pacientes() {
  const [pacientes, setPacientes] = useState([]);
  const [buscar, setBuscar] = useState('');
  const [modal, setModal] = useState(false);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(FORM_VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async (termino = '') => {
    try {
      const { data } = await pacienteService.listar(termino);
      setPacientes(data);
    } catch (e) { console.error(e); }
  };

  useEffect(() => { cargar(); }, []);

  useEffect(() => {
    const timer = setTimeout(() => cargar(buscar), 400);
    return () => clearTimeout(timer);
  }, [buscar]);

  const abrirNuevo = () => { setEditando(null); setForm(FORM_VACIO); setError(''); setModal(true); };
  const abrirEditar = (p) => {
    setEditando(p.id);
    setForm({ dni: p.dni, nombre: p.nombre, apellido: p.apellido, fechaNacimiento: p.fechaNacimiento || '', telefono: p.telefono || '', direccion: p.direccion || '', grupoSanguineo: p.grupoSanguineo || '', alergias: p.alergias || '' });
    setError('');
    setModal(true);
  };

  const guardar = async (e) => {
    e.preventDefault();
    setGuardando(true); setError('');
    try {
      if (editando) await pacienteService.actualizar(editando, form);
      else await pacienteService.registrar(form);
      setModal(false);
      cargar(buscar);
    } catch (err) {
      setError(err.response?.data?.error || 'Error al guardar');
    } finally { setGuardando(false); }
  };

  const eliminar = async (id) => {
    if (!window.confirm('¿Eliminar este paciente?')) return;
    try { await pacienteService.eliminar(id); cargar(buscar); }
    catch (err) { alert(err.response?.data?.error || 'Error al eliminar'); }
  };

  return (
    <Layout titulo="Gestión de Pacientes">
      <div style={s.header}>
        <div style={s.titulo}>👥 Pacientes</div>
        <div style={{ display: 'flex', gap: 12 }}>
          <input style={s.buscador} placeholder="🔍 Buscar por nombre o DNI..." value={buscar} onChange={e => setBuscar(e.target.value)} />
          <button style={s.btnPrimario} onClick={abrirNuevo}>+ Nuevo paciente</button>
        </div>
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <th style={s.th}>DNI</th>
            <th style={s.th}>Nombre completo</th>
            <th style={s.th}>Teléfono</th>
            <th style={s.th}>Grupo sanguíneo</th>
            <th style={s.th}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {pacientes.length === 0 ? (
            <tr><td colSpan={5} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No se encontraron pacientes</td></tr>
          ) : pacientes.map(p => (
            <tr key={p.id}>
              <td style={s.td}>{p.dni}</td>
              <td style={s.td}><strong>{p.nombre} {p.apellido}</strong></td>
              <td style={s.td}>{p.telefono || '—'}</td>
              <td style={s.td}>{p.grupoSanguineo || '—'}</td>
              <td style={s.td}>
                <button style={s.btnAccion('azul')} onClick={() => abrirEditar(p)}>Editar</button>
                <button style={s.btnAccion('rojo')} onClick={() => eliminar(p.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal */}
      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>{editando ? 'Editar paciente' : 'Nuevo paciente'}</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.grilla}>
                <div>
                  <label style={s.label}>DNI *</label>
                  <input style={s.input} maxLength={8} required value={form.dni} onChange={e => setForm(f => ({ ...f, dni: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Grupo sanguíneo</label>
                  <select style={s.select} value={form.grupoSanguineo} onChange={e => setForm(f => ({ ...f, grupoSanguineo: e.target.value }))}>
                    <option value="">— Seleccionar —</option>
                    {GRUPOS.map(g => <option key={g} value={g}>{g}</option>)}
                  </select>
                </div>
                <div>
                  <label style={s.label}>Nombre *</label>
                  <input style={s.input} required value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Apellido *</label>
                  <input style={s.input} required value={form.apellido} onChange={e => setForm(f => ({ ...f, apellido: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Fecha de nacimiento</label>
                  <input style={s.input} type="date" value={form.fechaNacimiento} onChange={e => setForm(f => ({ ...f, fechaNacimiento: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Teléfono</label>
                  <input style={s.input} value={form.telefono} onChange={e => setForm(f => ({ ...f, telefono: e.target.value }))} />
                </div>
                <div style={s.campoFull}>
                  <label style={s.label}>Dirección</label>
                  <input style={s.input} value={form.direccion} onChange={e => setForm(f => ({ ...f, direccion: e.target.value }))} />
                </div>
                <div style={s.campoFull}>
                  <label style={s.label}>Alergias conocidas</label>
                  <input style={s.input} placeholder="Ej: Penicilina, aspirina..." value={form.alergias} onChange={e => setForm(f => ({ ...f, alergias: e.target.value }))} />
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

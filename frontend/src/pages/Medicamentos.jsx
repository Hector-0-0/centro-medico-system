import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { medicamentoService } from '../services/servicios';

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  badge: (bajo) => ({ display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: bajo ? '#fee2e2' : '#dcfce7', color: bajo ? '#dc2626' : '#166534' }),
  btnAcc: (color) => ({ padding: '5px 10px', border: 'none', borderRadius: 6, fontSize: 11, fontWeight: 600, cursor: 'pointer', marginRight: 4, background: color === 'azul' ? '#fbece9' : color === 'rojo' ? '#fee2e2' : '#fef3c7', color: color === 'azul' ? '#711610' : color === 'rojo' ? '#dc2626' : '#92400e' }),
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 480, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  grilla: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 },
  campoFull: { gridColumn: '1 / -1' },
  campo: { marginBottom: 0 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
};

const FORM_VACIO = { nombre: '', principioActivo: '', presentacion: '', stockActual: 0, stockMinimo: 10, unidad: '' };

export default function Medicamentos() {
  const [medicamentos, setMedicamentos] = useState([]);
  const [modal, setModal] = useState(false);
  const [modalStock, setModalStock] = useState(null);
  const [editando, setEditando] = useState(null);
  const [form, setForm] = useState(FORM_VACIO);
  const [cantidadStock, setCantidadStock] = useState(0);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    try { const { data } = await medicamentoService.listar(); setMedicamentos(data); }
    catch (e) { console.error(e); }
  };

  useEffect(() => { cargar(); }, []);

  const abrirNuevo = () => { setEditando(null); setForm(FORM_VACIO); setError(''); setModal(true); };
  const abrirEditar = (m) => {
    setEditando(m.id);
    setForm({ nombre: m.nombre, principioActivo: m.principioActivo || '', presentacion: m.presentacion || '', stockActual: m.stockActual, stockMinimo: m.stockMinimo, unidad: m.unidad || '' });
    setError(''); setModal(true);
  };

  const guardar = async (e) => {
    e.preventDefault(); setGuardando(true); setError('');
    try {
      const payload = { ...form, stockActual: Number(form.stockActual), stockMinimo: Number(form.stockMinimo) };
      if (editando) await medicamentoService.actualizar(editando, payload);
      else await medicamentoService.registrar(payload);
      setModal(false); cargar();
    } catch (err) { setError(err.response?.data?.error || 'Error al guardar'); }
    finally { setGuardando(false); }
  };

  const ajustarStock = async () => {
    try { await medicamentoService.actualizarStock(modalStock.id, Number(cantidadStock)); setModalStock(null); setCantidadStock(0); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error al ajustar stock'); }
  };

  const desactivar = async (id) => {
    if (!window.confirm('¿Desactivar este medicamento?')) return;
    try { await medicamentoService.desactivar(id); cargar(); }
    catch (err) { alert(err.response?.data?.error || 'Error'); }
  };

  return (
    <Layout titulo="Medicamentos">
      <div style={s.header}>
        <div style={s.titulo}>💊 Medicamentos</div>
        <button style={s.btnPrimario} onClick={abrirNuevo}>+ Nuevo medicamento</button>
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <th style={s.th}>Nombre</th>
            <th style={s.th}>Principio activo</th>
            <th style={s.th}>Presentación</th>
            <th style={s.th}>Stock</th>
            <th style={s.th}>Estado</th>
            <th style={s.th}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {medicamentos.length === 0 ? (
            <tr><td colSpan={6} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No hay medicamentos registrados</td></tr>
          ) : medicamentos.map(m => (
            <tr key={m.id}>
              <td style={s.td}><strong>{m.nombre}</strong></td>
              <td style={s.td}>{m.principioActivo || '—'}</td>
              <td style={s.td}>{m.presentacion || '—'}</td>
              <td style={s.td}>{m.stockActual} {m.unidad} <span style={{ color: '#94a3b8', fontSize: 12 }}>(mín. {m.stockMinimo})</span></td>
              <td style={s.td}><span style={s.badge(m.stockActual <= m.stockMinimo)}>{m.stockActual <= m.stockMinimo ? 'Stock bajo' : 'Normal'}</span></td>
              <td style={s.td}>
                <button style={s.btnAcc('azul')} onClick={() => abrirEditar(m)}>Editar</button>
                <button style={s.btnAcc('amarillo')} onClick={() => { setModalStock(m); setCantidadStock(0); }}>Stock</button>
                <button style={s.btnAcc('rojo')} onClick={() => desactivar(m.id)}>Desactivar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal nuevo/editar */}
      {modal && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModal(false)}>
          <div style={s.modal}>
            <div style={s.modalTit}>{editando ? 'Editar medicamento' : 'Nuevo medicamento'}</div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={guardar}>
              <div style={s.grilla}>
                <div style={s.campoFull}>
                  <label style={s.label}>Nombre *</label>
                  <input style={s.input} required value={form.nombre} onChange={e => setForm(f => ({ ...f, nombre: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Principio activo</label>
                  <input style={s.input} value={form.principioActivo} onChange={e => setForm(f => ({ ...f, principioActivo: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Presentación</label>
                  <input style={s.input} placeholder="Ej: Tableta 500mg" value={form.presentacion} onChange={e => setForm(f => ({ ...f, presentacion: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Stock actual</label>
                  <input style={s.input} type="number" min={0} value={form.stockActual} onChange={e => setForm(f => ({ ...f, stockActual: e.target.value }))} />
                </div>
                <div>
                  <label style={s.label}>Stock mínimo</label>
                  <input style={s.input} type="number" min={0} value={form.stockMinimo} onChange={e => setForm(f => ({ ...f, stockMinimo: e.target.value }))} />
                </div>
                <div style={s.campoFull}>
                  <label style={s.label}>Unidad de medida</label>
                  <input style={s.input} placeholder="Ej: unidades, cajas, frascos" value={form.unidad} onChange={e => setForm(f => ({ ...f, unidad: e.target.value }))} />
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

      {/* Modal ajuste de stock */}
      {modalStock && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setModalStock(null)}>
          <div style={{ ...s.modal, width: 360 }}>
            <div style={s.modalTit}>Ajustar stock</div>
            <p style={{ fontSize: 14, color: '#374151', marginBottom: 8 }}><strong>{modalStock.nombre}</strong></p>
            <p style={{ fontSize: 13, color: '#64748b', marginBottom: 16 }}>Stock actual: {modalStock.stockActual} {modalStock.unidad}</p>
            <label style={s.label}>Cantidad a agregar (negativo para restar)</label>
            <input style={{ ...s.input, marginBottom: 8 }} type="number" value={cantidadStock} onChange={e => setCantidadStock(e.target.value)} />
            <p style={{ fontSize: 12, color: '#64748b' }}>Nuevo stock: {modalStock.stockActual + Number(cantidadStock)}</p>
            <div style={s.btnsFoot}>
              <button style={s.btnCancelar} onClick={() => setModalStock(null)}>Cancelar</button>
              <button style={s.btnPrimario} onClick={ajustarStock}>Confirmar</button>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}

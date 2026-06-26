import { useEffect, useState } from 'react';
import {
  listarMedicamentos,
  crearMedicamento,
  actualizarStock,
  listarMovimientos,
} from '../services/medicamentoService';
import { mensajeError } from '../services/api';

const TIPOS = ['tabletas', 'capsulas', 'comprimidos', 'jarabe', 'sobre', 'inyectable', 'crema', 'gotas'];

const estadoDe = (stock) =>
  stock === 0 ? 'AGOTADO' : stock < 20 ? 'BAJO' : 'NORMAL';

const colorEstado = (estado) =>
  estado === 'AGOTADO' ? '#b91c1c' : estado === 'BAJO' ? '#b45309' : '#15803d';

/** Gestión de Stock — réplica del GestionStockPanel del desktop. */
export default function GestionStock() {
  const [lista, setLista] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [modalAbierto, setModalAbierto] = useState(false);
  const [movimientosDe, setMovimientosDe] = useState(null);
  const [ajustarDe, setAjustarDe] = useState(null);

  const cargar = async () => {
    try {
      setLista(await listarMedicamentos());
    } catch {
      setLista([]);
    }
  };

  useEffect(() => {
    cargar();
  }, []);

  const q = busqueda.trim().toLowerCase();
  const filtrados = q
    ? lista.filter(
        (m) =>
          m.nombre.toLowerCase().includes(q) ||
          m.id.toLowerCase().includes(q) ||
          (m.tipo || '').toLowerCase().includes(q),
      )
    : lista;

  return (
    <div>
      <h1 className="panel__title">Gestión de Stock</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por nombre, código o tipo..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn btn--primary" onClick={() => setModalAbierto(true)}>
          + Nuevo Medicamento
        </button>
      </div>

      <p className="panel__hint" style={{ marginBottom: 12 }}>
        Doble clic en un medicamento para ajustar su stock (reabastecimiento).
      </p>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Dosis (mg)</th>
              <th>Stock</th>
              <th>Estado</th>
              <th>Auditoría</th>
            </tr>
          </thead>
          <tbody>
            {filtrados.length === 0 ? (
              <tr>
                <td className="table__empty" colSpan={6}>Sin medicamentos</td>
              </tr>
            ) : (
              filtrados.map((m) => {
                const estado = estadoDe(m.stock);
                return (
                  <tr key={m.id} onDoubleClick={() => setAjustarDe(m)}>
                    <td>{m.id}</td>
                    <td>{m.nombre}</td>
                    <td>{m.dosisMg != null ? m.dosisMg : '—'}</td>
                    <td>{m.stock}</td>
                    <td style={{ color: colorEstado(estado), fontWeight: 700 }}>{estado}</td>
                    <td>
                      <button
                        className="btn btn--ghost btn--sm"
                        onClick={(e) => { e.stopPropagation(); setMovimientosDe(m); }}
                      >
                        Movimientos
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      {modalAbierto && (
        <NuevoMedicamentoModal
          onClose={() => setModalAbierto(false)}
          onSaved={() => {
            setModalAbierto(false);
            cargar();
          }}
        />
      )}

      {movimientosDe && (
        <MovimientosModal
          medicamento={movimientosDe}
          onClose={() => setMovimientosDe(null)}
        />
      )}

      {ajustarDe && (
        <AjustarStockModal
          medicamento={ajustarDe}
          onClose={() => setAjustarDe(null)}
          onSaved={() => {
            setAjustarDe(null);
            cargar();
          }}
        />
      )}
    </div>
  );
}

/** Ajuste manual de stock (reabastecimiento; solo se puede aumentar). */
function AjustarStockModal({ medicamento, onClose, onSaved }) {
  const [valor, setValor] = useState(String(medicamento.stock));
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const guardar = async (e) => {
    e.preventDefault();
    setError('');
    const nuevo = Number(valor.trim());
    if (!Number.isInteger(nuevo) || nuevo < 0) {
      setError('Ingresa un número entero ≥ 0.');
      return;
    }
    if (nuevo < medicamento.stock) {
      setError('El stock solo puede aumentarse manualmente. Las salidas se descuentan al entregar recetas.');
      return;
    }
    setGuardando(true);
    try {
      await actualizarStock(medicamento.id, nuevo);
      onSaved();
    } catch (err) {
      setError(mensajeError(err, 'No se pudo actualizar el stock.'));
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="modal__overlay" onClick={onClose}>
      <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={guardar} style={{ maxWidth: 420 }}>
        <div className="modal__header">Ajustar stock · {medicamento.nombre}</div>
        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}
          <p className="panel__hint" style={{ margin: 0 }}>
            Solo se puede aumentar; las salidas se descuentan al entregar recetas.
          </p>
          <label className="field">
            <span className="field__label">Nuevo stock</span>
            <input
              className="field__input"
              type="number"
              min={medicamento.stock}
              value={valor}
              onChange={(e) => setValor(e.target.value)}
              autoFocus
            />
          </label>
        </div>
        <div className="modal__footer">
          <button type="button" className="btn btn--ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn btn--primary" disabled={guardando}>
            {guardando ? 'Guardando…' : 'Guardar'}
          </button>
        </div>
      </form>
    </div>
  );
}

/** Historial de entradas/salidas de un medicamento (auditoría de stock). */
function MovimientosModal({ medicamento, onClose }) {
  const [movs, setMovs] = useState(null);

  useEffect(() => {
    listarMovimientos(medicamento.id)
      .then(setMovs)
      .catch(() => setMovs([]));
  }, [medicamento.id]);

  return (
    <div className="modal__overlay" onClick={onClose}>
      <div className="modal modal--wide" onClick={(e) => e.stopPropagation()}>
        <div className="modal__header">Movimientos · {medicamento.nombre}</div>
        <div className="modal__body">
          {movs === null ? (
            <p className="panel__hint">Cargando…</p>
          ) : movs.length === 0 ? (
            <p className="panel__hint">Sin movimientos registrados.</p>
          ) : (
            <div className="table-wrap">
              <table className="table">
                <thead>
                  <tr>
                    <th>Fecha</th>
                    <th>Tipo</th>
                    <th>Cantidad</th>
                    <th>Stock resultante</th>
                    <th>Motivo</th>
                    <th>Usuario</th>
                  </tr>
                </thead>
                <tbody>
                  {movs.map((mv) => (
                    <tr key={mv.id}>
                      <td>{(mv.fecha || '').replace('T', ' ').slice(0, 19)}</td>
                      <td style={{ color: mv.tipoMovimiento === 'ENTRADA' ? '#15803d' : '#b91c1c', fontWeight: 700 }}>
                        {mv.tipoMovimiento}
                      </td>
                      <td>{mv.tipoMovimiento === 'ENTRADA' ? '+' : '−'}{mv.cantidad}</td>
                      <td>{mv.stockResultante}</td>
                      <td>{mv.motivo || '—'}</td>
                      <td>{mv.usuario || '—'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
        <div className="modal__footer">
          <button type="button" className="btn btn--primary" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    </div>
  );
}

const VACIO = { id: '', nombre: '', dosisMg: '', stock: '0', tipo: '' };

function NuevoMedicamentoModal({ onClose, onSaved }) {
  const [form, setForm] = useState(VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');

    const { id, nombre, dosisMg, stock, tipo } = form;
    if (!id.trim() || !nombre.trim() || !tipo.trim()) {
      setError('Código, nombre y tipo son obligatorios.');
      return;
    }
    if (!/^[A-Za-z0-9-]{3,15}$/.test(id.trim())) {
      setError('El código debe tener 3-15 caracteres: letras, números o guion.');
      return;
    }
    const dosisNum = Number(dosisMg);
    if (!Number.isInteger(dosisNum) || dosisNum < 1 || dosisNum > 10000) {
      setError('La dosis debe ser un entero entre 1 y 10000 mg.');
      return;
    }
    const stockNum = Number(stock);
    if (!Number.isInteger(stockNum) || stockNum < 0 || stockNum > 10000) {
      setError('El stock debe ser un entero entre 0 y 10000.');
      return;
    }

    setGuardando(true);
    try {
      await crearMedicamento({
        id: id.trim(),
        nombre: nombre.trim(),
        dosisMg: dosisNum,
        stock: stockNum,
        tipo: tipo.trim(),
      });
      onSaved();
    } catch (err) {
      setError(mensajeError(err, 'No se pudo registrar. El código ya puede existir.'));
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="modal__overlay" onClick={onClose}>
      <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={guardar}>
        <div className="modal__header">Nuevo Medicamento</div>
        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}
          <Campo label="Código" value={form.id} onChange={set('id')} autoFocus />
          <Campo label="Nombre" placeholder="Ej. Amoxicilina 500mg" value={form.nombre} onChange={set('nombre')} />
          <Campo label="Dosis (mg)" type="number" min="1" max="10000" placeholder="Ej. 500" value={form.dosisMg} onChange={set('dosisMg')} />
          <Campo label="Stock" type="number" min="0" max="10000" value={form.stock} onChange={set('stock')} />
          <label className="field">
            <span className="field__label">Tipo</span>
            <select className="field__input" value={form.tipo} onChange={set('tipo')}>
              <option value="">Selecciona...</option>
              {TIPOS.map((t) => <option key={t} value={t}>{t}</option>)}
            </select>
          </label>
        </div>
        <div className="modal__footer">
          <button type="button" className="btn btn--ghost" onClick={onClose}>Cancelar</button>
          <button type="submit" className="btn btn--primary" disabled={guardando}>
            {guardando ? 'Guardando…' : 'Guardar'}
          </button>
        </div>
      </form>
    </div>
  );
}

function Campo({ label, ...props }) {
  return (
    <label className="field">
      <span className="field__label">{label}</span>
      <input className="field__input" {...props} />
    </label>
  );
}

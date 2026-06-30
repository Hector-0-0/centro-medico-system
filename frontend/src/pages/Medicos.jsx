import { useState } from 'react';
import {
  listarDoctores,
  crearDoctor,
  eliminarDoctor,
} from '../services/doctorService';
import { mensajeError } from '../services/api';
import { useCargar } from '../hooks/useCargar';
import FilaTablaEstado from '../components/FilaTablaEstado';
import { useDialog } from '../components/Dialog';
import { ESPECIALIDADES } from '../constants/catalogos';

/** Gestión de Médicos (doctores) — réplica del MedicoPanel del desktop. */
export default function Medicos() {
  const { datos, cargando, error, recargar } = useCargar(listarDoctores);
  const lista = datos || [];
  const [busqueda, setBusqueda] = useState('');
  const [especialidad, setEspecialidad] = useState('');
  const [seleccion, setSeleccion] = useState(null);
  const [modalAbierto, setModalAbierto] = useState(false);
  const { alerta, confirmar } = useDialog();

  const cargar = () => recargar().then(() => setSeleccion(null));

  const q = busqueda.trim().toLowerCase();
  const filtrados = lista.filter(
    (d) =>
      (!q ||
        d.nombre.toLowerCase().includes(q) ||
        d.id.toLowerCase().includes(q)) &&
      (!especialidad || d.especialidad === especialidad),
  );

  const eliminar = async () => {
    if (!seleccion) {
      alerta('Selecciona un médico para eliminar.');
      return;
    }
    const d = lista.find((x) => x.id === seleccion);
    if (!(await confirmar(`¿Eliminar al médico ${d?.nombre}?`, { peligro: true, textoOk: 'Eliminar' }))) return;
    try {
      await eliminarDoctor(seleccion);
      cargar();
    } catch (err) {
      alerta(mensajeError(err, 'No se pudo eliminar el médico.'));
    }
  };

  return (
    <div>
      <h1 className="panel__title">Gestión de Médicos</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por código o nombre..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <select
          className="toolbar__select"
          value={especialidad}
          onChange={(e) => setEspecialidad(e.target.value)}
          aria-label="Filtrar por especialidad"
        >
          <option value="">Todas las especialidades</option>
          {ESPECIALIDADES.map((e) => <option key={e} value={e}>{e}</option>)}
        </select>
        <button className="btn btn--danger" onClick={eliminar}>Eliminar</button>
        <button className="btn btn--primary" onClick={() => setModalAbierto(true)}>
          + Nuevo Médico
        </button>
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Especialidad</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {cargando || error || filtrados.length === 0 ? (
              <FilaTablaEstado
                colSpan={4}
                cargando={cargando}
                error={error}
                onReintentar={recargar}
                vacio="Sin médicos"
              />
            ) : (
              filtrados.map((d) => (
                <tr
                  key={d.id}
                  className={seleccion === d.id ? 'is-selected' : ''}
                  onClick={() => setSeleccion(d.id)}
                  tabIndex={0}
                  aria-selected={seleccion === d.id}
                  onKeyDown={(ev) => {
                    if (ev.key === 'Enter' || ev.key === ' ') {
                      ev.preventDefault();
                      setSeleccion(d.id);
                    }
                  }}
                >
                  <td>{d.id}</td>
                  <td>{d.nombre}</td>
                  <td>{d.especialidad}</td>
                  <td>{d.activo ? 'Activo' : 'Inactivo'}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {modalAbierto && (
        <NuevoMedicoModal
          onClose={() => setModalAbierto(false)}
          onSaved={() => {
            setModalAbierto(false);
            cargar();
          }}
        />
      )}
    </div>
  );
}

const VACIO = { id: '', nombre: '', especialidad: '', consultorio: '', password: '' };

function NuevoMedicoModal({ onClose, onSaved }) {
  const [form, setForm] = useState(VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) =>
    setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');

    const { id, nombre, especialidad, consultorio, password } = form;
    if (!id.trim() || !nombre.trim() || !especialidad.trim() || !consultorio.trim() || !password.trim()) {
      setError('Todos los campos son obligatorios.');
      return;
    }
    if (!/^\d{8}$/.test(id.trim())) {
      setError('El código del médico debe ser un DNI de 8 dígitos.');
      return;
    }

    setGuardando(true);
    try {
      await crearDoctor({
        id: id.trim(),
        nombre: nombre.trim(),
        especialidad: especialidad.trim(),
        consultorio: consultorio.trim(),
        password,
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
        <div className="modal__header">Registrar Médico</div>

        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}
          <Campo label="Código (DNI)" placeholder="8 dígitos" inputMode="numeric" maxLength={8} autoFocus
            value={form.id}
            onChange={(e) => setForm((f) => ({ ...f, id: e.target.value.replace(/\D/g, '') }))} />
          <Campo label="Nombre" value={form.nombre} onChange={set('nombre')} />
          <label className="field">
            <span className="field__label">Especialidad</span>
            <select className="field__input" value={form.especialidad} onChange={set('especialidad')}>
              <option value="">Selecciona...</option>
              {ESPECIALIDADES.map((e) => <option key={e} value={e}>{e}</option>)}
            </select>
          </label>
          <Campo label="Consultorio" value={form.consultorio} onChange={set('consultorio')} />
          <Campo label="Contraseña" type="password" value={form.password} onChange={set('password')} />
        </div>

        <div className="modal__footer">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancelar
          </button>
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

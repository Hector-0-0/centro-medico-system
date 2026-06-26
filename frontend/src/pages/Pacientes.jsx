import { useEffect, useState } from 'react';
import {
  listarEstudiantes,
  crearEstudiante,
  eliminarEstudiante,
} from '../services/estudianteService';
import { mensajeError } from '../services/api';
import { useDialog } from '../components/Dialog';
import { CARRERAS } from '../constants/catalogos';

/** Edad cumplida a partir de una fecha de nacimiento (YYYY-MM-DD). */
export function edadDesde(fecha) {
  if (!fecha) return NaN;
  const hoy = new Date();
  const n = new Date(fecha + 'T00:00:00');
  let edad = hoy.getFullYear() - n.getFullYear();
  const m = hoy.getMonth() - n.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < n.getDate())) edad--;
  return edad;
}

/** Gestión de Pacientes (estudiantes) — réplica del PacientePanel del desktop. */
export default function Pacientes() {
  const [lista, setLista] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [seleccion, setSeleccion] = useState(null);
  const [modalAbierto, setModalAbierto] = useState(false);
  const { alerta, confirmar } = useDialog();

  const cargar = async () => {
    try {
      setLista(await listarEstudiantes());
    } catch {
      setLista([]);
    }
    setSeleccion(null);
  };

  useEffect(() => {
    cargar();
  }, []);

  const q = busqueda.trim().toLowerCase();
  const filtrados = q
    ? lista.filter(
        (e) =>
          e.nombre.toLowerCase().includes(q) || e.id.toLowerCase().includes(q),
      )
    : lista;

  const eliminar = async () => {
    if (!seleccion) {
      alerta('Selecciona un paciente para eliminar.');
      return;
    }
    const p = lista.find((e) => e.id === seleccion);
    if (!(await confirmar(`¿Eliminar al paciente ${p?.nombre}?`, { peligro: true, textoOk: 'Eliminar' }))) return;
    try {
      await eliminarEstudiante(seleccion);
      cargar();
    } catch (err) {
      alerta(err.response?.data?.error || 'No se pudo eliminar el paciente.');
    }
  };

  return (
    <div>
      <h1 className="panel__title">Gestión de Pacientes</h1>

      <div className="toolbar">
        <input
          className="toolbar__search"
          placeholder="Buscar por nombre..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn btn--danger" onClick={eliminar}>Eliminar</button>
        <button className="btn btn--primary" onClick={() => setModalAbierto(true)}>
          + Nuevo Paciente
        </button>
      </div>

      <div className="table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Edad</th>
              <th>Carrera</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {filtrados.length === 0 ? (
              <tr>
                <td className="table__empty" colSpan={5}>Sin pacientes</td>
              </tr>
            ) : (
              filtrados.map((e) => (
                <tr
                  key={e.id}
                  className={seleccion === e.id ? 'is-selected' : ''}
                  onClick={() => setSeleccion(e.id)}
                >
                  <td>{e.id}</td>
                  <td>{e.nombre}</td>
                  <td>{e.edad}</td>
                  <td>{e.carrera}</td>
                  <td>{e.email}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {modalAbierto && (
        <NuevoPacienteModal
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

const VACIO = { id: '', nombre: '', fechaNacimiento: '', carrera: '', email: '', password: '' };

function NuevoPacienteModal({ onClose, onSaved }) {
  const [form, setForm] = useState(VACIO);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) =>
    setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');

    const { id, nombre, fechaNacimiento, carrera, email, password } = form;
    if (!id.trim() || !nombre.trim() || !fechaNacimiento || !carrera.trim() || !email.trim() || !password.trim()) {
      setError('Todos los campos son obligatorios.');
      return;
    }
    if (!/^[A-Za-z0-9]{3,10}$/.test(id.trim())) {
      setError('El código debe tener 3-10 caracteres, solo letras y números.');
      return;
    }
    const edadNum = edadDesde(fechaNacimiento);
    if (!Number.isInteger(edadNum) || edadNum < 18 || edadNum > 100) {
      setError('El paciente debe tener entre 18 y 100 años según su fecha de nacimiento.');
      return;
    }
    if (!/^[A-Za-z0-9._%+-]+@(uni\.pe|uni\.edu\.pe)$/.test(email.trim())) {
      setError('El email debe terminar en @uni.pe o @uni.edu.pe.');
      return;
    }

    setGuardando(true);
    try {
      await crearEstudiante({
        id: id.trim(),
        nombre: nombre.trim(),
        fechaNacimiento,
        carrera: carrera.trim(),
        email: email.trim(),
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
        <div className="modal__header">Registrar Paciente</div>

        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}
          <Campo label="Código" value={form.id} onChange={set('id')} autoFocus />
          <Campo label="Nombre" value={form.nombre} onChange={set('nombre')} />
          <Campo
            label="Fecha de nacimiento"
            type="date"
            max={new Date().toISOString().slice(0, 10)}
            value={form.fechaNacimiento}
            onChange={set('fechaNacimiento')}
          />
          <label className="field">
            <span className="field__label">Carrera</span>
            <select className="field__input" value={form.carrera} onChange={set('carrera')}>
              <option value="">Selecciona...</option>
              {CARRERAS.map((c) => <option key={c} value={c}>{c}</option>)}
            </select>
          </label>
          <Campo label="Email" type="email" placeholder="usuario@uni.pe" value={form.email} onChange={set('email')} />
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

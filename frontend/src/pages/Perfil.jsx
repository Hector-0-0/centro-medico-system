import { useEffect, useState } from 'react';
import { obtenerPerfil, actualizarPerfil } from '../services/perfilService';
import { mensajeError } from '../services/api';

// Carreras de la UNI para el combo (el usuario también puede escribir).
const CARRERAS = [
  'Ingenieria de Sistemas', 'Ingenieria Industrial', 'Ingenieria Civil',
  'Ingenieria Electronica', 'Ingenieria Mecanica', 'Ingenieria Quimica',
  'Ingenieria Economica', 'Arquitectura', 'Ciencias',
];

const MAX_FOTO_BYTES = 1_300_000; // ~1.3 MB

/** Edad cumplida a partir de una fecha de nacimiento (YYYY-MM-DD). */
function edadDesde(fecha) {
  if (!fecha) return NaN;
  const hoy = new Date();
  const n = new Date(fecha + 'T00:00:00');
  let edad = hoy.getFullYear() - n.getFullYear();
  const m = hoy.getMonth() - n.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < n.getDate())) edad--;
  return edad;
}

/** Iniciales para el avatar cuando no hay foto. */
function iniciales(nombre = '') {
  return nombre.split(' ').filter(Boolean).slice(0, 2).map((s) => s[0]).join('').toUpperCase() || '?';
}

function Avatar({ nombre, foto, size = 96 }) {
  const estilo = {
    width: size, height: size, borderRadius: '50%', objectFit: 'cover',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    background: '#7f1d1d', color: '#fff', fontSize: size * 0.36, fontWeight: 700,
    flexShrink: 0,
  };
  return foto
    ? <img src={foto} alt={nombre} style={estilo} />
    : <div style={estilo}>{iniciales(nombre)}</div>;
}

/** Mi Perfil — datos + estadísticas, con edición y foto para el estudiante. */
export default function Perfil() {
  const [p, setP] = useState(null);
  const [editando, setEditando] = useState(false);

  useEffect(() => {
    obtenerPerfil().then(setP).catch(() => setP(null));
  }, []);

  if (!p) {
    return (
      <div>
        <h1 className="panel__title">Mi Perfil</h1>
        <p className="panel__hint">Cargando...</p>
      </div>
    );
  }

  const tieneStats = p.totalCitas !== undefined;
  const esEstudiante = p.rol === 'ESTUDIANTE';

  return (
    <div>
      <h1 className="panel__title">Mi Perfil</h1>

      <div className="cards-2">
        {/* Datos personales */}
        <div className="card-section">
          <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 12 }}>
            <Avatar nombre={p.nombre} foto={p.foto} />
            <div>
              <p className="perfil__nombre" style={{ margin: 0 }}>{p.nombre}</p>
              <p className="perfil__linea" style={{ margin: 0 }}>Código: {p.id}</p>
            </div>
          </div>

          {p.rol === 'DOCTOR' && (
            <>
              <p className="perfil__linea">Especialidad: {p.especialidad}</p>
              <p className="perfil__linea">Consultorio: {p.consultorio}</p>
              <p className="perfil__linea">Estado: {p.activo ? 'Activo' : 'Inactivo'}</p>
            </>
          )}
          {esEstudiante && (
            <>
              <p className="perfil__linea">Carrera: {p.carrera}</p>
              <p className="perfil__linea">Email: {p.email || '—'}</p>
              <p className="perfil__linea">
                Fecha de nacimiento: {p.fechaNacimiento || '—'}
                {p.edad != null && ` (${p.edad} años)`}
              </p>
              <button
                className="btn btn--primary"
                style={{ marginTop: 12 }}
                onClick={() => setEditando(true)}
              >
                Editar Perfil
              </button>
            </>
          )}
        </div>

        {/* Estadísticas de citas */}
        {tieneStats && (
          <div className="card-section">
            <h2 className="card-section__title">Resumen de citas</h2>
            <div className="perfil__stat">
              <span>Total de citas</span>
              <strong>{p.totalCitas}</strong>
            </div>
            <div className="perfil__stat">
              <span>Citas pendientes</span>
              <strong>{p.citasPendientes}</strong>
            </div>
            <div className="perfil__stat">
              <span>Citas atendidas</span>
              <strong>{p.citasAtendidas}</strong>
            </div>
          </div>
        )}
      </div>

      {editando && (
        <EditarPerfilModal
          perfil={p}
          onClose={() => setEditando(false)}
          onSaved={(actualizado) => {
            setP(actualizado);
            setEditando(false);
          }}
        />
      )}
    </div>
  );
}

function EditarPerfilModal({ perfil, onClose, onSaved }) {
  const [form, setForm] = useState({
    email: perfil.email || '',
    carrera: perfil.carrera || '',
    fechaNacimiento: perfil.fechaNacimiento || '',
    foto: perfil.foto || null, // null = sin foto; '' = quitar; dataURL = nueva
  });
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const elegirFoto = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) {
      setError('El archivo debe ser una imagen.');
      return;
    }
    if (file.size > MAX_FOTO_BYTES) {
      setError('La imagen es demasiado grande (máx. ~1.3 MB).');
      return;
    }
    const reader = new FileReader();
    reader.onload = () => setForm((f) => ({ ...f, foto: reader.result }));
    reader.readAsDataURL(file);
  };

  const guardar = async (e) => {
    e.preventDefault();
    setError('');

    if (!form.email.trim() || !form.carrera.trim() || !form.fechaNacimiento) {
      setError('Email, carrera y fecha de nacimiento son obligatorios.');
      return;
    }
    if (!/^[A-Za-z0-9._%+-]+@(uni\.pe|uni\.edu\.pe)$/.test(form.email.trim())) {
      setError('El email debe terminar en @uni.pe o @uni.edu.pe.');
      return;
    }
    if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/.test(form.carrera.trim())) {
      setError('La carrera solo puede contener letras y espacios.');
      return;
    }
    const edad = edadDesde(form.fechaNacimiento);
    if (!Number.isInteger(edad) || edad < 18 || edad > 100) {
      setError('Debes tener entre 18 y 100 años según tu fecha de nacimiento.');
      return;
    }

    setGuardando(true);
    try {
      const actualizado = await actualizarPerfil({
        email: form.email.trim(),
        carrera: form.carrera.trim(),
        fechaNacimiento: form.fechaNacimiento,
        foto: form.foto, // null = sin cambios; '' = quitar; dataURL = nueva
      });
      onSaved(actualizado);
    } catch (err) {
      setError(mensajeError(err, 'No se pudo guardar el perfil.'));
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="modal__overlay" onClick={onClose}>
      <form className="modal" onClick={(e) => e.stopPropagation()} onSubmit={guardar}>
        <div className="modal__header">Editar Perfil</div>
        <div className="modal__body">
          {error && <div className="modal__error">{error}</div>}

          <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 8 }}>
            <Avatar nombre={perfil.nombre} foto={form.foto || null} size={72} />
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              <label className="btn btn--ghost btn--sm" style={{ cursor: 'pointer' }}>
                Subir foto
                <input type="file" accept="image/*" onChange={elegirFoto} style={{ display: 'none' }} />
              </label>
              {form.foto && (
                <button
                  type="button"
                  className="btn btn--ghost btn--sm"
                  onClick={() => setForm((f) => ({ ...f, foto: '' }))}
                >
                  Quitar foto
                </button>
              )}
            </div>
          </div>

          <label className="field">
            <span className="field__label">Email</span>
            <input className="field__input" type="email" placeholder="usuario@uni.pe"
              value={form.email} onChange={set('email')} />
          </label>
          <label className="field">
            <span className="field__label">Carrera</span>
            <input className="field__input" list="carreras-perfil"
              value={form.carrera} onChange={set('carrera')} />
            <datalist id="carreras-perfil">
              {CARRERAS.map((c) => <option key={c} value={c} />)}
            </datalist>
          </label>
          <label className="field">
            <span className="field__label">Fecha de nacimiento</span>
            <input className="field__input" type="date"
              max={new Date().toISOString().slice(0, 10)}
              value={form.fechaNacimiento} onChange={set('fechaNacimiento')} />
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

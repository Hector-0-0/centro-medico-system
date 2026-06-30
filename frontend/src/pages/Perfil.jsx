import { useEffect, useState } from 'react';
import {
  obtenerPerfil,
  actualizarPerfil,
  actualizarPerfilDoctor,
  actualizarPerfilEmpleado,
} from '../services/perfilService';
import { mensajeError } from '../services/api';
import { TIPOS_SANGRE } from '../constants/catalogos';

const MAX_FOTO_BYTES = 1_300_000; // ~1.3 MB

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

/**
 * Fila de dato de solo lectura. Si está vacío se oculta, salvo que sea un campo
 * editable (`editable`), en cuyo caso se muestra "No especificado" como pista.
 */
function Linea({ label, value, editable }) {
  if (value == null || value === '') {
    if (!editable) return null;
    return (
      <p className="perfil__linea">
        <strong>{label}:</strong> <span style={{ color: '#94a3b8', fontStyle: 'italic' }}>No especificado</span>
      </p>
    );
  }
  return <p className="perfil__linea"><strong>{label}:</strong> {value}</p>;
}

/** Lee un archivo de imagen a data URL, validando tipo y tamaño. */
function leerFoto(e, setFoto, setError) {
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
  reader.onload = () => setFoto(reader.result);
  reader.readAsDataURL(file);
}

/** Avatar + controles de foto reutilizables en los modales. */
function FotoControls({ nombre, foto, setFoto, setError }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 8 }}>
      <Avatar nombre={nombre} foto={foto || null} size={72} />
      <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
        <label className="btn btn--ghost btn--sm" style={{ cursor: 'pointer' }}>
          Subir foto
          <input type="file" accept="image/*" onChange={(e) => leerFoto(e, setFoto, setError)} style={{ display: 'none' }} />
        </label>
        {foto && (
          <button type="button" className="btn btn--ghost btn--sm" onClick={() => setFoto('')}>
            Quitar foto
          </button>
        )}
      </div>
    </div>
  );
}

/** Sección de verificación: contraseña actual (obligatoria) + nueva (opcional). */
function SeccionCredenciales({ actual, setActual, nueva, setNueva, confirmar, setConfirmar }) {
  const [ver, setVer] = useState(false);
  const tipo = ver ? 'text' : 'password';
  return (
    <div className="card-section" style={{ padding: 12, marginTop: 4 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 8, marginBottom: 4 }}>
        <p className="perfil__linea" style={{ margin: 0 }}>
          <strong>Seguridad</strong> — confirma tu contraseña actual para guardar.
        </p>
        <button type="button" className="btn btn--ghost btn--sm" onClick={() => setVer((v) => !v)}>
          {ver ? 'Ocultar' : 'Mostrar'}
        </button>
      </div>
      <label className="field">
        <span className="field__label">Contraseña actual *</span>
        <input className="field__input" type={tipo} value={actual}
          onChange={(e) => setActual(e.target.value)} autoComplete="current-password" />
      </label>
      <label className="field">
        <span className="field__label">Nueva contraseña (opcional)</span>
        <input className="field__input" type={tipo} value={nueva}
          onChange={(e) => setNueva(e.target.value)} placeholder="Déjalo vacío para no cambiarla"
          autoComplete="new-password" />
      </label>
      {nueva && (
        <label className="field">
          <span className="field__label">Confirmar nueva contraseña</span>
          <input className="field__input" type={tipo} value={confirmar}
            onChange={(e) => setConfirmar(e.target.value)} autoComplete="new-password" />
        </label>
      )}
    </div>
  );
}

/** Estado + validación + payload de credenciales, compartido por los tres modales. */
function useCredenciales() {
  const [actual, setActual] = useState('');
  const [nueva, setNueva] = useState('');
  const [confirmar, setConfirmar] = useState('');

  const validar = () => {
    if (!actual.trim()) return 'Debes ingresar tu contraseña actual para guardar los cambios.';
    if (nueva) {
      if (nueva.length < 4) return 'La nueva contraseña debe tener al menos 4 caracteres.';
      if (nueva === actual) return 'La nueva contraseña debe ser distinta de la actual.';
      if (nueva !== confirmar) return 'La confirmación no coincide con la nueva contraseña.';
    }
    return null;
  };

  return {
    props: { actual, setActual, nueva, setNueva, confirmar, setConfirmar },
    validar,
    payload: { passwordActual: actual, ...(nueva ? { passwordNueva: nueva } : {}) },
  };
}

/** Mi Perfil — datos + estadísticas, editable por todos los roles. */
export default function Perfil() {
  const [p, setP] = useState(null);
  const [error, setError] = useState('');
  const [editando, setEditando] = useState(false);

  const cargar = () => {
    setError('');
    obtenerPerfil()
      .then(setP)
      .catch((err) => setError(mensajeError(err, 'No se pudo cargar el perfil.')));
  };

  useEffect(() => {
    cargar();
  }, []);

  if (error) {
    return (
      <div>
        <h1 className="panel__title">Mi Perfil</h1>
        <div className="card-section">
          <p className="panel__hint table__empty--error" style={{ marginBottom: 12 }}>{error}</p>
          <button className="btn btn--primary" onClick={cargar}>Reintentar</button>
        </div>
      </div>
    );
  }

  if (!p) {
    return (
      <div>
        <h1 className="panel__title">Mi Perfil</h1>
        <p className="panel__hint">Cargando…</p>
      </div>
    );
  }

  const tieneStats = p.totalCitas !== undefined;
  const cerrarGuardado = (actualizado) => {
    setP(actualizado);
    setEditando(false);
  };

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
              <p className="perfil__linea" style={{ margin: 0 }}>
                {p.rol === 'ESTUDIANTE' ? 'Código UNI' : p.rol === 'DOCTOR' ? 'Código (DNI)' : 'Código'}: {p.id}
              </p>
            </div>
          </div>

          {p.rol === 'ESTUDIANTE' && (
            <>
              <Linea label="DNI" value={p.dni} />
              <Linea label="Carrera" value={p.carrera} />
              <Linea label="Email" value={p.email} />
              <Linea
                label="Fecha de nacimiento"
                value={p.fechaNacimiento ? `${p.fechaNacimiento}${p.edad != null ? ` (${p.edad} años)` : ''}` : null}
              />
              <Linea label="Teléfono" value={p.telefono} editable />
              <Linea label="Tipo de sangre" value={p.tipoSangre} editable />
              <Linea label="Alergias" value={p.alergias} editable />
              <Linea label="Contacto de emergencia" value={p.contactoEmergencia} editable />
            </>
          )}

          {p.rol === 'DOCTOR' && (
            <>
              <Linea label="Especialidad" value={p.especialidad} />
              <Linea label="Consultorio" value={p.consultorio} editable />
              <Linea label="Estado" value={p.activo ? 'Activo' : 'Inactivo'} />
              <Linea label="Teléfono" value={p.telefono} editable />
              <Linea label="Email" value={p.email} editable />
            </>
          )}

          {(p.rol === 'ADMIN' || p.rol === 'FARMACIA') && (
            <>
              <Linea label="Rol" value={p.rol} />
              <Linea label="Teléfono" value={p.telefono} editable />
              <Linea label="Email" value={p.email} editable />
            </>
          )}

          <button className="btn btn--primary" style={{ marginTop: 12 }} onClick={() => setEditando(true)}>
            Editar Perfil
          </button>
        </div>

        {/* Estadísticas de citas */}
        {tieneStats && (
          <div className="card-section">
            <h2 className="card-section__title">Resumen de citas</h2>
            <div className="perfil__stat"><span>Total de citas</span><strong>{p.totalCitas}</strong></div>
            <div className="perfil__stat"><span>Citas pendientes</span><strong>{p.citasPendientes}</strong></div>
            <div className="perfil__stat"><span>Citas atendidas</span><strong>{p.citasAtendidas}</strong></div>
          </div>
        )}
      </div>

      {editando && p.rol === 'ESTUDIANTE' && (
        <EditarPerfilEstudianteModal perfil={p} onClose={() => setEditando(false)} onSaved={cerrarGuardado} />
      )}
      {editando && p.rol === 'DOCTOR' && (
        <EditarPerfilDoctorModal perfil={p} onClose={() => setEditando(false)} onSaved={cerrarGuardado} />
      )}
      {editando && (p.rol === 'ADMIN' || p.rol === 'FARMACIA') && (
        <EditarPerfilEmpleadoModal perfil={p} onClose={() => setEditando(false)} onSaved={cerrarGuardado} />
      )}
    </div>
  );
}

function EditarPerfilEstudianteModal({ perfil, onClose, onSaved }) {
  const [form, setForm] = useState({
    email: perfil.email || '',
    telefono: perfil.telefono || '',
    tipoSangre: perfil.tipoSangre || '',
    alergias: perfil.alergias || '',
    contactoEmergencia: perfil.contactoEmergencia || '',
    foto: perfil.foto || null, // null = sin cambios; '' = quitar; dataURL = nueva
  });
  const cred = useCredenciales();
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');

    if (!form.email.trim()) {
      setError('El email es obligatorio.');
      return;
    }
    if (!/^[A-Za-z0-9._%+-]+@(uni\.pe|uni\.edu\.pe)$/.test(form.email.trim())) {
      setError('El email debe terminar en @uni.pe o @uni.edu.pe.');
      return;
    }
    if (form.telefono.trim() && !/^\d{6,15}$/.test(form.telefono.trim())) {
      setError('El teléfono debe tener entre 6 y 15 dígitos.');
      return;
    }
    const errCred = cred.validar();
    if (errCred) { setError(errCred); return; }

    setGuardando(true);
    try {
      const actualizado = await actualizarPerfil({
        email: form.email.trim(),
        telefono: form.telefono.trim(),
        tipoSangre: form.tipoSangre,
        alergias: form.alergias.trim(),
        contactoEmergencia: form.contactoEmergencia.trim(),
        foto: form.foto,
        ...cred.payload,
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

          <FotoControls nombre={perfil.nombre} foto={form.foto}
            setFoto={(v) => setForm((f) => ({ ...f, foto: v }))} setError={setError} />

          <p className="perfil__linea" style={{ marginTop: 0, fontSize: 12.5, color: '#64748b' }}>
            La carrera y la fecha de nacimiento las gestiona el administrador.
          </p>
          <label className="field">
            <span className="field__label">Email</span>
            <input className="field__input" type="email" placeholder="usuario@uni.pe"
              value={form.email} onChange={set('email')} />
          </label>
          <label className="field">
            <span className="field__label">Teléfono</span>
            <input className="field__input" placeholder="Solo dígitos" inputMode="numeric" maxLength={15}
              value={form.telefono}
              onChange={(e) => setForm((f) => ({ ...f, telefono: e.target.value.replace(/\D/g, '') }))} />
          </label>
          <label className="field">
            <span className="field__label">Tipo de sangre</span>
            <select className="field__input" value={form.tipoSangre} onChange={set('tipoSangre')}>
              <option value="">Sin especificar</option>
              {TIPOS_SANGRE.map((t) => <option key={t} value={t}>{t}</option>)}
            </select>
          </label>
          <label className="field">
            <span className="field__label">Alergias</span>
            <textarea className="field__input" rows={2} placeholder="Ej. Penicilina, mariscos…"
              value={form.alergias} onChange={set('alergias')} maxLength={255} />
          </label>
          <label className="field">
            <span className="field__label">Contacto de emergencia</span>
            <input className="field__input" placeholder="Nombre y teléfono"
              value={form.contactoEmergencia} onChange={set('contactoEmergencia')} maxLength={150} />
          </label>

          <SeccionCredenciales {...cred.props} />
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

function EditarPerfilDoctorModal({ perfil, onClose, onSaved }) {
  const [form, setForm] = useState({
    consultorio: perfil.consultorio || '',
    telefono: perfil.telefono || '',
    email: perfil.email || '',
    foto: perfil.foto || null,
  });
  const cred = useCredenciales();
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');
    if (!form.consultorio.trim()) {
      setError('El consultorio es obligatorio.');
      return;
    }
    if (form.telefono.trim() && !/^\d{6,15}$/.test(form.telefono.trim())) {
      setError('El teléfono debe tener entre 6 y 15 dígitos.');
      return;
    }
    if (form.email.trim() && !/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(form.email.trim())) {
      setError('El email no es válido.');
      return;
    }
    const errCred = cred.validar();
    if (errCred) { setError(errCred); return; }

    setGuardando(true);
    try {
      const actualizado = await actualizarPerfilDoctor({
        consultorio: form.consultorio.trim(),
        telefono: form.telefono.trim(),
        email: form.email.trim(),
        foto: form.foto,
        ...cred.payload,
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

          <FotoControls nombre={perfil.nombre} foto={form.foto}
            setFoto={(v) => setForm((f) => ({ ...f, foto: v }))} setError={setError} />

          <p className="perfil__linea"><strong>Especialidad:</strong> {perfil.especialidad}</p>
          <label className="field">
            <span className="field__label">Consultorio</span>
            <input className="field__input" value={form.consultorio} onChange={set('consultorio')} />
          </label>
          <label className="field">
            <span className="field__label">Teléfono</span>
            <input className="field__input" placeholder="Solo dígitos" inputMode="numeric" maxLength={15}
              value={form.telefono}
              onChange={(e) => setForm((f) => ({ ...f, telefono: e.target.value.replace(/\D/g, '') }))} />
          </label>
          <label className="field">
            <span className="field__label">Email</span>
            <input className="field__input" type="email" value={form.email} onChange={set('email')} />
          </label>

          <SeccionCredenciales {...cred.props} />
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

function EditarPerfilEmpleadoModal({ perfil, onClose, onSaved }) {
  const [form, setForm] = useState({
    telefono: perfil.telefono || '',
    email: perfil.email || '',
    foto: perfil.foto || null,
  });
  const cred = useCredenciales();
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);

  const set = (campo) => (e) => setForm((f) => ({ ...f, [campo]: e.target.value }));

  const guardar = async (e) => {
    e.preventDefault();
    setError('');
    if (form.telefono.trim() && !/^\d{6,15}$/.test(form.telefono.trim())) {
      setError('El teléfono debe tener entre 6 y 15 dígitos.');
      return;
    }
    if (form.email.trim() && !/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(form.email.trim())) {
      setError('El email no es válido.');
      return;
    }
    const errCred = cred.validar();
    if (errCred) { setError(errCred); return; }

    setGuardando(true);
    try {
      const actualizado = await actualizarPerfilEmpleado({
        telefono: form.telefono.trim(),
        email: form.email.trim(),
        foto: form.foto,
        ...cred.payload,
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

          <FotoControls nombre={perfil.nombre} foto={form.foto}
            setFoto={(v) => setForm((f) => ({ ...f, foto: v }))} setError={setError} />

          <p className="perfil__linea"><strong>Rol:</strong> {perfil.rol}</p>
          <label className="field">
            <span className="field__label">Teléfono</span>
            <input className="field__input" placeholder="Solo dígitos" inputMode="numeric" maxLength={15}
              value={form.telefono}
              onChange={(e) => setForm((f) => ({ ...f, telefono: e.target.value.replace(/\D/g, '') }))} />
          </label>
          <label className="field">
            <span className="field__label">Email</span>
            <input className="field__input" type="email" value={form.email} onChange={set('email')} />
          </label>

          <SeccionCredenciales {...cred.props} />
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

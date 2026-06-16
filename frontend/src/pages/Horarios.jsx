import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import { disponibilidadService, pacienteService } from '../services/servicios';
import { getRol, getPacienteId } from '../services/authService';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());

const s = {
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b' },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 20 },
  filtros: { display: 'flex', gap: 10, marginBottom: 16, alignItems: 'center' },
  select: { padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, background: '#fff', outline: 'none' },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  btnAgendar: { padding: '6px 14px', background: '#711610', color: '#fff', border: 'none', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer' },
  badge: { display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: '#dcfce7', color: '#166534' },
  overlay: { position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { background: '#fff', borderRadius: 16, padding: 32, width: 460, boxShadow: '0 20px 60px rgba(0,0,0,0.2)' },
  modalTit: { fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 6 },
  modalSub: { fontSize: 13, color: '#64748b', marginBottom: 20 },
  campo: { marginBottom: 16 },
  label: { display: 'block', fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 6 },
  input: { width: '100%', padding: '9px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, outline: 'none', boxSizing: 'border-box' },
  btnsFoot: { display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 24 },
  btnCancelar: { padding: '10px 20px', background: '#f1e9e2', color: '#475569', border: 'none', borderRadius: 8, fontSize: 14, cursor: 'pointer' },
  btnPrimario: { padding: '10px 20px', background: '#711610', color: '#fff', border: 'none', borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer' },
  error: { background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#dc2626', marginBottom: 16 },
  ok: { background: '#f0fdf4', border: '1px solid #bbf7d0', borderRadius: 8, padding: '10px 12px', fontSize: 13, color: '#166534', marginBottom: 16 },
};

// Día español (BACK) → índice JS getDay() (Domingo=0 … Sábado=6)
const DIA_INDEX = { DOMINGO: 0, LUNES: 1, MARTES: 2, MIERCOLES: 3, JUEVES: 4, VIERNES: 5, SABADO: 6 };

/** Próxima fecha (YYYY-MM-DD) que cae en el día de la semana indicado. */
const proximaFecha = (diaSemana) => {
  const objetivo = DIA_INDEX[(diaSemana || '').toUpperCase()];
  if (objetivo === undefined) return '';
  const hoy = new Date();
  let diff = (objetivo - hoy.getDay() + 7) % 7;
  if (diff === 0) diff = 7; // siempre a futuro
  const d = new Date(hoy); d.setDate(hoy.getDate() + diff);
  return d.toISOString().slice(0, 10);
};

export default function Horarios() {
  const rol = getRol();
  const esPaciente = rol === 'PACIENTE';

  const [horarios, setHorarios] = useState([]);
  const [pacientes, setPacientes] = useState([]);
  const [especialidad, setEspecialidad] = useState('');
  const [buscar, setBuscar] = useState('');
  const [agendar, setAgendar] = useState(null); // { disp, fecha, pacienteId, motivo }
  const [error, setError] = useState('');
  const [ok, setOk] = useState('');
  const [guardando, setGuardando] = useState(false);

  const cargar = async () => {
    try {
      setHorarios((await disponibilidadService.listar()).data);
      if (!esPaciente) setPacientes((await pacienteService.listar()).data);
    } catch (e) { console.error(e); }
  };
  useEffect(() => { cargar(); /* eslint-disable-next-line */ }, []);

  const especialidades = [...new Set(horarios.map(h => h.medico?.especialidad?.nombre).filter(Boolean))];
  const visibles = horarios.filter(h =>
    (!especialidad || h.medico?.especialidad?.nombre === especialidad) &&
    (!buscar || incluye(buscar, h.medico?.nombre, h.medico?.apellido, h.medico?.especialidad?.nombre, h.diaSemana, h.consultorio))
  );

  const abrirAgendar = (disp) => {
    setError(''); setOk('');
    setAgendar({ disp, fecha: proximaFecha(disp.diaSemana), pacienteId: esPaciente ? getPacienteId() : '', motivo: '' });
  };

  const confirmar = async (e) => {
    e.preventDefault(); setGuardando(true); setError(''); setOk('');
    try {
      await disponibilidadService.agendar({
        disponibilidadId: agendar.disp.id,
        pacienteId: Number(agendar.pacienteId),
        fecha: agendar.fecha,
        motivo: agendar.motivo,
      });
      setAgendar(null);
      setOk('✅ Cita agendada correctamente. Revísala en "Mis Citas".');
    } catch (err) {
      setError(err.response?.data?.error || err.response?.data?.message || 'No se pudo agendar la cita');
    } finally { setGuardando(false); }
  };

  const hora = (h) => (h ? h.slice(0, 5) : '');

  return (
    <Layout titulo="Horarios disponibles">
      <div style={s.header}><div style={s.titulo}>🗓️ Horarios disponibles</div></div>
      <div style={s.subtitulo}>Selecciona un horario y agenda tu cita</div>

      {ok && <div style={s.ok}>{ok}</div>}

      <div style={s.filtros}>
        <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por médico, día o consultorio..." ancho={320} />
        <select style={s.select} value={especialidad} onChange={e => setEspecialidad(e.target.value)}>
          <option value="">Todas las especialidades</option>
          {especialidades.map(e => <option key={e} value={e}>{e}</option>)}
        </select>
      </div>

      <table style={s.tabla}>
        <thead>
          <tr>
            <th style={s.th}>Especialidad</th>
            <th style={s.th}>Médico</th>
            <th style={s.th}>Día</th>
            <th style={s.th}>Horario</th>
            <th style={s.th}>Consultorio</th>
            <th style={s.th}>Acción</th>
          </tr>
        </thead>
        <tbody>
          {visibles.length === 0 ? (
            <tr><td colSpan={6} style={{ ...s.td, textAlign: 'center', color: '#94a3b8', padding: 40 }}>No hay horarios disponibles</td></tr>
          ) : visibles.map(h => (
            <tr key={h.id}>
              <td style={s.td}>{h.medico?.especialidad?.nombre || '—'}</td>
              <td style={s.td}>Dr. {h.medico?.nombre} {h.medico?.apellido}</td>
              <td style={s.td}>{h.diaSemana}</td>
              <td style={s.td}>{hora(h.horaInicio)} – {hora(h.horaFin)}</td>
              <td style={s.td}>{h.consultorio || '—'}</td>
              <td style={s.td}><button style={s.btnAgendar} onClick={() => abrirAgendar(h)}>Agendar</button></td>
            </tr>
          ))}
        </tbody>
      </table>

      {agendar && (
        <div style={s.overlay} onClick={e => e.target === e.currentTarget && setAgendar(null)}>
          <div style={s.modal}>
            <div style={s.modalTit}>Agendar cita</div>
            <div style={s.modalSub}>
              Dr. {agendar.disp.medico?.nombre} {agendar.disp.medico?.apellido} · {agendar.disp.diaSemana} {hora(agendar.disp.horaInicio)}
            </div>
            {error && <div style={s.error}>{error}</div>}
            <form onSubmit={confirmar}>
              {!esPaciente && (
                <div style={s.campo}>
                  <label style={s.label}>Paciente *</label>
                  <select style={{ ...s.input, background: '#fff' }} required value={agendar.pacienteId}
                          onChange={e => setAgendar(a => ({ ...a, pacienteId: e.target.value }))}>
                    <option value="">— Seleccionar paciente —</option>
                    {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido} ({p.dni})</option>)}
                  </select>
                </div>
              )}
              <div style={s.campo}>
                <label style={s.label}>Fecha ({agendar.disp.diaSemana}) *</label>
                <input style={s.input} type="date" required value={agendar.fecha}
                       onChange={e => setAgendar(a => ({ ...a, fecha: e.target.value }))} />
              </div>
              <div style={s.campo}>
                <label style={s.label}>Motivo</label>
                <input style={s.input} placeholder="Motivo de la consulta" value={agendar.motivo}
                       onChange={e => setAgendar(a => ({ ...a, motivo: e.target.value }))} />
              </div>
              <div style={s.btnsFoot}>
                <button type="button" style={s.btnCancelar} onClick={() => setAgendar(null)}>Cancelar</button>
                <button type="submit" style={s.btnPrimario} disabled={guardando}>{guardando ? 'Agendando...' : 'Confirmar cita'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </Layout>
  );
}

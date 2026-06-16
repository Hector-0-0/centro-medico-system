import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { citaService, pacienteService, medicamentoService } from '../services/servicios';
import { getRol, getPacienteId, getMedicoId } from '../services/authService';

const s = {
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b', marginBottom: 6 },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 24 },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 16, marginBottom: 32 },
  tarjeta: { background: '#fff', borderRadius: 12, padding: '20px 24px', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', border: '1px solid #e8ddd8' },
  tarjetaNum: { fontSize: 32, fontWeight: 700, color: '#711610', marginBottom: 4 },
  tarjetaLabel: { fontSize: 13, color: '#64748b' },
  seccion: { marginBottom: 32 },
  seccionTit: { fontSize: 16, fontWeight: 600, color: '#1e293b', marginBottom: 12 },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 700, color: '#8b1414', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e8ddd8', background: '#f4ece4' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1e9e2' },
  badge: (color) => ({
    display: 'inline-block', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600,
    background: color === 'verde' ? '#dcfce7' : color === 'rojo' ? '#fee2e2' : color === 'amarillo' ? '#fef3c7' : '#fbece9',
    color: color === 'verde' ? '#166534' : color === 'rojo' ? '#dc2626' : color === 'amarillo' ? '#92400e' : '#711610',
  }),
  alerta: { background: '#fef3c7', border: '1px solid #fcd34d', borderRadius: 10, padding: '12px 16px', marginBottom: 8, fontSize: 14, color: '#78350f', display: 'flex', alignItems: 'center', gap: 8 },
  vacio: { background: '#fff', borderRadius: 12, padding: 40, textAlign: 'center', color: '#94a3b8', border: '1px solid #e8ddd8' },
};

const COLORES_ESTADO = { PENDIENTE: 'amarillo', REPROGRAMADA: 'amarillo', ATENDIDA: 'verde', CANCELADA: 'rojo' };

const fmtFecha = (f) => new Date(f).toLocaleString('es-PE', { dateStyle: 'short', timeStyle: 'short' });
const fmtHora  = (f) => new Date(f).toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' });

export default function Dashboard() {
  const rol = getRol();
  const esAdmin = rol === 'ADMIN' || rol === 'RECEPCIONISTA';

  const [citas, setCitas] = useState([]);
  const [totalPacientes, setTotalPacientes] = useState(0);
  const [stockBajo, setStockBajo] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const cargar = async () => {
      try {
        if (esAdmin) {
          const [resCitas, resPacientes, resStock] = await Promise.all([
            citaService.listarHoy(), pacienteService.listar(), medicamentoService.stockBajo(),
          ]);
          setCitas(resCitas.data);
          setTotalPacientes(resPacientes.data.length);
          setStockBajo(resStock.data);
        } else if (rol === 'MEDICO') {
          const id = getMedicoId();
          if (id) setCitas((await citaService.listarPorMedico(id)).data);
        } else if (rol === 'PACIENTE') {
          const id = getPacienteId();
          if (id) setCitas((await citaService.listarPorPaciente(id)).data);
        }
      } catch (e) {
        console.error('Error cargando dashboard', e);
      } finally {
        setCargando(false);
      }
    };
    cargar();
  }, [esAdmin, rol]);

  if (cargando) return <Layout titulo="Panel de control"><div style={{ padding: 40, color: '#64748b' }}>Cargando...</div></Layout>;

  const pendientes = citas.filter(c => c.estado === 'PENDIENTE' || c.estado === 'REPROGRAMADA').length;
  const atendidas  = citas.filter(c => c.estado === 'ATENDIDA').length;

  // ─── Vista ADMIN / RECEPCIONISTA ──────────────────────────────────────────
  if (esAdmin) {
    return (
      <Layout titulo="Panel de control">
        <div style={s.titulo}>🏠 Panel de control</div>
        <div style={s.subtitulo}>Resumen general del Centro Médico</div>

        <div style={s.grid}>
          <div style={s.tarjeta}><div style={s.tarjetaNum}>{citas.length}</div><div style={s.tarjetaLabel}>📅 Citas hoy</div></div>
          <div style={s.tarjeta}><div style={s.tarjetaNum}>{pendientes}</div><div style={s.tarjetaLabel}>⏳ Pendientes hoy</div></div>
          <div style={s.tarjeta}><div style={s.tarjetaNum}>{totalPacientes}</div><div style={s.tarjetaLabel}>👥 Pacientes registrados</div></div>
          <div style={s.tarjeta}><div style={{ ...s.tarjetaNum, color: stockBajo.length > 0 ? '#dc2626' : '#711610' }}>{stockBajo.length}</div><div style={s.tarjetaLabel}>💊 Stock bajo</div></div>
        </div>

        {stockBajo.length > 0 && (
          <div style={s.seccion}>
            <div style={s.seccionTit}>⚠️ Alertas de stock</div>
            {stockBajo.map(m => (
              <div key={m.id} style={s.alerta}>⚠️ <strong>{m.nombre}</strong> — Stock actual: {m.stockActual} (mínimo: {m.stockMinimo})</div>
            ))}
          </div>
        )}

        <div style={s.seccion}>
          <div style={s.seccionTit}>📅 Citas de hoy ({citas.length})</div>
          {citas.length === 0 ? <div style={s.vacio}>No hay citas programadas para hoy</div> : (
            <table style={s.tabla}>
              <thead><tr><th style={s.th}>Hora</th><th style={s.th}>Paciente</th><th style={s.th}>Médico</th><th style={s.th}>Motivo</th><th style={s.th}>Estado</th></tr></thead>
              <tbody>
                {citas.map(c => (
                  <tr key={c.id}>
                    <td style={s.td}>{fmtHora(c.fechaHora)}</td>
                    <td style={s.td}>{c.paciente?.nombre} {c.paciente?.apellido}</td>
                    <td style={s.td}>Dr. {c.medico?.nombre} {c.medico?.apellido}</td>
                    <td style={s.td}>{c.motivo || '—'}</td>
                    <td style={s.td}><span style={s.badge(COLORES_ESTADO[c.estado])}>{c.estado}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </Layout>
    );
  }

  // ─── Vista MÉDICO / PACIENTE (su propia agenda) ───────────────────────────
  const esMedico = rol === 'MEDICO';
  const proximas = [...citas].sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora));

  return (
    <Layout titulo={esMedico ? 'Mi agenda' : 'Mi panel'}>
      <div style={s.titulo}>{esMedico ? '🩺 Mi agenda' : '🏠 Mi panel'}</div>
      <div style={s.subtitulo}>{esMedico ? 'Tus citas y consultas' : 'Tus próximas citas en el Centro Médico'}</div>

      <div style={s.grid}>
        <div style={s.tarjeta}><div style={s.tarjetaNum}>{citas.length}</div><div style={s.tarjetaLabel}>📅 Citas totales</div></div>
        <div style={s.tarjeta}><div style={{ ...s.tarjetaNum, color: '#92400e' }}>{pendientes}</div><div style={s.tarjetaLabel}>⏳ Pendientes</div></div>
        <div style={s.tarjeta}><div style={{ ...s.tarjetaNum, color: '#166534' }}>{atendidas}</div><div style={s.tarjetaLabel}>✅ Atendidas</div></div>
      </div>

      <div style={s.seccion}>
        <div style={s.seccionTit}>📅 Próximas citas</div>
        {proximas.length === 0 ? <div style={s.vacio}>No tienes citas registradas</div> : (
          <table style={s.tabla}>
            <thead><tr><th style={s.th}>Fecha y hora</th><th style={s.th}>{esMedico ? 'Paciente' : 'Médico'}</th><th style={s.th}>Motivo</th><th style={s.th}>Estado</th></tr></thead>
            <tbody>
              {proximas.map(c => (
                <tr key={c.id}>
                  <td style={s.td}>{fmtFecha(c.fechaHora)}</td>
                  <td style={s.td}>{esMedico ? `${c.paciente?.nombre || ''} ${c.paciente?.apellido || ''}` : `Dr. ${c.medico?.nombre || ''} ${c.medico?.apellido || ''}`}</td>
                  <td style={s.td}>{c.motivo || '—'}</td>
                  <td style={s.td}><span style={s.badge(COLORES_ESTADO[c.estado])}>{c.estado}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </Layout>
  );
}

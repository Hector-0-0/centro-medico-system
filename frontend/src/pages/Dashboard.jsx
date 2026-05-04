import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { citaService, pacienteService, medicamentoService } from '../services/servicios';

const s = {
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b', marginBottom: 24 },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 16, marginBottom: 32 },
  tarjeta: {
    background: '#fff', borderRadius: 12, padding: '20px 24px',
    boxShadow: '0 1px 3px rgba(0,0,0,0.08)', border: '1px solid #e2e8f0',
  },
  tarjetaNum: { fontSize: 32, fontWeight: 700, color: '#1a73e8', marginBottom: 4 },
  tarjetaLabel: { fontSize: 13, color: '#64748b' },
  seccion: { marginBottom: 32 },
  seccionTit: { fontSize: 16, fontWeight: 600, color: '#1e293b', marginBottom: 12 },
  tabla: { width: '100%', background: '#fff', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.08)', borderCollapse: 'collapse' },
  th: { padding: '12px 16px', textAlign: 'left', fontSize: 12, fontWeight: 600, color: '#64748b', textTransform: 'uppercase', letterSpacing: 0.5, borderBottom: '1px solid #e2e8f0', background: '#f8fafc' },
  td: { padding: '12px 16px', fontSize: 14, color: '#374151', borderBottom: '1px solid #f1f5f9' },
  badge: (color) => ({
    display: 'inline-block', padding: '3px 10px', borderRadius: 20,
    fontSize: 12, fontWeight: 600,
    background: color === 'verde' ? '#dcfce7' : color === 'rojo' ? '#fee2e2' : color === 'amarillo' ? '#fef3c7' : '#e0f2fe',
    color: color === 'verde' ? '#166534' : color === 'rojo' ? '#dc2626' : color === 'amarillo' ? '#92400e' : '#0369a1',
  }),
  alerta: {
    background: '#fef3c7', border: '1px solid #fcd34d', borderRadius: 10,
    padding: '12px 16px', marginBottom: 8, fontSize: 14, color: '#78350f',
    display: 'flex', alignItems: 'center', gap: 8,
  },
};

const COLORES_ESTADO = { PENDIENTE: 'azul', ATENDIDA: 'verde', CANCELADA: 'rojo', REPROGRAMADA: 'amarillo' };

export default function Dashboard() {
  const [citasHoy, setCitasHoy] = useState([]);
  const [totalPacientes, setTotalPacientes] = useState(0);
  const [stockBajo, setStockBajo] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const cargar = async () => {
      try {
        const [resCitas, resPacientes, resStock] = await Promise.all([
          citaService.listarHoy(),
          pacienteService.listar(),
          medicamentoService.stockBajo(),
        ]);
        setCitasHoy(resCitas.data);
        setTotalPacientes(resPacientes.data.length);
        setStockBajo(resStock.data);
      } catch (e) {
        console.error('Error cargando dashboard', e);
      } finally {
        setCargando(false);
      }
    };
    cargar();
  }, []);

  const pendientes = citasHoy.filter(c => c.estado === 'PENDIENTE').length;
  const atendidas = citasHoy.filter(c => c.estado === 'ATENDIDA').length;

  if (cargando) return <Layout><div style={{ padding: 40, color: '#64748b' }}>Cargando...</div></Layout>;

  return (
    <Layout>
      <div style={s.titulo}>🏠 Panel de control</div>

      {/* Métricas */}
      <div style={s.grid}>
        <div style={s.tarjeta}>
          <div style={s.tarjetaNum}>{citasHoy.length}</div>
          <div style={s.tarjetaLabel}>📅 Citas hoy</div>
        </div>
        <div style={s.tarjeta}>
          <div style={s.tarjetaNum}>{pendientes}</div>
          <div style={s.tarjetaLabel}>⏳ Pendientes hoy</div>
        </div>
        <div style={s.tarjeta}>
          <div style={{ ...s.tarjetaNum, color: '#1d9e75' }}>{totalPacientes}</div>
          <div style={s.tarjetaLabel}>👥 Pacientes registrados</div>
        </div>
        <div style={s.tarjeta}>
          <div style={{ ...s.tarjetaNum, color: stockBajo.length > 0 ? '#e24b4a' : '#1d9e75' }}>{stockBajo.length}</div>
          <div style={s.tarjetaLabel}>💊 Medicamentos con stock bajo</div>
        </div>
      </div>

      {/* Alertas de stock */}
      {stockBajo.length > 0 && (
        <div style={s.seccion}>
          <div style={s.seccionTit}>⚠️ Alertas de stock</div>
          {stockBajo.map(m => (
            <div key={m.id} style={s.alerta}>
              ⚠️ <strong>{m.nombre}</strong> — Stock actual: {m.stockActual} unidades (mínimo: {m.stockMinimo})
            </div>
          ))}
        </div>
      )}

      {/* Citas del día */}
      <div style={s.seccion}>
        <div style={s.seccionTit}>📅 Citas de hoy ({citasHoy.length})</div>
        {citasHoy.length === 0 ? (
          <div style={{ background: '#fff', borderRadius: 12, padding: 40, textAlign: 'center', color: '#94a3b8' }}>
            No hay citas programadas para hoy
          </div>
        ) : (
          <table style={s.tabla}>
            <thead>
              <tr>
                <th style={s.th}>Hora</th>
                <th style={s.th}>Paciente</th>
                <th style={s.th}>Médico</th>
                <th style={s.th}>Motivo</th>
                <th style={s.th}>Estado</th>
              </tr>
            </thead>
            <tbody>
              {citasHoy.map(cita => (
                <tr key={cita.id}>
                  <td style={s.td}>{new Date(cita.fechaHora).toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' })}</td>
                  <td style={s.td}>{cita.paciente?.nombre} {cita.paciente?.apellido}</td>
                  <td style={s.td}>Dr. {cita.medico?.nombre} {cita.medico?.apellido}</td>
                  <td style={s.td}>{cita.motivo || '—'}</td>
                  <td style={s.td}><span style={s.badge(COLORES_ESTADO[cita.estado])}>{cita.estado}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </Layout>
  );
}

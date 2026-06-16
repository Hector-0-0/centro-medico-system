import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import api from '../services/api';
import { citaService } from '../services/servicios';
import { getRol, getPacienteId, getMedicoId } from '../services/authService';

// Paleta institucional UNI (igual al PerfilPanel del desktop)
const GUINDA = '#711610';

const s = {
  grid: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24, alignItems: 'start' },
  card: {
    background: '#fff', borderRadius: 14, padding: 32,
    border: '1px solid #e8ddd8', boxShadow: '0 1px 3px rgba(0,0,0,0.06)',
  },
  avatar: {
    width: 88, height: 88, borderRadius: '50%', margin: '0 auto 16px',
    background: GUINDA, color: '#fff', display: 'flex', alignItems: 'center',
    justifyContent: 'center', fontSize: 34, fontWeight: 700,
  },
  nombre: { fontSize: 20, fontWeight: 800, color: '#1e293b', textAlign: 'center' },
  codigo: { fontSize: 13, color: '#64748b', textAlign: 'center', marginTop: 4 },
  sub: { fontSize: 14, color: '#8b1414', textAlign: 'center', fontWeight: 600, marginTop: 6 },
  sep: { height: 1, background: '#e8ddd8', margin: '22px 0' },
  fila: { display: 'flex', justifyContent: 'space-between', fontSize: 14, padding: '8px 0', color: '#374151' },
  filaLabel: { color: '#64748b' },
  statTit: { fontSize: 16, fontWeight: 800, color: '#1e293b', marginBottom: 20 },
  statNum: (color = GUINDA) => ({ fontSize: 30, fontWeight: 800, color }),
  statRow: { display: 'flex', alignItems: 'baseline', gap: 12, marginBottom: 18 },
  statLabel: { fontSize: 14, color: '#64748b' },
};

export default function Perfil() {
  const [perfil, setPerfil] = useState(null);
  const [citas, setCitas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const rol = getRol();

  useEffect(() => {
    const cargar = async () => {
      try {
        const { data } = await api.get('/auth/me');
        setPerfil(data);

        const pacId = getPacienteId();
        const medId = getMedicoId();
        if (pacId)      setCitas((await citaService.listarPorPaciente(pacId)).data);
        else if (medId) setCitas((await citaService.listarPorMedico(medId)).data);
      } catch (e) {
        console.error('Error cargando perfil', e);
      } finally {
        setCargando(false);
      }
    };
    cargar();
  }, []);

  if (cargando) return <Layout titulo="Mi Perfil"><div style={{ color: '#64748b' }}>Cargando...</div></Layout>;
  if (!perfil)  return <Layout titulo="Mi Perfil"><div style={{ color: '#64748b' }}>No se pudo cargar el perfil.</div></Layout>;

  const nombre = `${perfil.nombre || ''} ${perfil.apellido || ''}`.trim() || perfil.username;
  const iniciales = nombre.split(' ').map(p => p[0]).slice(0, 2).join('').toUpperCase();
  const total      = citas.length;
  const pendientes = citas.filter(c => c.estado === 'PENDIENTE').length;
  const atendidas  = citas.filter(c => c.estado === 'ATENDIDA').length;

  return (
    <Layout titulo="Mi Perfil">
      <div style={s.grid}>
        {/* Card datos personales */}
        <div style={s.card}>
          <div style={s.avatar}>{iniciales}</div>
          <div style={s.nombre}>{nombre}</div>
          <div style={s.codigo}>Código: {perfil.id} · {perfil.username}</div>
          {perfil.especialidad && <div style={s.sub}>Especialidad: {perfil.especialidad}</div>}
          {perfil.grupoSanguineo && <div style={s.sub}>Grupo sanguíneo: {perfil.grupoSanguineo}</div>}

          <div style={s.sep} />

          <div style={s.fila}><span style={s.filaLabel}>Email</span><span>{perfil.email || '—'}</span></div>
          <div style={s.fila}><span style={s.filaLabel}>Rol</span><span>{rol}</span></div>
          {perfil.alergias && (
            <div style={s.fila}><span style={s.filaLabel}>Alergias</span><span>{perfil.alergias}</span></div>
          )}
        </div>

        {/* Card resumen de citas */}
        <div style={s.card}>
          <div style={s.statTit}>Resumen</div>
          <div style={s.statRow}>
            <span style={s.statNum()}>{total}</span>
            <span style={s.statLabel}>Citas totales</span>
          </div>
          <div style={s.statRow}>
            <span style={s.statNum('#92400e')}>{pendientes}</span>
            <span style={s.statLabel}>Citas pendientes</span>
          </div>
          <div style={s.statRow}>
            <span style={s.statNum('#166534')}>{atendidas}</span>
            <span style={s.statLabel}>Citas atendidas</span>
          </div>
        </div>
      </div>
    </Layout>
  );
}

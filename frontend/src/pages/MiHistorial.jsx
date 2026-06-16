import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import Buscador from '../components/Buscador';
import { historialService } from '../services/servicios';
import { getPacienteId, getNombre } from '../services/authService';

const incluye = (txt, ...campos) => campos.join(' ').toLowerCase().includes(txt.trim().toLowerCase());

const s = {
  titulo: { fontSize: 22, fontWeight: 700, color: '#1e293b', marginBottom: 6 },
  subtitulo: { fontSize: 14, color: '#64748b', marginBottom: 28 },
  cards: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: 16 },
  card: { background: '#fff', borderRadius: 12, padding: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.08)', border: '1px solid #e8ddd8' },
  cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16, paddingBottom: 12, borderBottom: '1px solid #f1e9e2' },
  cardFecha: { fontSize: 13, fontWeight: 700, color: '#711610' },
  cardMedico: { fontSize: 14, fontWeight: 600, color: '#1e293b', marginBottom: 2 },
  cardEsp: { fontSize: 12, color: '#94a3b8' },
  seccion: { marginBottom: 14 },
  seccionTit: { fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase', letterSpacing: 0.8, marginBottom: 4 },
  seccionTxt: { fontSize: 14, color: '#374151', lineHeight: 1.6, background: '#f4ece4', padding: '8px 12px', borderRadius: 6, borderLeft: '3px solid #e8ddd8' },
  recetaTxt: { fontSize: 14, color: '#374151', lineHeight: 1.6, background: '#f0fdf4', padding: '8px 12px', borderRadius: 6, borderLeft: '3px solid #86efac' },
  vacio: { background: '#fff', borderRadius: 12, padding: '80px 24px', textAlign: 'center', color: '#94a3b8', boxShadow: '0 1px 3px rgba(0,0,0,0.08)' },
};

export default function MiHistorial() {
  const pacienteId = getPacienteId();
  const [historiales, setHistoriales] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [buscar, setBuscar] = useState('');

  useEffect(() => {
    if (!pacienteId) { setCargando(false); return; }
    historialService.listarPorPaciente(pacienteId)
      .then(r => setHistoriales(r.data))
      .catch(() => setHistoriales([]))
      .finally(() => setCargando(false));
  }, []);

  const formatFecha = (f) => new Date(f).toLocaleDateString('es-PE', { dateStyle: 'long' });

  if (!pacienteId) return (
    <Layout titulo="Mi historial médico">
      <div style={{ background: '#fef3c7', borderRadius: 12, padding: 32, border: '1px solid #fcd34d' }}>
        <div style={{ fontSize: 18, fontWeight: 700, color: '#92400e', marginBottom: 8 }}>⚠️ Perfil incompleto</div>
        <p style={{ color: '#78350f' }}>Tu cuenta no tiene un perfil de paciente asociado. Contacta con administración.</p>
      </div>
    </Layout>
  );

  return (
    <Layout titulo="Mi historial médico">
      <div style={s.titulo}>📋 Mi historial médico</div>
      <div style={s.subtitulo}>Registro completo de tus consultas en el Centro Médico UNI</div>

      {cargando ? (
        <div style={s.vacio}>Cargando tu historial...</div>
      ) : historiales.length === 0 ? (
        <div style={s.vacio}>
          <div style={{ fontSize: 40, marginBottom: 12 }}>📋</div>
          <div style={{ fontSize: 16, fontWeight: 600, marginBottom: 8, color: '#475569' }}>Sin historial médico</div>
          <p>Aún no tienes consultas registradas. Cuando el médico atienda tu cita, aparecerá aquí.</p>
        </div>
      ) : (
        <>
        <div style={{ marginBottom: 16 }}>
          <Buscador value={buscar} onChange={setBuscar} placeholder="Buscar por médico, diagnóstico o tratamiento..." ancho={400} />
        </div>
        <div style={s.cards}>
          {historiales.filter(h => !buscar || incluye(buscar, h.cita?.medico?.nombre, h.cita?.medico?.apellido, h.cita?.medico?.especialidad?.nombre, h.diagnostico, h.tratamiento, h.receta)).map(h => (
            <div key={h.id} style={s.card}>
              <div style={s.cardHeader}>
                <div>
                  <div style={s.cardMedico}>Dr. {h.cita?.medico?.nombre} {h.cita?.medico?.apellido}</div>
                  <div style={s.cardEsp}>{h.cita?.medico?.especialidad?.nombre}</div>
                </div>
                <div style={s.cardFecha}>{formatFecha(h.fechaRegistro)}</div>
              </div>

              {h.diagnostico && (
                <div style={s.seccion}>
                  <div style={s.seccionTit}>🔍 Diagnóstico</div>
                  <div style={s.seccionTxt}>{h.diagnostico}</div>
                </div>
              )}
              {h.tratamiento && (
                <div style={s.seccion}>
                  <div style={s.seccionTit}>💉 Tratamiento</div>
                  <div style={s.seccionTxt}>{h.tratamiento}</div>
                </div>
              )}
              {h.receta && (
                <div style={s.seccion}>
                  <div style={s.seccionTit}>💊 Receta médica</div>
                  <div style={s.recetaTxt}>{h.receta}</div>
                </div>
              )}
              {h.observaciones && (
                <div style={s.seccion}>
                  <div style={s.seccionTit}>📝 Observaciones</div>
                  <div style={s.seccionTxt}>{h.observaciones}</div>
                </div>
              )}
            </div>
          ))}
        </div>
        </>
      )}
    </Layout>
  );
}

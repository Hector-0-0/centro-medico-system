import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

import Portal       from './pages/Portal';
import LoginRol     from './pages/LoginRol';
import Login        from './pages/Login';
import Dashboard    from './pages/Dashboard';
import Pacientes    from './pages/Pacientes';
import Medicos      from './pages/Medicos';
import Citas        from './pages/Citas';
import Historial    from './pages/Historial';
import Medicamentos from './pages/Medicamentos';
import MisCitas     from './pages/MisCitas';
import MiHistorial  from './pages/MiHistorial';
import Perfil       from './pages/Perfil';
import Horarios      from './pages/Horarios';
import Disponibilidad from './pages/Disponibilidad';
import Recetas       from './pages/Recetas';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* ── Rutas públicas ─────────────────────────────────── */}
          <Route path="/"            element={<Portal />} />
          <Route path="/login"       element={<Login />} />
          <Route path="/login/:tipo" element={<LoginRol />} />

          {/* ── Panel principal (todos los roles) ──────────────── */}
          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/perfil"    element={<ProtectedRoute><Perfil /></ProtectedRoute>} />
          <Route path="/horarios"       element={<ProtectedRoute><Horarios /></ProtectedRoute>} />
          <Route path="/disponibilidad" element={<ProtectedRoute><Disponibilidad /></ProtectedRoute>} />
          <Route path="/recetas"        element={<ProtectedRoute><Recetas /></ProtectedRoute>} />

          {/* ── Rutas del PACIENTE ─────────────────────────────── */}
          <Route path="/mis-citas"    element={<ProtectedRoute><MisCitas /></ProtectedRoute>} />
          <Route path="/mi-historial" element={<ProtectedRoute><MiHistorial /></ProtectedRoute>} />

          {/* ── Rutas de ADMIN / MÉDICO / RECEPCIONISTA ────────── */}
          <Route path="/pacientes"    element={<ProtectedRoute><Pacientes /></ProtectedRoute>} />
          <Route path="/medicos"      element={<ProtectedRoute><Medicos /></ProtectedRoute>} />
          <Route path="/citas"        element={<ProtectedRoute><Citas /></ProtectedRoute>} />
          <Route path="/historial"    element={<ProtectedRoute><Historial /></ProtectedRoute>} />
          <Route path="/medicamentos" element={<ProtectedRoute><Medicamentos /></ProtectedRoute>} />

          {/* ── Ruta no encontrada ─────────────────────────────── */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
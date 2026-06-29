import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { DialogProvider } from './components/Dialog';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';

import Login         from './pages/Login';
import Pacientes     from './pages/Pacientes';
import Medicos       from './pages/Medicos';
import Citas         from './pages/Citas';
import CitasMedico   from './pages/CitasMedico';
import AtenderCita   from './pages/AtenderCita';
import Disponibilidad from './pages/Disponibilidad';
import VerStock      from './pages/VerStock';
import Perfil        from './pages/Perfil';
import Horarios      from './pages/Horarios';
import MisCitas      from './pages/MisCitas';
import Recetas       from './pages/Recetas';
import GestionStock  from './pages/GestionStock';

const TODOS = ['ESTUDIANTE', 'DOCTOR', 'ADMIN', 'FARMACIA'];

// Rutas privadas con los roles autorizados (espejo de components/menu.js).
const RUTAS = [
  { path: '/pacientes',                    element: <Pacientes />,      roles: ['ADMIN'] },
  { path: '/medicos',                      element: <Medicos />,        roles: ['ADMIN'] },
  { path: '/citas',                        element: <Citas />,          roles: ['ADMIN'] },
  { path: '/citas-medico',                 element: <CitasMedico />,    roles: ['DOCTOR'] },
  { path: '/citas-medico/:id/atender',     element: <AtenderCita />,    roles: ['DOCTOR'] },
  { path: '/disponibilidad',               element: <Disponibilidad />, roles: ['DOCTOR'] },
  { path: '/stock',                        element: <VerStock />,       roles: ['DOCTOR'] },
  { path: '/horarios',                     element: <Horarios />,       roles: ['ESTUDIANTE'] },
  { path: '/mis-citas',                    element: <MisCitas />,       roles: ['ESTUDIANTE'] },
  { path: '/recetas',                      element: <Recetas />,        roles: ['FARMACIA'] },
  { path: '/gestion-stock',                element: <GestionStock />,   roles: ['FARMACIA'] },
  { path: '/perfil',                       element: <Perfil />,         roles: TODOS },
];

export default function App() {
  return (
    <AuthProvider>
      <DialogProvider>
      <BrowserRouter>
        <Routes>
          {/* Login: único punto de entrada */}
          <Route path="/"      element={<Login />} />
          <Route path="/login" element={<Login />} />

          {/* Área autenticada con el marco (sidebar + topbar) */}
          <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            {RUTAS.map((r) => (
              <Route
                key={r.path}
                path={r.path}
                element={<ProtectedRoute roles={r.roles}>{r.element}</ProtectedRoute>}
              />
            ))}
          </Route>

          {/* Cualquier otra ruta vuelve al login */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
      </DialogProvider>
    </AuthProvider>
  );
}

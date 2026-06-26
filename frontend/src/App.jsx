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
            <Route path="/pacientes" element={<Pacientes />} />
            <Route path="/medicos"   element={<Medicos />} />
            <Route path="/citas"     element={<Citas />} />
            <Route path="/citas-medico" element={<CitasMedico />} />
            <Route path="/citas-medico/:id/atender" element={<AtenderCita />} />
            <Route path="/disponibilidad" element={<Disponibilidad />} />
            <Route path="/stock" element={<VerStock />} />
            <Route path="/perfil" element={<Perfil />} />
            <Route path="/horarios" element={<Horarios />} />
            <Route path="/mis-citas" element={<MisCitas />} />
            <Route path="/recetas" element={<Recetas />} />
            <Route path="/gestion-stock" element={<GestionStock />} />
          </Route>

          {/* Cualquier otra ruta vuelve al login */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
      </DialogProvider>
    </AuthProvider>
  );
}

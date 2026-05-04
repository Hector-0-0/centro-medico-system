import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/** Bloquea rutas privadas si no hay sesión activa. */
export default function ProtectedRoute({ children }) {
  const { loggedIn } = useAuth();
  return loggedIn ? children : <Navigate to="/login" replace />;
}

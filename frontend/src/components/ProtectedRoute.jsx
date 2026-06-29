import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { landingPath } from './menu';

/**
 * Protege rutas privadas:
 *  - si no hay sesión, redirige al login;
 *  - si se indican `roles` y el rol del usuario no está permitido, lo manda a
 *    la página de inicio de su propio rol (evita que, p. ej., un ESTUDIANTE
 *    abra /pacientes escribiendo la URL y vea un panel vacío por el 403).
 *
 * El backend sigue siendo la fuente de verdad de la autorización (@PreAuthorize);
 * esto solo mejora la experiencia en el cliente.
 */
export default function ProtectedRoute({ children, roles }) {
  const { loggedIn, rol } = useAuth();
  if (!loggedIn) return <Navigate to="/login" replace />;
  if (roles && !roles.includes(rol)) return <Navigate to={landingPath(rol)} replace />;
  return children;
}

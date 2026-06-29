import { createContext, useContext, useState } from 'react';
import { isLoggedIn, getRol, getUsername, logout, cargarPerfil } from '../services/authService';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [loggedIn,  setLoggedIn]  = useState(isLoggedIn);
  const [rol,       setRol]       = useState(getRol);
  const [username,  setUsername]  = useState(getUsername);

  const handleLogin = async (data) => {
    setLoggedIn(true);
    setRol(data.rol);
    setUsername(data.username);
    // Trae el nombre completo del usuario (GET /auth/me) para mostrarlo en el topbar.
    await cargarPerfil();
  };

  const handleLogout = () => {
    logout();
    setLoggedIn(false);
    setRol(null);
    setUsername(null);
  };

  return (
    <AuthContext.Provider value={{ loggedIn, rol, username, handleLogin, handleLogout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
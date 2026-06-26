/**
 * Menú lateral por rol — réplica del Sidebar del desktop.
 * Cada item define su etiqueta y su ruta. Los módulos aún no construidos
 * apuntan a una página placeholder hasta que se implementen.
 */
export const MENU = {
  ESTUDIANTE: [
    { label: 'Horarios',        path: '/horarios' },
    { label: 'Mis Citas',       path: '/mis-citas' },
    { label: 'Mi Perfil',       path: '/perfil' },
  ],
  DOCTOR: [
    { label: 'Mis Citas',       path: '/citas-medico' },
    { label: 'Disponibilidad',  path: '/disponibilidad' },
    { label: 'Ver Stock',       path: '/stock' },
    { label: 'Mi Perfil',       path: '/perfil' },
  ],
  ADMIN: [
    { label: 'Pacientes',       path: '/pacientes' },
    { label: 'Médicos',         path: '/medicos' },
    { label: 'Todas las Citas', path: '/citas' },
    { label: 'Mi Perfil',       path: '/perfil' },
  ],
  FARMACIA: [
    { label: 'Recetas',         path: '/recetas' },
    { label: 'Gestión Stock',   path: '/gestion-stock' },
    { label: 'Mi Perfil',       path: '/perfil' },
  ],
};

/** Ruta de inicio tras el login según el rol (primer item del menú). */
export const landingPath = (rol) => MENU[rol]?.[0]?.path || '/login';

/** Etiqueta del módulo correspondiente a una ruta (para el topbar). */
export const labelForPath = (path) => {
  for (const items of Object.values(MENU)) {
    const found = items.find((it) => it.path === path);
    if (found) return found.label;
  }
  return '';
};

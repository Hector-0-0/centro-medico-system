/** Edad cumplida a partir de una fecha de nacimiento (YYYY-MM-DD). */
export function edadDesde(fecha) {
  if (!fecha) return NaN;
  const hoy = new Date();
  const n = new Date(fecha + 'T00:00:00');
  let edad = hoy.getFullYear() - n.getFullYear();
  const m = hoy.getMonth() - n.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < n.getDate())) edad--;
  return edad;
}

/** Fecha de hoy en formato YYYY-MM-DD (para el `max` de inputs date). */
export const hoyISO = () => new Date().toISOString().slice(0, 10);

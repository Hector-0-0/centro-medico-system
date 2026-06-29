import { edadDesde } from './fechas';

describe('edadDesde', () => {
  test('devuelve NaN si no hay fecha', () => {
    expect(Number.isNaN(edadDesde(''))).toBe(true);
    expect(Number.isNaN(edadDesde(null))).toBe(true);
  });

  test('calcula la edad de alguien nacido hace 20 años exactos', () => {
    const hoy = new Date();
    const fecha = `${hoy.getFullYear() - 20}-${String(hoy.getMonth() + 1).padStart(2, '0')}-${String(hoy.getDate()).padStart(2, '0')}`;
    expect(edadDesde(fecha)).toBe(20);
  });

  test('aún no cumple años si el cumpleaños es mañana', () => {
    const manana = new Date();
    manana.setDate(manana.getDate() + 1);
    const fecha = `${manana.getFullYear() - 25}-${String(manana.getMonth() + 1).padStart(2, '0')}-${String(manana.getDate()).padStart(2, '0')}`;
    expect(edadDesde(fecha)).toBe(24);
  });
});

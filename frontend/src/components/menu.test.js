import { landingPath, labelForPath } from './menu';

describe('landingPath', () => {
  test('lleva a cada rol a su primer módulo', () => {
    expect(landingPath('ESTUDIANTE')).toBe('/horarios');
    expect(landingPath('DOCTOR')).toBe('/citas-medico');
    expect(landingPath('ADMIN')).toBe('/pacientes');
    expect(landingPath('FARMACIA')).toBe('/recetas');
  });

  test('rol desconocido cae al login', () => {
    expect(landingPath('OTRO')).toBe('/login');
    expect(landingPath(null)).toBe('/login');
  });
});

describe('labelForPath', () => {
  test('devuelve la etiqueta del módulo', () => {
    expect(labelForPath('/pacientes')).toBe('Pacientes');
    expect(labelForPath('/gestion-stock')).toBe('Gestión Stock');
  });

  test('ruta inexistente devuelve cadena vacía', () => {
    expect(labelForPath('/no-existe')).toBe('');
  });
});

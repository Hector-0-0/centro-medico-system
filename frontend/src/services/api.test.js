import { mensajeError } from './api';

describe('mensajeError', () => {
  test('prioriza el primer error de validación de campo', () => {
    const err = { response: { data: { campos: { email: 'Email inválido', id: 'Ya existe' } } } };
    expect(mensajeError(err)).toBe('Email inválido');
  });

  test('usa el mensaje general cuando no hay campos', () => {
    const err = { response: { data: { error: 'No se pudo guardar' } } };
    expect(mensajeError(err)).toBe('No se pudo guardar');
  });

  test('cae al texto por defecto cuando no hay datos', () => {
    expect(mensajeError({}, 'Falló')).toBe('Falló');
    expect(mensajeError(undefined, 'Falló')).toBe('Falló');
  });
});

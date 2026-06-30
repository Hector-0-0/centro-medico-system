/**
 * Catálogos compartidos (carreras y especialidades). Fuente única para los
 * combos del frontend; el backend valida contra las mismas listas (ver
 * `model/Catalogos.java`). Mantener ambas en sintonía.
 */

// Carreras de la UNI.
export const CARRERAS = [
  'Ingenieria de Sistemas', 'Ingenieria Industrial', 'Ingenieria Civil',
  'Ingenieria Electronica', 'Ingenieria Mecanica', 'Ingenieria Quimica',
  'Ingenieria Economica', 'Arquitectura', 'Ciencias',
];

// Especialidades médicas (incluye las sembradas en init_db.sql).
export const ESPECIALIDADES = [
  'Medicina General', 'Cardiologia', 'Dermatologia', 'Endocrinologia',
  'Ginecologia', 'Neurologia', 'Odontologia', 'Oftalmologia', 'Pediatria',
  'Psicologia', 'Radiologia', 'Traumatologia',
];

// Grupos sanguíneos (debe coincidir con Catalogos.TIPOS_SANGRE en el backend).
export const TIPOS_SANGRE = ['O+', 'O-', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-'];

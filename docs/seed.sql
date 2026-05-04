-- ================================================================
-- DATOS INICIALES — Centro Médico UNI
-- Ejecutar DESPUÉS de que Spring Boot cree las tablas automáticamente
-- ================================================================

-- Contraseñas hasheadas con BCrypt:
-- admin123  → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtXtWNRiFhe
-- medico123 → $2a$10$D9cOVhDyJP0B0FzF3GVHxevNSwjN5pQUoiyAp3zDMHk9qU7hPiKsG
-- recep123  → $2a$10$8y5LV1e5M2JK0Z8VuZ7V8OVm1G8f5E1v8L3a2sK9p6n5Rd4n5Sq3m

-- ─── USUARIOS ───────────────────────────────────────────────────
INSERT INTO usuarios (username, password, email, rol, activo) VALUES
  ('admin',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtXtWNRiFhe', 'admin@uni.edu.pe',   'ADMIN',          true),
  ('medico1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtXtWNRiFhe', 'garcia@uni.edu.pe',  'MEDICO',         true),
  ('medico2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtXtWNRiFhe', 'torres@uni.edu.pe',  'MEDICO',         true),
  ('recep1',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LtXtWNRiFhe', 'recep@uni.edu.pe',   'RECEPCIONISTA',  true)
ON CONFLICT (username) DO NOTHING;

-- Contraseña para todos: admin123

-- ─── ESPECIALIDADES ─────────────────────────────────────────────
INSERT INTO especialidades (nombre, descripcion) VALUES
  ('Medicina General',   'Atención primaria de salud y consulta general'),
  ('Odontología',        'Salud bucal y dental'),
  ('Psicología',         'Salud mental y bienestar emocional'),
  ('Nutrición',          'Orientación nutricional y dietética'),
  ('Oftalmología',       'Salud visual y ocular'),
  ('Traumatología',      'Lesiones musculoesqueléticas y fracturas')
ON CONFLICT (nombre) DO NOTHING;

-- ─── MÉDICOS ────────────────────────────────────────────────────
INSERT INTO medicos (cmp, nombre, apellido, telefono, especialidad_id, usuario_id) VALUES
  ('CMP-12345', 'Carlos',   'García',  '987654321', 1, (SELECT id FROM usuarios WHERE username = 'medico1')),
  ('CMP-67890', 'Patricia', 'Torres',  '976543210', 2, (SELECT id FROM usuarios WHERE username = 'medico2'))
ON CONFLICT (cmp) DO NOTHING;

-- ─── PACIENTES ──────────────────────────────────────────────────
INSERT INTO pacientes (dni, nombre, apellido, fecha_nacimiento, telefono, grupo_sanguineo, alergias) VALUES
  ('12345678', 'Juan',    'Pérez',   '2000-05-15', '991234567', 'O+',  'Penicilina'),
  ('87654321', 'María',   'López',   '1998-11-20', '992345678', 'A+',  null),
  ('11223344', 'Carlos',  'Quispe',  '2001-03-08', '993456789', 'B+',  null),
  ('44332211', 'Lucía',   'Mamani',  '1999-07-25', '994567890', 'AB-', 'Ibuprofeno'),
  ('55667788', 'Diego',   'Vargas',  '2002-01-12', '995678901', 'O-',  null)
ON CONFLICT (dni) DO NOTHING;

-- ─── MEDICAMENTOS ───────────────────────────────────────────────
INSERT INTO medicamentos (nombre, principio_activo, presentacion, stock_actual, stock_minimo, unidad, activo) VALUES
  ('Paracetamol 500mg',  'Paracetamol',    'Tableta',   150, 20,  'unidades', true),
  ('Ibuprofeno 400mg',   'Ibuprofeno',     'Cápsula',    80, 15,  'unidades', true),
  ('Amoxicilina 500mg',  'Amoxicilina',    'Cápsula',    60, 10,  'unidades', true),
  ('Omeprazol 20mg',     'Omeprazol',      'Tableta',    90, 10,  'unidades', true),
  ('Loratadina 10mg',    'Loratadina',     'Tableta',     8,  10, 'unidades', true),
  ('Metformina 850mg',   'Metformina',     'Tableta',    45,  10, 'unidades', true),
  ('Suero Fisiológico',  'Cloruro sódico', 'Frasco 1L',   5,  10, 'frascos',  true),
  ('Alcohol 70°',        'Etanol',         'Frasco 250ml', 20, 5,  'frascos',  true)
ON CONFLICT DO NOTHING;

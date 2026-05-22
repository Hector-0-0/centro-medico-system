CREATE DATABASE IF NOT EXISTS centro_medico
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE centro_medico;

-- ─────────────────────────────────────────────
-- TABLAS
-- ─────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS usuarios (
    id         VARCHAR(10) PRIMARY KEY,
    password   VARCHAR(100) NOT NULL,
    rol        ENUM('ESTUDIANTE','DOCTOR','ADMIN','FARMACIA') NOT NULL,
    eliminado  TINYINT(1) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS estudiantes (
    id_usuario VARCHAR(10) PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    edad       INT,
    carrera    VARCHAR(100),
    email      VARCHAR(100) UNIQUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS doctores (
    id_usuario   VARCHAR(10) PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    consultorio  VARCHAR(50),
    activo       TINYINT(1) DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS administradores (
    id_usuario VARCHAR(10) PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS farmacia_usuarios (
    id_usuario VARCHAR(10) PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS horarios_doctor (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor    VARCHAR(10) NOT NULL,
    dia_semana   VARCHAR(20) NOT NULL,
    hora_inicio  VARCHAR(10) NOT NULL,
    hora_fin     VARCHAR(10) NOT NULL,
    disponible   TINYINT(1) DEFAULT 1,
    FOREIGN KEY (id_doctor) REFERENCES doctores(id_usuario),
    UNIQUE KEY uq_horario (id_doctor, dia_semana, hora_inicio)
);

CREATE TABLE IF NOT EXISTS medicamentos (
    id      VARCHAR(10) PRIMARY KEY,
    nombre  VARCHAR(150) NOT NULL,
    stock   INT DEFAULT 0,
    tipo    VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS citas (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante  VARCHAR(10) NOT NULL,
    id_doctor      VARCHAR(10) NOT NULL,
    id_horario     INT NOT NULL,
    motivo         VARCHAR(255),
    estado         ENUM('PENDIENTE','ATENDIDA','CANCELADA') DEFAULT 'PENDIENTE',

    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_usuario),
    FOREIGN KEY (id_doctor) REFERENCES doctores(id_usuario),
    FOREIGN KEY (id_horario) REFERENCES horarios_doctor(id)
);

CREATE TABLE IF NOT EXISTS atencion_cita (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_cita          INT NOT NULL UNIQUE,
    diagnostico      TEXT,
    comentarios      TEXT,
    fecha_atencion   DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_cita) REFERENCES citas(id)
);

CREATE TABLE IF NOT EXISTS recetas (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_atencion   INT NOT NULL UNIQUE,
    estado        ENUM('PENDIENTE','ENTREGADA') DEFAULT 'PENDIENTE',

    FOREIGN KEY (id_atencion) REFERENCES atencion_cita(id)
);

CREATE TABLE IF NOT EXISTS receta_detalle (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_receta        INT NOT NULL,
    id_medicamento   VARCHAR(10) NOT NULL,
    dosis            VARCHAR(100),
    duracion         VARCHAR(50),

    FOREIGN KEY (id_receta) REFERENCES recetas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);

-- ─────────────────────────────────────────────
-- DATOS DE PRUEBA
-- ─────────────────────────────────────────────

INSERT IGNORE INTO usuarios (id, password, rol, eliminado) VALUES
('U001', '1234',      'ESTUDIANTE', 0),
('U002', 'abcd',      'ESTUDIANTE', 0),
('U003', 'pass2024',  'ESTUDIANTE', 0),
('U004', 'qwerty',    'ESTUDIANTE', 0),
('U005', 'admin123',  'ESTUDIANTE', 0),

('D001', 'pass123',   'DOCTOR', 0),
('D002', 'laura2024', 'DOCTOR', 0),
('D003', 'ruizpass',  'DOCTOR', 0),
('D004', 'sofiaRX',   'DOCTOR', 0),
('D005', 'cardio99',  'DOCTOR', 0),

('ADM001', 'adm123',  'ADMIN', 0),

('FAR001', 'far123',  'FARMACIA', 0);

INSERT IGNORE INTO estudiantes VALUES
('U001', 'Juan Perez',     21, 'Ingenieria de Sistemas', 'hola@uni.pe'),
('U002', 'Maria Lopez',    22, 'Ingenieria Industrial',  'correo@uni.pe'),
('U003', 'Carlos Ramirez', 20, 'Ingenieria Civil',       'prueba@uni.pe'),
('U004', 'Ana Torres',     23, 'Ingenieria Electronica', 'yafueya@uni.pe'),
('U005', 'Luis Gomez',     24, 'Ingenieria Mecanica',    'arianasapa@uni.pe');

INSERT IGNORE INTO doctores VALUES
('D001', 'Dr. Carlos Medina', 'Endocrinologia', 'Consultorio 101', 1),
('D002', 'Dra. Laura Pena',   'Endocrinologia', 'Consultorio 102', 0),
('D003', 'Dr. Javier Ruiz',   'Odontologia',    'Consultorio 201', 1),
('D004', 'Dra. Sofia Torres', 'Radiologia',     'Sala RX-01',      1),
('D005', 'Dr. Luis Ramos',    'Cardiologia',    'Consultorio 305', 1);

INSERT IGNORE INTO administradores VALUES
('ADM001', 'Administrador General Sotelo');

INSERT IGNORE INTO farmacia_usuarios VALUES
('FAR001', 'Juan Farmaceutico');

INSERT IGNORE INTO horarios_doctor
(id_doctor, dia_semana, hora_inicio, hora_fin, disponible)
VALUES
('D001', 'Lunes',     '08:00', '09:00', 1),
('D001', 'Lunes',     '10:00', '11:00', 1),
('D002', 'Martes',    '09:00', '10:00', 1),
('D003', 'Miercoles', '11:00', '12:00', 1),
('D004', 'Jueves',    '14:00', '15:00', 1),
('D005', 'Viernes',   '15:00', '16:00', 1);

INSERT IGNORE INTO medicamentos VALUES
('MED-001', 'Amoxicilina 500mg', 150, 'capsulas'),
('MED-002', 'Ibuprofeno 400mg',   80, 'comprimidos'),
('MED-003', 'Loratadina 10mg',     0, 'tabletas'),
('MED-004', 'Paracetamol 500mg', 220, 'comprimidos'),
('MED-005', 'Omeprazol 20mg',     12, 'capsulas'),
('MED-006', 'Metformina 850mg',   95, 'comprimidos');

-- Citas de prueba

INSERT IGNORE INTO citas
(id, id_estudiante, id_doctor, id_horario, motivo, estado)
VALUES
(1, 'U001', 'D001', 1, 'Control de glucosa', 'ATENDIDA'),

(2, 'U002', 'D001', 2, 'Consulta general', 'PENDIENTE');

-- Marcar horario 1 como ocupado

UPDATE horarios_doctor
SET disponible = 0
WHERE id = 1;

-- Atención de la cita 1

INSERT IGNORE INTO atencion_cita
(id, id_cita, diagnostico, comentarios)
VALUES
(
    1,
    1,
    'Diabetes tipo 2 leve',
    'Paciente debe controlar dieta y hacer ejercicio'
);

-- Receta asociada

INSERT IGNORE INTO recetas
(id, id_atencion, estado)
VALUES
(1, 1, 'ENTREGADA');

-- Medicamentos de la receta

INSERT IGNORE INTO receta_detalle
(id_receta, id_medicamento, dosis, duracion)
VALUES
(1, 'MED-001', '1 capsula cada 8h', '7 dias'),

(1, 'MED-006', '1 comp con el desayuno', '30 dias');
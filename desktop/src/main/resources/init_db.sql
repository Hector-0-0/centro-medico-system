CREATE DATABASE IF NOT EXISTS centro_medico
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE centro_medico;

-- ─────────────────────────────────────────────
-- TABLAS
-- ─────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS usuarios (
    id        VARCHAR(10)  PRIMARY KEY,
    password  VARCHAR(100) NOT NULL,
    rol       ENUM('ESTUDIANTE','DOCTOR','ADMIN','FARMACIA') NOT NULL,
    eliminado TINYINT(1)   DEFAULT 0
);

CREATE TABLE IF NOT EXISTS estudiantes (
    id_usuario VARCHAR(10)  PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    edad       INT,
    carrera    VARCHAR(100),
    email      VARCHAR(100) UNIQUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS doctores (
    id_usuario   VARCHAR(10)  PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    consultorio  VARCHAR(50),
    activo       TINYINT(1) DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS administradores (
    id_usuario VARCHAR(10)  PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS farmacia_usuarios (
    id_usuario VARCHAR(10)  PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Rango de disponibilidad que define el doctor
-- Ej: "Lunes 10:00-12:00" o "Lunes 14:00-16:00"
-- Varios rangos por día están permitidos
CREATE TABLE IF NOT EXISTS disponibilidad_doctor (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_doctor   VARCHAR(10)  NOT NULL,
    dia_semana  VARCHAR(20)  NOT NULL,
    hora_inicio VARCHAR(10)  NOT NULL,
    hora_fin    VARCHAR(10)  NOT NULL,
    eliminado   TINYINT(1)   DEFAULT 0,
    -- evita duplicar el mismo rango activo para el mismo doctor y día
    UNIQUE KEY uq_disponibilidad (id_doctor, dia_semana, hora_inicio, hora_fin),
    FOREIGN KEY (id_doctor) REFERENCES doctores(id_usuario) ON DELETE CASCADE
);

-- Slots de 30 minutos generados automáticamente desde disponibilidad_doctor
-- Estos son los que el estudiante elige al agendar
-- Guardan snapshot del día y hora para que el historial
-- no se rompa si el rango cambia en el futuro
CREATE TABLE IF NOT EXISTS slots_disponibilidad (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    id_disponibilidad  INT         NOT NULL,
    id_doctor          VARCHAR(10) NOT NULL,
    dia_semana         VARCHAR(20) NOT NULL,  -- snapshot
    hora_inicio        VARCHAR(10) NOT NULL,  -- snapshot
    hora_fin           VARCHAR(10) NOT NULL,  -- snapshot
    disponible         TINYINT(1)  DEFAULT 1,
    eliminado          TINYINT(1)  DEFAULT 0,
    FOREIGN KEY (id_disponibilidad) REFERENCES disponibilidad_doctor(id),
    FOREIGN KEY (id_doctor)         REFERENCES doctores(id_usuario)
);

CREATE TABLE IF NOT EXISTS medicamentos (
    id     VARCHAR(10)  PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    stock  INT          DEFAULT 0,
    tipo   VARCHAR(50)
);

-- Citas referencian un slot específico
-- El slot guarda snapshot de día y hora,
-- así el historial es inmune a cambios futuros del doctor
CREATE TABLE IF NOT EXISTS citas (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante VARCHAR(10) NOT NULL,
    id_doctor     VARCHAR(10) NOT NULL,
    id_slot       INT         NOT NULL,
    motivo        VARCHAR(255),
    estado        ENUM('PENDIENTE','ATENDIDA','CANCELADA') DEFAULT 'PENDIENTE',
    eliminado     TINYINT(1)  DEFAULT 0,
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_usuario),
    FOREIGN KEY (id_doctor)     REFERENCES doctores(id_usuario),
    FOREIGN KEY (id_slot)       REFERENCES slots_disponibilidad(id)
);

CREATE TABLE IF NOT EXISTS atencion_cita (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_cita        INT  NOT NULL UNIQUE,
    diagnostico    TEXT,
    comentarios    TEXT,
    fecha_atencion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cita) REFERENCES citas(id)
);

CREATE TABLE IF NOT EXISTS recetas (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_atencion INT NOT NULL UNIQUE,
    estado      ENUM('PENDIENTE','ENTREGADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_atencion) REFERENCES atencion_cita(id)
);

CREATE TABLE IF NOT EXISTS receta_detalle (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_receta      INT         NOT NULL,
    id_medicamento VARCHAR(10) NOT NULL,
    dosis          VARCHAR(100),
    duracion       VARCHAR(50),
    FOREIGN KEY (id_receta)      REFERENCES recetas(id) ON DELETE CASCADE,
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
('D001', 'pass123',   'DOCTOR',     0),
('D002', 'laura2024', 'DOCTOR',     0),
('D003', 'ruizpass',  'DOCTOR',     0),
('D004', 'sofiaRX',   'DOCTOR',     0),
('D005', 'cardio99',  'DOCTOR',     0),
('ADM001', 'adm123',  'ADMIN',      0),
('FAR001', 'far123',  'FARMACIA',   0);

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

-- Rangos de disponibilidad de prueba
-- D001 tiene mañana y tarde el lunes (dos rangos en el mismo día)
INSERT IGNORE INTO disponibilidad_doctor
(id, id_doctor, dia_semana, hora_inicio, hora_fin, eliminado) VALUES
(1, 'D001', 'Lunes',     '08:00', '10:00', 0),  -- D001 mañana
(2, 'D001', 'Lunes',     '14:00', '16:00', 0),  -- D001 tarde
(3, 'D002', 'Martes',    '09:00', '11:00', 0),
(4, 'D003', 'Miercoles', '11:00', '13:00', 0),
(5, 'D004', 'Jueves',    '14:00', '16:00', 0),
(6, 'D005', 'Viernes',   '15:00', '17:00', 0);

-- Slots generados de 30min para cada disponibilidad
-- disponibilidad 1: D001 Lunes 08:00-10:00 → 4 slots
INSERT IGNORE INTO slots_disponibilidad
(id, id_disponibilidad, id_doctor, dia_semana, hora_inicio, hora_fin, disponible, eliminado) VALUES
(1,  1, 'D001', 'Lunes',     '08:00', '08:30', 0, 0),  -- ocupado (tiene cita)
(2,  1, 'D001', 'Lunes',     '08:30', '09:00', 1, 0),
(3,  1, 'D001', 'Lunes',     '09:00', '09:30', 1, 0),
(4,  1, 'D001', 'Lunes',     '09:30', '10:00', 1, 0),
-- disponibilidad 2: D001 Lunes 14:00-16:00 → 4 slots
(5,  2, 'D001', 'Lunes',     '14:00', '14:30', 1, 0),
(6,  2, 'D001', 'Lunes',     '14:30', '15:00', 1, 0),
(7,  2, 'D001', 'Lunes',     '15:00', '15:30', 1, 0),
(8,  2, 'D001', 'Lunes',     '15:30', '16:00', 1, 0),
-- disponibilidad 3: D002 Martes 09:00-11:00 → 4 slots
(9,  3, 'D002', 'Martes',    '09:00', '09:30', 1, 0),
(10, 3, 'D002', 'Martes',    '09:30', '10:00', 1, 0),
(11, 3, 'D002', 'Martes',    '10:00', '10:30', 1, 0),
(12, 3, 'D002', 'Martes',    '10:30', '11:00', 1, 0),
-- disponibilidad 4: D003 Miercoles 11:00-13:00 → 4 slots
(13, 4, 'D003', 'Miercoles', '11:00', '11:30', 1, 0),
(14, 4, 'D003', 'Miercoles', '11:30', '12:00', 1, 0),
(15, 4, 'D003', 'Miercoles', '12:00', '12:30', 1, 0),
(16, 4, 'D003', 'Miercoles', '12:30', '13:00', 1, 0),
-- disponibilidad 5: D004 Jueves 14:00-16:00 → 4 slots
(17, 5, 'D004', 'Jueves',    '14:00', '14:30', 1, 0),
(18, 5, 'D004', 'Jueves',    '14:30', '15:00', 1, 0),
(19, 5, 'D004', 'Jueves',    '15:00', '15:30', 1, 0),
(20, 5, 'D004', 'Jueves',    '15:30', '16:00', 1, 0),
-- disponibilidad 6: D005 Viernes 15:00-17:00 → 4 slots
(21, 6, 'D005', 'Viernes',   '15:00', '15:30', 1, 0),
(22, 6, 'D005', 'Viernes',   '15:30', '16:00', 1, 0),
(23, 6, 'D005', 'Viernes',   '16:00', '16:30', 1, 0),
(24, 6, 'D005', 'Viernes',   '16:30', '17:00', 1, 0);

INSERT IGNORE INTO medicamentos VALUES
('MED-001', 'Amoxicilina 500mg', 150, 'capsulas'),
('MED-002', 'Ibuprofeno 400mg',   80, 'comprimidos'),
('MED-003', 'Loratadina 10mg',     0, 'tabletas'),
('MED-004', 'Paracetamol 500mg', 220, 'comprimidos'),
('MED-005', 'Omeprazol 20mg',     12, 'capsulas'),
('MED-006', 'Metformina 850mg',   95, 'comprimidos');

-- Cita de prueba usando slot 1 (D001, Lunes 08:00-08:30)
INSERT IGNORE INTO citas
(id, id_estudiante, id_doctor, id_slot, motivo, estado, eliminado) VALUES
(1, 'U001', 'D001', 1, 'Control de glucosa', 'ATENDIDA',  0),
(2, 'U002', 'D001', 2, 'Consulta general',   'PENDIENTE', 0);

-- Atención de la cita 1
INSERT IGNORE INTO atencion_cita (id, id_cita, diagnostico, comentarios) VALUES
(1, 1, 'Diabetes tipo 2 leve', 'Paciente debe controlar dieta y hacer ejercicio');

-- Receta asociada
INSERT IGNORE INTO recetas (id, id_atencion, estado) VALUES
(1, 1, 'ENTREGADA');

-- Medicamentos de la receta
INSERT IGNORE INTO receta_detalle (id_receta, id_medicamento, dosis, duracion) VALUES
(1, 'MED-001', '1 capsula cada 8h',      '7 dias'),
(1, 'MED-006', '1 comp con el desayuno', '30 dias');
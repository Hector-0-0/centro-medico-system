-- ═════════════════════════════════════════════════════════════════════════
-- CENTRO MÉDICO UNI — Esquema completo para SQL Server (T-SQL)
-- Incluye todos los cambios acordados: especialidades, fecha_nacimiento
-- unificada, estado NO_ASISTIO, fecha_creacion en citas, eliminado propio
-- por tabla de rol, vistas de activos, sin ON DELETE CASCADE, índice único
-- de slot activo.
-- Idempotente: se puede ejecutar varias veces sin error
-- ═════════════════════════════════════════════════════════════════════════

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'centro_medico')
BEGIN
    CREATE DATABASE centro_medico;
END;
GO

USE centro_medico;
GO

-- ─────────────────────────────────────────────────────────────────────────
-- TABLAS
-- ─────────────────────────────────────────────────────────────────────────

IF OBJECT_ID('usuarios', 'U') IS NULL
CREATE TABLE usuarios (
    id               VARCHAR(10)  NOT NULL PRIMARY KEY,
    password         VARCHAR(100) NOT NULL,
    rol              VARCHAR(20)  NOT NULL
                     CONSTRAINT chk_usuarios_rol
                     CHECK (rol IN ('ESTUDIANTE','DOCTOR','ADMIN','FARMACIA')),
    eliminado        BIT          NOT NULL DEFAULT 0,
    fecha_nacimiento DATE         NULL
);
GO

IF OBJECT_ID('especialidades', 'U') IS NULL
CREATE TABLE especialidades (
    id     INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);
GO

IF OBJECT_ID('estudiantes', 'U') IS NULL
CREATE TABLE estudiantes (
    id_usuario VARCHAR(10)  NOT NULL PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    carrera    VARCHAR(100) NULL,
    email      VARCHAR(100) NULL UNIQUE,
    eliminado  BIT          NOT NULL DEFAULT 0,
    CONSTRAINT fk_estudiantes_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
GO

IF OBJECT_ID('doctores', 'U') IS NULL
CREATE TABLE doctores (
    id_usuario      VARCHAR(10)  NOT NULL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    especialidad_id INT          NULL,
    consultorio     VARCHAR(50)  NULL,
    activo          BIT          NOT NULL DEFAULT 1,
    eliminado       BIT          NOT NULL DEFAULT 0,
    CONSTRAINT fk_doctores_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    CONSTRAINT fk_doctores_especialidad
        FOREIGN KEY (especialidad_id) REFERENCES especialidades(id)
);
GO

IF OBJECT_ID('administradores', 'U') IS NULL
CREATE TABLE administradores (
    id_usuario VARCHAR(10)  NOT NULL PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    eliminado  BIT          NOT NULL DEFAULT 0,
    CONSTRAINT fk_admin_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
GO

IF OBJECT_ID('farmacia_usuarios', 'U') IS NULL
CREATE TABLE farmacia_usuarios (
    id_usuario VARCHAR(10)  NOT NULL PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    eliminado  BIT          NOT NULL DEFAULT 0,
    CONSTRAINT fk_farmacia_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
GO

IF OBJECT_ID('disponibilidad_doctor', 'U') IS NULL
CREATE TABLE disponibilidad_doctor (
    id          INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_doctor   VARCHAR(10)  NOT NULL,
    dia_semana  VARCHAR(20)  NOT NULL,
    hora_inicio VARCHAR(10)  NOT NULL,
    hora_fin    VARCHAR(10)  NOT NULL,
    eliminado   BIT          NOT NULL DEFAULT 0,
    CONSTRAINT uq_disponibilidad UNIQUE (id_doctor, dia_semana, hora_inicio, hora_fin),
    CONSTRAINT fk_disp_doctor
        FOREIGN KEY (id_doctor) REFERENCES doctores(id_usuario)
);
GO

IF OBJECT_ID('slots_disponibilidad', 'U') IS NULL
CREATE TABLE slots_disponibilidad (
    id                 INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_disponibilidad  INT          NOT NULL,
    id_doctor          VARCHAR(10)  NOT NULL,
    dia_semana         VARCHAR(20)  NOT NULL,
    hora_inicio        VARCHAR(10)  NOT NULL,
    hora_fin           VARCHAR(10)  NOT NULL,
    disponible         BIT          NOT NULL DEFAULT 1,
    eliminado          BIT          NOT NULL DEFAULT 0,
    CONSTRAINT fk_slot_disp
        FOREIGN KEY (id_disponibilidad) REFERENCES disponibilidad_doctor(id),
    CONSTRAINT fk_slot_doctor
        FOREIGN KEY (id_doctor)         REFERENCES doctores(id_usuario)
);
GO

IF OBJECT_ID('medicamentos', 'U') IS NULL
CREATE TABLE medicamentos (
    id        VARCHAR(10)  NOT NULL PRIMARY KEY,
    nombre    VARCHAR(150) NOT NULL,
    stock     INT          NOT NULL DEFAULT 0,
    tipo      VARCHAR(50)  NULL,
    dosis     VARCHAR(100) NULL,
    eliminado BIT          NOT NULL DEFAULT 0
);
GO

IF OBJECT_ID('stock_auditoria', 'U') IS NULL
CREATE TABLE stock_auditoria (
    id               INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_medicamento   VARCHAR(10)  NOT NULL,
    stock_anterior   INT          NOT NULL,
    stock_nuevo      INT          NOT NULL,
    cambio           INT          NOT NULL,
    tipo_movimiento  VARCHAR(20)  NOT NULL
                     CONSTRAINT chk_stock_tipo
                     CHECK (tipo_movimiento IN ('VENTA','REABASTECIMIENTO','AJUSTE')),
    id_usuario       VARCHAR(10)  NOT NULL,
    fecha_movimiento DATETIME     NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_auditoria_medicamento
        FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);
GO

IF OBJECT_ID('citas', 'U') IS NULL
CREATE TABLE citas (
    id             INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_estudiante  VARCHAR(10)  NOT NULL,
    id_doctor      VARCHAR(10)  NOT NULL,
    id_slot        INT          NOT NULL,
    motivo         VARCHAR(255) NULL,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE'
                   CONSTRAINT chk_citas_estado
                   CHECK (estado IN ('PENDIENTE','ATENDIDA','CANCELADA','NO_ASISTIO')),
    eliminado      BIT          NOT NULL DEFAULT 0,
    fecha_creacion DATETIME     NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_citas_estudiante
        FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_usuario),
    CONSTRAINT fk_citas_doctor
        FOREIGN KEY (id_doctor)     REFERENCES doctores(id_usuario),
    CONSTRAINT fk_citas_slot
        FOREIGN KEY (id_slot)       REFERENCES slots_disponibilidad(id)
);
GO

-- Evita que dos citas PENDIENTE usen el mismo slot al mismo tiempo
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'uq_slot_activo')
BEGIN
    SET QUOTED_IDENTIFIER ON;
    CREATE UNIQUE INDEX uq_slot_activo
    ON citas (id_slot)
    WHERE estado = 'PENDIENTE';
END;
GO

IF OBJECT_ID('atencion_cita', 'U') IS NULL
CREATE TABLE atencion_cita (
    id             INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_cita        INT          NOT NULL UNIQUE,
    diagnostico    VARCHAR(MAX) NULL,
    comentarios    VARCHAR(MAX) NULL,
    fecha_atencion DATETIME     NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_atencion_cita
        FOREIGN KEY (id_cita) REFERENCES citas(id)
);
GO

IF OBJECT_ID('recetas', 'U') IS NULL
CREATE TABLE recetas (
    id          INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_atencion INT NOT NULL UNIQUE,
    estado      VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                CONSTRAINT chk_recetas_estado
                CHECK (estado IN ('PENDIENTE','ENTREGADA')),
    CONSTRAINT fk_receta_atencion
        FOREIGN KEY (id_atencion) REFERENCES atencion_cita(id)
);
GO

IF OBJECT_ID('receta_detalle', 'U') IS NULL
CREATE TABLE receta_detalle (
    id             INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_receta      INT          NOT NULL,
    id_medicamento VARCHAR(10)  NOT NULL,
    dosis          VARCHAR(100) NULL,
    duracion       VARCHAR(50)  NULL,
    CONSTRAINT fk_detalle_receta
        FOREIGN KEY (id_receta)      REFERENCES recetas(id),
    CONSTRAINT fk_detalle_medicamento
        FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id)
);
GO

-- ─────────────────────────────────────────────────────────────────────────
-- CATÁLOGO CIE-10
-- ─────────────────────────────────────────────────────────────────────────

IF OBJECT_ID('codigos_cie', 'U') IS NULL
CREATE TABLE codigos_cie (
    id          INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    codigo      VARCHAR(10)  NOT NULL UNIQUE,
    descripcion VARCHAR(300) NOT NULL
);
GO

IF OBJECT_ID('atencion_diagnostico', 'U') IS NULL
CREATE TABLE atencion_diagnostico (
    id          INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_atencion INT NOT NULL,
    id_cie      INT NOT NULL,
    observacion VARCHAR(255) NULL,
    CONSTRAINT uq_atencion_cie UNIQUE (id_atencion, id_cie),
    CONSTRAINT fk_diag_atencion
        FOREIGN KEY (id_atencion) REFERENCES atencion_cita(id),
    CONSTRAINT fk_diag_cie
        FOREIGN KEY (id_cie)      REFERENCES codigos_cie(id)
);
GO

-- ─────────────────────────────────────────────────────────────────────────
-- VISTAS DE "ACTIVOS" — centralizan el filtro de eliminado por rol
-- Ambas apps (JDBC y Spring Boot) deberían consultar estas vistas
-- en lugar de hacer el JOIN manual con usuarios cada vez.
-- ─────────────────────────────────────────────────────────────────────────

CREATE OR ALTER VIEW doctores_activos AS
SELECT d.*
FROM doctores d
JOIN usuarios u ON u.id = d.id_usuario
WHERE u.eliminado = 0 AND d.eliminado = 0;
GO

CREATE OR ALTER VIEW estudiantes_activos AS
SELECT e.*
FROM estudiantes e
JOIN usuarios u ON u.id = e.id_usuario
WHERE u.eliminado = 0 AND e.eliminado = 0;
GO

CREATE OR ALTER VIEW administradores_activos AS
SELECT a.*
FROM administradores a
JOIN usuarios u ON u.id = a.id_usuario
WHERE u.eliminado = 0 AND a.eliminado = 0;
GO

CREATE OR ALTER VIEW farmacia_usuarios_activos AS
SELECT f.*
FROM farmacia_usuarios f
JOIN usuarios u ON u.id = f.id_usuario
WHERE u.eliminado = 0 AND f.eliminado = 0;
GO

CREATE OR ALTER VIEW medicamentos_activos AS
SELECT *
FROM medicamentos
WHERE eliminado = 0;
GO

-- ─────────────────────────────────────────────────────────────────────────
-- SEED: Especialidades
-- ─────────────────────────────────────────────────────────────────────────

IF NOT EXISTS (SELECT 1 FROM especialidades WHERE nombre = 'Endocrinologia')
BEGIN
    INSERT INTO especialidades (nombre) VALUES
    ('Endocrinologia'),
    ('Odontologia'),
    ('Cardiologia'),
    ('Radiologia'),
    ('Medicina General'),
    ('Pediatria'),
    ('Ginecologia'),
    ('Neurologia'),
    ('Dermatologia'),
    ('Oftalmologia');
END;
GO

-- ─────────────────────────────────────────────────────────────────────────
-- SEED: Códigos CIE-10
-- ─────────────────────────────────────────────────────────────────────────

IF NOT EXISTS (SELECT 1 FROM codigos_cie WHERE codigo = 'E10')
BEGIN
    INSERT INTO codigos_cie (codigo, descripcion) VALUES
    ('E10', 'Diabetes mellitus tipo 1'),
    ('E11', 'Diabetes mellitus tipo 2'),
    ('E78', 'Hiperlipidemia mixta'),
    ('E03', 'Hipotiroidismo'),
    ('E66', 'Obesidad'),
    ('E04', 'Bocio nodular no tóxico'),
    ('E07', 'Trastornos de la glándula tiroides'),
    ('K02', 'Caries dental'),
    ('K04', 'Enfermedades de la pulpa y periapicales'),
    ('K05', 'Gingivitis y enfermedades periodontales'),
    ('K08', 'Trastornos de los dientes y estructuras de apoyo'),
    ('K01', 'Dientes incluidos e impactados'),
    ('K12', 'Estomatitis y lesiones relacionadas'),
    ('I10', 'Hipertensión esencial (primaria)'),
    ('I11', 'Cardiopatía hipertensiva'),
    ('I20', 'Angina de pecho'),
    ('I25', 'Cardiopatía isquémica crónica'),
    ('I48', 'Fibrilación y aleteo auricular'),
    ('I50', 'Insuficiencia cardíaca'),
    ('I70', 'Aterosclerosis'),
    ('M81', 'Osteoporosis sin fractura patológica'),
    ('J90', 'Derrame pleural'),
    ('J91', 'Derrame pleural en afecciones clasificadas'),
    ('S22', 'Fractura de costilla(s) o esternón'),
    ('S42', 'Fractura del húmero'),
    ('J45', 'Asma'),
    ('J15', 'Neumonía bacteriana'),
    ('N39', 'Infección del tracto urinario'),
    ('M54', 'Dolor de espalda'),
    ('R51', 'Cefalea'),
    ('A09', 'Gastroenteritis de presunto origen infeccioso');
END;
GO

-- ─────────────────────────────────────────────────────────────────────────
-- DATOS DE PRUEBA
-- ─────────────────────────────────────────────────────────────────────────

INSERT INTO usuarios (id, password, rol, eliminado, fecha_nacimiento)
SELECT v.id, v.password, v.rol, v.eliminado, v.fecha_nacimiento
FROM (VALUES
    ('U001', '1234',      'ESTUDIANTE', 0, '2003-05-15'),
    ('U002', 'abcd',      'ESTUDIANTE', 0, '2002-08-22'),
    ('U003', 'pass2024',  'ESTUDIANTE', 0, '2004-01-10'),
    ('U004', 'qwerty',    'ESTUDIANTE', 0, '2001-11-03'),
    ('U005', 'admin123',  'ESTUDIANTE', 0, '2000-07-19'),
    ('D001', 'pass123',   'DOCTOR',     0, '1975-03-12'),
    ('D002', 'laura2024', 'DOCTOR',     0, '1980-09-25'),
    ('D003', 'ruizpass',  'DOCTOR',     0, '1982-06-08'),
    ('D004', 'sofiaRX',   'DOCTOR',     0, '1985-12-01'),
    ('D005', 'cardio99',  'DOCTOR',     0, '1978-04-17'),
    ('ADM001', 'adm123',  'ADMIN',      0, '1990-02-28'),
    ('FAR001', 'far123',  'FARMACIA',   0, '1988-10-05')
) AS v(id, password, rol, eliminado, fecha_nacimiento)
WHERE NOT EXISTS (SELECT 1 FROM usuarios u WHERE u.id = v.id);
GO

INSERT INTO estudiantes (id_usuario, nombre, carrera, email)
SELECT v.id_usuario, v.nombre, v.carrera, v.email
FROM (VALUES
    ('U001', 'Juan Perez',     'Ingenieria de Sistemas', 'hola@uni.pe'),
    ('U002', 'Maria Lopez',    'Ingenieria Industrial',  'correo@uni.pe'),
    ('U003', 'Carlos Ramirez', 'Ingenieria Civil',       'prueba@uni.pe'),
    ('U004', 'Ana Torres',     'Ingenieria Electronica', 'yafueya@uni.pe'),
    ('U005', 'Luis Gomez',     'Ingenieria Mecanica',    'arianasapa@uni.pe')
) AS v(id_usuario, nombre, carrera, email)
WHERE NOT EXISTS (SELECT 1 FROM estudiantes e WHERE e.id_usuario = v.id_usuario);
GO

INSERT INTO doctores (id_usuario, nombre, especialidad_id, consultorio, activo)
SELECT v.id_usuario, v.nombre, e.id, v.consultorio, v.activo
FROM (VALUES
    ('D001', 'Dr. Carlos Medina', 'Endocrinologia', 'Consultorio 101', 1),
    ('D002', 'Dra. Laura Pena',   'Endocrinologia', 'Consultorio 102', 0),
    ('D003', 'Dr. Javier Ruiz',   'Odontologia',    'Consultorio 201', 1),
    ('D004', 'Dra. Sofia Torres', 'Radiologia',     'Sala RX-01',      1),
    ('D005', 'Dr. Luis Ramos',    'Cardiologia',    'Consultorio 305', 1)
) AS v(id_usuario, nombre, especialidad_nombre, consultorio, activo)
JOIN especialidades e ON e.nombre = v.especialidad_nombre
WHERE NOT EXISTS (SELECT 1 FROM doctores d WHERE d.id_usuario = v.id_usuario);
GO

INSERT INTO administradores (id_usuario, nombre)
SELECT 'ADM001', 'Administrador General Sotelo'
WHERE NOT EXISTS (SELECT 1 FROM administradores WHERE id_usuario = 'ADM001');
GO

INSERT INTO farmacia_usuarios (id_usuario, nombre)
SELECT 'FAR001', 'Juan Farmaceutico'
WHERE NOT EXISTS (SELECT 1 FROM farmacia_usuarios WHERE id_usuario = 'FAR001');
GO

IF NOT EXISTS (SELECT 1 FROM disponibilidad_doctor WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT disponibilidad_doctor ON;
    INSERT INTO disponibilidad_doctor (id, id_doctor, dia_semana, hora_inicio, hora_fin, eliminado) VALUES
    (1, 'D001', 'Lunes',     '08:00', '10:00', 0),
    (2, 'D001', 'Lunes',     '14:00', '16:00', 0),
    (3, 'D002', 'Martes',    '09:00', '11:00', 0),
    (4, 'D003', 'Miercoles', '11:00', '13:00', 0),
    (5, 'D004', 'Jueves',    '14:00', '16:00', 0),
    (6, 'D005', 'Viernes',   '15:00', '17:00', 0);
    SET IDENTITY_INSERT disponibilidad_doctor OFF;
END;
GO

IF NOT EXISTS (SELECT 1 FROM slots_disponibilidad WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT slots_disponibilidad ON;
    INSERT INTO slots_disponibilidad
    (id, id_disponibilidad, id_doctor, dia_semana, hora_inicio, hora_fin, disponible, eliminado) VALUES
    (1,  1, 'D001', 'Lunes',     '08:00', '08:30', 0, 0),
    (2,  1, 'D001', 'Lunes',     '08:30', '09:00', 1, 0),
    (3,  1, 'D001', 'Lunes',     '09:00', '09:30', 1, 0),
    (4,  1, 'D001', 'Lunes',     '09:30', '10:00', 1, 0),
    (5,  2, 'D001', 'Lunes',     '14:00', '14:30', 1, 0),
    (6,  2, 'D001', 'Lunes',     '14:30', '15:00', 1, 0),
    (7,  2, 'D001', 'Lunes',     '15:00', '15:30', 1, 0),
    (8,  2, 'D001', 'Lunes',     '15:30', '16:00', 1, 0),
    (9,  3, 'D002', 'Martes',    '09:00', '09:30', 1, 0),
    (10, 3, 'D002', 'Martes',    '09:30', '10:00', 1, 0),
    (11, 3, 'D002', 'Martes',    '10:00', '10:30', 1, 0),
    (12, 3, 'D002', 'Martes',    '10:30', '11:00', 1, 0),
    (13, 4, 'D003', 'Miercoles', '11:00', '11:30', 1, 0),
    (14, 4, 'D003', 'Miercoles', '11:30', '12:00', 1, 0),
    (15, 4, 'D003', 'Miercoles', '12:00', '12:30', 1, 0),
    (16, 4, 'D003', 'Miercoles', '12:30', '13:00', 1, 0),
    (17, 5, 'D004', 'Jueves',    '14:00', '14:30', 1, 0),
    (18, 5, 'D004', 'Jueves',    '14:30', '15:00', 1, 0),
    (19, 5, 'D004', 'Jueves',    '15:00', '15:30', 1, 0),
    (20, 5, 'D004', 'Jueves',    '15:30', '16:00', 1, 0),
    (21, 6, 'D005', 'Viernes',   '15:00', '15:30', 1, 0),
    (22, 6, 'D005', 'Viernes',   '15:30', '16:00', 1, 0),
    (23, 6, 'D005', 'Viernes',   '16:00', '16:30', 1, 0),
    (24, 6, 'D005', 'Viernes',   '16:30', '17:00', 1, 0);
    SET IDENTITY_INSERT slots_disponibilidad OFF;
END;
GO

INSERT INTO medicamentos (id, nombre, stock, tipo)
SELECT v.id, v.nombre, v.stock, v.tipo
FROM (VALUES
    ('MED-001', 'Amoxicilina 500mg', 150, 'capsulas'),
    ('MED-002', 'Ibuprofeno 400mg',   80, 'comprimidos'),
    ('MED-003', 'Loratadina 10mg',     0, 'tabletas'),
    ('MED-004', 'Paracetamol 500mg', 220, 'comprimidos'),
    ('MED-005', 'Omeprazol 20mg',     12, 'capsulas'),
    ('MED-006', 'Metformina 850mg',   95, 'comprimidos')
) AS v(id, nombre, stock, tipo)
WHERE NOT EXISTS (SELECT 1 FROM medicamentos m WHERE m.id = v.id);
GO

IF NOT EXISTS (SELECT 1 FROM citas WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT citas ON;
    INSERT INTO citas (id, id_estudiante, id_doctor, id_slot, motivo, estado, eliminado) VALUES
    (1, 'U001', 'D001', 1, 'Control de glucosa', 'ATENDIDA',  0),
    (2, 'U002', 'D001', 2, 'Consulta general',   'PENDIENTE', 0);
    SET IDENTITY_INSERT citas OFF;
END;
GO

IF NOT EXISTS (SELECT 1 FROM atencion_cita WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT atencion_cita ON;
    INSERT INTO atencion_cita (id, id_cita, diagnostico, comentarios) VALUES
    (1, 1, 'Diabetes tipo 2 leve', 'Paciente debe controlar dieta y hacer ejercicio');
    SET IDENTITY_INSERT atencion_cita OFF;
END;
GO

IF NOT EXISTS (SELECT 1 FROM recetas WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT recetas ON;
    INSERT INTO recetas (id, id_atencion, estado) VALUES (1, 1, 'ENTREGADA');
    SET IDENTITY_INSERT recetas OFF;
END;
GO

IF NOT EXISTS (SELECT 1 FROM receta_detalle WHERE id_receta = 1)
BEGIN
    INSERT INTO receta_detalle (id_receta, id_medicamento, dosis, duracion) VALUES
    (1, 'MED-001', '1 capsula cada 8h',      '7 dias'),
    (1, 'MED-006', '1 comp con el desayuno', '30 dias');
END;
GO
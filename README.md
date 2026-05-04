# 🏥 Centro Médico UNI — Sistema de Gestión

Sistema web completo para el Centro Médico de la Universidad Nacional de Ingeniería (UNI).

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Backend | Java 21 + Spring Boot 3.2 |
| Seguridad | Spring Security + JWT |
| Base de datos | PostgreSQL 15 |
| Frontend | React 18 + React Router |
| HTTP Client | Axios |
| ORM | Spring Data JPA / Hibernate |

## Módulos del sistema

- **Autenticación** — Login con JWT, roles diferenciados
- **Pacientes** — CRUD completo con búsqueda por nombre/DNI
- **Médicos** — Gestión de médicos y especialidades
- **Citas** — Agenda, cancelación, atención y reprogramación
- **Historial médico** — Diagnóstico, tratamiento y recetas por cita
- **Medicamentos** — Inventario con alertas de stock bajo

## Roles del sistema

| Rol | Permisos |
|-----|----------|
| `ADMIN` | Acceso total al sistema |
| `MEDICO` | Citas, historial, lectura de pacientes |
| `RECEPCIONISTA` | Pacientes, citas, medicamentos (lectura) |
| `PACIENTE` | Solo sus propias citas |

## Requisitos previos

- Java 21
- Node.js 18+
- PostgreSQL 15
- Maven 3.9+

## Instalación y ejecución

### 1. Base de datos

```bash
# Crear la base de datos
sudo -u postgres psql -c "CREATE DATABASE centro_medico;"

# Configurar contraseña del usuario postgres
sudo -u postgres psql -c "ALTER USER postgres PASSWORD '1234';"
```

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

El servidor arranca en: `http://localhost:8080`

Documentación Swagger: `http://localhost:8080/swagger-ui.html`

### 3. Cargar datos iniciales

```bash
# Ejecutar en PostgreSQL después de que Spring cree las tablas
psql -U postgres -d centro_medico -f docs/seed.sql
```

### 4. Frontend

```bash
cd frontend
npm install
npm start
```

La aplicación abre en: `http://localhost:3000`

## Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | Administrador |
| `medico1` | `admin123` | Médico |
| `medico2` | `admin123` | Médico |
| `recep1` | `admin123` | Recepcionista |

## Con Docker (alternativa)

```bash
# Levantar todo con un solo comando
docker-compose up -d

# Ver logs
docker-compose logs -f backend
```

## Estructura del proyecto

```
centro-medico-system/
├── backend/                    ← Spring Boot API REST
│   └── src/main/java/.../
│       ├── config/             ← JWT, Security, CORS
│       ├── model/              ← Entidades JPA
│       ├── repository/         ← Interfaces Spring Data
│       ├── service/            ← Lógica de negocio
│       ├── controller/         ← Endpoints REST
│       ├── dto/                ← Objetos de transferencia
│       └── exception/          ← Manejo global de errores
├── frontend/                   ← React
│   └── src/
│       ├── services/           ← Llamadas HTTP a la API
│       ├── context/            ← Estado global (AuthContext)
│       ├── components/         ← Sidebar, Layout, ProtectedRoute
│       └── pages/              ← Login, Dashboard, Pacientes...
├── docs/
│   └── seed.sql                ← Datos iniciales de prueba
└── docker-compose.yml
```

## Endpoints principales de la API

```
POST   /api/auth/login
GET    /api/pacientes
POST   /api/pacientes
PUT    /api/pacientes/{id}
DELETE /api/pacientes/{id}
GET    /api/medicos
GET    /api/especialidades
GET    /api/citas
POST   /api/citas
PUT    /api/citas/{id}/cancelar
PUT    /api/citas/{id}/atender
GET    /api/historiales/paciente/{id}
POST   /api/historiales
GET    /api/medicamentos
GET    /api/medicamentos/stock-bajo
PATCH  /api/medicamentos/{id}/stock
```

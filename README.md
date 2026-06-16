<div align="center">

# 🏥 Centro Médico UNI — Sistema de Gestión

![GitHub stars](https://img.shields.io/github/stars/Hector-0-0/centro--medico-system?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/Hector-0-0/centro--medico-system?style=flat-square)

**Sistema de gestión para el Centro Médico de la Universidad Nacional de Ingeniería (UNI)**

</div>

---

## 📋 Sobre el Proyecto

Trabajo del curso de **Programación Orientada a Objetos (POO)** de la UNI. El sistema gestiona pacientes, médicos, citas, historiales, medicamentos y recetas de un centro médico universitario.

Se entrega en **dos versiones** que comparten el mismo dominio, branding institucional (guinda `#711610` + logotipo oficial) y base de datos, de modo que la experiencia es equivalente en escritorio y web.

| Versión | Carpeta | Stack |
|---------|---------|-------|
| 🖥️ **Escritorio** | [`desktop/`](desktop/) | Java 21 + Swing (FlatLaf) + JDBC + PostgreSQL |
| 🌐 **Web** | [`backend/`](backend/) + [`frontend/`](frontend/) | Spring Boot 3.2 (REST + JWT) + React 18 |

## 💻 Tecnologías

### Frontend
![React](https://img.shields.io/badge/React-20232A?style=flat&logo=react&logoColor=61DAFB)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=black)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=flat&logo=axios&logoColor=white)
![React Router](https://img.shields.io/badge/React_Router-CA4245?style=flat&logo=react-router&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)

### Escritorio
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=flat&logo=java&logoColor=white)
![FlatLaf](https://img.shields.io/badge/FlatLaf-2C2C2C?style=flat&logo=java&logoColor=white)

### Bases de datos
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)

### DevOps y herramientas
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=flat&logo=nginx&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white)

## 📦 Módulos del Sistema

| Módulo | Descripción |
|--------|-------------|
| **Autenticación** | Login con JWT y roles diferenciados |
| **Pacientes** | CRUD completo con búsqueda por nombre/DNI |
| **Médicos** | Gestión de médicos y especialidades |
| **Citas** | Agenda, cancelación, atención y reprogramación |
| **Historial médico** | Diagnóstico, tratamiento y recetas por cita |
| **Medicamentos** | Inventario con alertas de stock bajo |
| **Horarios / Disponibilidad** | El médico publica horarios y el paciente agenda sobre ellos |
| **Recetas / Farmacia** | El médico emite recetas; la farmacia las entrega y descuenta stock |

## 👥 Roles del Sistema

| Rol | Permisos |
|-----|----------|
| `ADMIN` | Acceso total al sistema |
| `MEDICO` | Citas, historial y lectura de pacientes |
| `RECEPCIONISTA` | Pacientes, citas y medicamentos (lectura) |
| `PACIENTE` | Solo sus propias citas |

## 🐳 Ejecución con Docker (recomendado)

La forma más rápida de levantar **toda la versión web** (base de datos + API + frontend) en un solo comando:

```bash
git clone https://github.com/Hector-0-0/centro--medico-system.git
cd centro--medico-system

# Levantar PostgreSQL + Backend + Frontend
docker compose up -d --build

# Ver logs del backend
docker compose logs -f backend
```

| Servicio | URL |
|----------|-----|
| 🌐 Frontend (React + Nginx) | http://localhost:3000 |
| ⚙️ Backend (API REST) | http://localhost:8080 |
| 🗄️ PostgreSQL | localhost:5432 |

Para detener todo:

```bash
docker compose down          # conserva los datos
docker compose down -v       # elimina también el volumen de la BD
```

## 🚀 Ejecución manual (sin Docker)

<details>
<summary>Requisitos previos</summary>

- Java 21
- Node.js 18+
- PostgreSQL 15
- Maven 3.9+ (o usar el wrapper `./mvnw`)

</details>

### 1. Base de datos

```bash
sudo -u postgres psql -c "CREATE DATABASE centro_medico;"
sudo -u postgres psql -c "ALTER USER postgres PASSWORD '1234';"
```

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

> Arranca en `http://localhost:8080`. Documentación Swagger en `http://localhost:8080/swagger-ui.html`.

### 3. Datos iniciales

```bash
# Tras el primer arranque (Spring crea las tablas con ddl-auto=update)
psql -U postgres -d centro_medico -f docs/seed.sql
```

### 4. Frontend

```bash
cd frontend
npm install
npm start
```

> Abre en `http://localhost:3000`.

### 5. Versión de escritorio

```bash
cd desktop
mvn clean compile
mvn exec:java          # o ejecutar AplicativoCentroMedico desde NetBeans
```

> La ruta de las imágenes se configura en `desktop/SGH.config`. Si el archivo no existe, la app usa las imágenes embebidas en el classpath.

## 🔑 Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | Administrador |
| `medico1` | `admin123` | Médico |
| `medico2` | `admin123` | Médico |
| `recep1` | `admin123` | Recepcionista |

## 🗂️ Estructura del Proyecto

```
centro-medico-system/
├── desktop/                    ← App de escritorio (Java Swing + JDBC)
│   └── src/main/
│       ├── java/pe/edu/uni/centromedico/
│       │   ├── models/         ← Entidades del dominio
│       │   ├── db/dao/         ← Acceso a datos (JDBC)
│       │   ├── service/        ← Lógica de negocio
│       │   ├── controller/     ← Coordinadores de UI
│       │   ├── ui/             ← frames, panels, dialogs, components
│       │   └── util/           ← Configuración y utilidades
│       └── resources/Images/   ← Logo y banners institucionales UNI
│
├── backend/                    ← Spring Boot API REST
│   ├── Dockerfile
│   ├── mvnw / mvnw.cmd         ← Maven wrapper (no requiere Maven instalado)
│   └── src/main/java/.../
│       ├── config/             ← JWT, Security, CORS
│       ├── model/              ← Entidades JPA
│       ├── repository/         ← Interfaces Spring Data
│       ├── service/            ← Lógica de negocio
│       ├── controller/         ← Endpoints REST
│       ├── dto/                ← Objetos de transferencia
│       └── exception/          ← Manejo global de errores
│
├── frontend/                   ← React (mismo branding que el desktop)
│   ├── Dockerfile / nginx.conf
│   ├── public/images/          ← Logo y banner UNI compartidos
│   └── src/
│       ├── services/           ← Llamadas HTTP a la API
│       ├── context/            ← Estado global (AuthContext)
│       ├── components/         ← Sidebar, Layout, ProtectedRoute
│       └── pages/              ← Login, Dashboard, Pacientes...
│
├── docs/                       ← Esquema BD, seed, colección Postman, diagramas
└── docker-compose.yml
```

## 📡 Endpoints principales de la API

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
GET    /api/disponibilidades
GET    /api/disponibilidades/medico/{medicoId}
POST   /api/disponibilidades
POST   /api/disponibilidades/agendar
GET    /api/recetas
GET    /api/recetas/pendientes
POST   /api/recetas
PUT    /api/recetas/{id}/entregar
```

## 👤 Autor

**Hector-0-0**
- GitHub: [@Hector-0-0](https://github.com/Hector-0-0)

---

<div align="center">
⭐ Si te fue útil, dale una estrella!
</div>

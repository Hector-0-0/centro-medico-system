# 01 · Introducción y arquitectura

## ¿Qué es esta aplicación?

Es el sistema de un **centro médico universitario** (UNI). Permite, según quién inicie sesión:

- **Estudiantes** (los "pacientes"): ver horarios libres de los médicos, agendar citas, ver sus citas y cancelarlas.
- **Doctores**: ver sus citas, atenderlas (poner diagnóstico y receta), definir su disponibilidad y ver el stock de medicinas.
- **Administradores**: ver pacientes, médicos y todas las citas.
- **Farmacia**: ver/entregar recetas y gestionar el stock de medicamentos.

Lo importante para entender el código: **es una aplicación web de tres piezas** que trabajan juntas.

---

## Las tres piezas

```
   ┌───────────────┐        ┌───────────────┐        ┌───────────────┐
   │   FRONTEND    │  HTTP  │    BACKEND    │  SQL   │ BASE DE DATOS │
   │   (React)     │ ─────► │ (Spring Boot) │ ─────► │  (SQL Server) │
   │  el navegador │ ◄───── │   el servidor │ ◄───── │  los datos    │
   └───────────────┘  JSON  └───────────────┘ filas  └───────────────┘
       lo que ve            la lógica y las         donde se guardan
       el usuario            reglas del negocio       los datos
```

### 1. Frontend — React (carpeta `frontend/`)
Es la página web que se abre en el navegador. Está escrita en **JavaScript** con la librería **React**.
No sabe nada de medicina ni de reglas: solo **muestra pantallas** y, cuando el usuario hace clic,
**le pide datos al backend** y los pinta. Por ejemplo: muestra la tabla "Mis Citas".

### 2. Backend — Spring Boot (carpeta `backend/`)
Es un programa hecho en **Java** con el framework **Spring Boot**. Vive en un servidor.
Es **el cerebro**: recibe las peticiones del frontend, aplica las reglas ("solo el dueño puede cancelar su cita",
"un horario ya ocupado no se puede reservar"), consulta la base de datos y devuelve respuestas.
También se encarga de la **seguridad** (¿quién eres?, ¿puedes hacer esto?).

### 3. Base de datos — SQL Server (carpeta `backend/src/main/resources/init_db.sql`)
Es donde se **guardan los datos** de forma permanente: usuarios, citas, médicos, medicamentos, etc.
Aunque apagues el servidor, los datos siguen ahí. El backend le habla en lenguaje **SQL**.

> 🔑 **Dato clave del proyecto:** la app web y la app de escritorio comparten **la misma base de datos**.
> Por eso el backend usa exactamente las mismas tablas y consultas que la versión de escritorio.

---

## ¿Por qué separar en tres piezas?

Porque cada una tiene un trabajo distinto y conviene que sean independientes:

- El frontend puede cambiar de aspecto sin tocar las reglas del negocio.
- El backend puede cambiar de reglas sin que el navegador se entere.
- La base de datos puede crecer sin afectar a los otros dos.

Es como un restaurante: **el salón** (frontend) no cocina; **la cocina** (backend) no atiende mesas;
**la despensa** (base de datos) solo guarda ingredientes. Se comunican con "comandas".

---

## ¿Cómo se comunican? (la idea general)

1. El usuario hace algo en el navegador (por ejemplo, clic en "Agendar").
2. El **frontend** envía una **petición HTTP** al backend (algo como: `POST /api/citas/agendar`).
3. El **backend** recibe la petición, revisa permisos, aplica reglas y habla con la **base de datos**.
4. La base de datos guarda/devuelve filas.
5. El backend arma una **respuesta en JSON** y se la manda de vuelta al frontend.
6. El frontend recibe el JSON y **actualiza la pantalla**.

Todo esto ocurre en una fracción de segundo, cada vez que tocas un botón.

Los términos en negrita (HTTP, JSON, petición, REST...) se explican en el siguiente archivo.

---

## Estructura de carpetas del apartado web

```
centro-medico-system/
├── backend/                 ← El cerebro (Java + Spring Boot)
│   ├── src/main/java/edu/universidad/centromedico/
│   │   ├── controller/      ← Reciben las peticiones HTTP (la "puerta")
│   │   ├── service/         ← La lógica y las consultas SQL (la "cocina")
│   │   ├── dto/             ← Objetos para enviar/recibir datos (las "comandas")
│   │   ├── model/           ← Tablas representadas como clases Java
│   │   ├── repository/      ← Acceso automático a tablas (solo para 3 entidades)
│   │   ├── config/          ← Seguridad, JWT, arranque de la BD
│   │   └── exception/       ← Manejo central de errores
│   └── src/main/resources/
│       ├── application.properties  ← Configuración (BD, JWT, puerto...)
│       └── init_db.sql             ← Crea y rellena la base de datos
│
├── frontend/                ← El salón (JavaScript + React)
│   └── src/
│       ├── pages/           ← Cada pantalla (Login, MisCitas, Pacientes...)
│       ├── components/      ← Piezas reutilizables (menú, layout, diálogos)
│       ├── services/        ← Funciones que llaman al backend (axios)
│       ├── context/         ← Estado global de la sesión (quién está logueado)
│       ├── hooks/           ← Lógica reutilizable de React (cargar datos)
│       └── App.jsx          ← El mapa de rutas (qué URL muestra qué página)
│
└── docker-compose.yml       ← Receta para levantar todo junto
```

---

## Resumen en una frase

> **El frontend (React) pide datos por HTTP → el backend (Spring Boot) aplica reglas y consulta la base de datos (SQL Server) → y devuelve la respuesta en JSON para que el frontend la muestre.**

➡️ Siguiente: **[02-conceptos-base.md](02-conceptos-base.md)** — el "idioma" con el que se hablan estas piezas.

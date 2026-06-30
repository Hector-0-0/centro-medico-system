# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

University (UNI) coursework for Programación Orientada a Objetos. The repo ships the **same medical-center system in two forms** that share one domain, the institutional branding (guinda `#711610` + crema `#F9F5F0`), and — critically — **one single database**:

- `desktop/` — Java 21 + Swing (FlatLaf 3.7 + MigLayout) + raw JDBC. Main class `pe.edu.uni.AplicativoCentroMedico`.
- `backend/` — Spring Boot 3.2.5 (Java 21) REST API: Spring Security + JWT + Swagger. Package `edu.universidad.centromedico`.
- `frontend/` — React 18 (CRA / react-scripts) + React Router + Axios.

> **The README.md is stale.** It still describes an earlier PostgreSQL design with roles ADMIN/MEDICO/RECEPCIONISTA/PACIENTE and `admin/admin123` credentials. The live system uses **SQL Server** and the desktop's roles/credentials (see below). Trust the source, `application.properties`, and `init_db.sql` over the README.

## Shared database (the single most important fact)

All three apps connect to the **same SQL Server** database so the web behaves identically to the desktop on the same data:

- `jdbc:sqlserver://localhost:1433;databaseName=centro_medico`, user `sa`, password `Rodrigo3%`.
- Defined in `backend/.../application.properties` and `desktop/.../resources/config.properties` — if the local SA password differs, **change both**.
- The backend does **not** let Hibernate manage the schema (`ddl-auto=none`). `config/DataInitializer.java` runs `backend/src/main/resources/init_db.sql` (split on `GO`) at startup, creating the DB if absent — this clones the desktop's `DatabaseManager`. Edit `init_db.sql` to change schema/seed.
- **Passwords are stored and compared in plain text** (`AuthService` uses `.equals()`, no BCrypt). This is deliberate, to share the desktop's `usuarios` table. Don't "fix" it to hashing without coordinating both apps.

## Roles & login

Four roles, identical to the desktop: **ESTUDIANTE, DOCTOR, ADMIN, FARMACIA** (`model/Rol.java`). The web maps "Pacientes" → `estudiantes` table, "Médicos" → `doctores`. There is no FARMACIA UI in desktop terms — pharmacy work lives under the FARMACIA role.

Seed credentials (plain text, from `init_db.sql`): `ADM001/adm123`, `70123456/pass123`, `202000154A/1234`, `FAR001/far123`. Student codes are UNI codes (202000154A, 201900233B, 202100487C, 201800712D, 201700598E) and carry a separate `dni` column; **doctor codes are the doctor's DNI** — 8 digits (70123456, 71234567, 72345678, 73456789, 74567890).

Authorization is enforced with `@PreAuthorize("hasRole('…')")` on controllers; `SecurityConfig` only permits `/api/auth/login` and Swagger, everything else requires a valid JWT (subject = user id).

## Backend architecture notes

- **Data access is mostly raw `JdbcTemplate`, not JPA.** Only `Usuario`, `Estudiante`, `Doctor` are real JPA entities/repositories. Citas, slots, atención, recetas, medicamentos, CIE, disponibilidad are all hand-written SQL in their `service/` classes — intentionally, to replicate the desktop DAOs' exact queries and behavior. When adding a feature, mirror the corresponding desktop DAO + panel + controller for exact parity (including which operations exist — e.g. Pacientes/Médicos have no "edit", matching the desktop).
- Per-module layering: `model` (JPA, rare) / `repository` → `dto` → `service` (business logic + SQL) → `controller` (`@PreAuthorize`). Frontend mirror: a `pages/*.jsx` + a `services/*.js`.
- Errors: services throw `RuntimeException`; `GlobalExceptionHandler` turns them into `400 {"error": "..."}` (or `{"campos": {...}}` for validation). Frontend reads these via `services/api.js` `mensajeError(err)`.
- Receta flow also generates a PDF (`RecetaPdfService`, openhtmltopdf) and notifies via n8n webhook (`N8nNotificacionService`, URL from `n8n.webhook.receta.url` / `N8N_WEBHOOK_RECETA_URL`; if blank, notification is skipped).

## Frontend architecture notes

- `components/Layout.jsx` (sidebar + 54px topbar) + `components/Sidebar.jsx` replicate the desktop `MainFrame`/`Sidebar`. `components/menu.js` defines the per-role menu, `landingPath(rol)`, and label lookups — after login users are redirected to their role's landing page.
- `components/Layout.css` holds the guinda+crema palette and reusable classes (`.panel__title`, `.toolbar`, `.btn--primary/--ghost/--danger`, `.table`, `.modal*`, `.field*`, `.card-section`). Reuse these rather than inventing new styles.
- Auth state in `context/AuthContext.jsx`; JWT stored in `localStorage`; routes guarded by `components/ProtectedRoute.jsx`.
- API base URL comes from `frontend/.env` (CRA only reads `.env` at the project root, **not** `src/.env`); if unset, `services/api.js` falls back to `/api` (nginx proxy in prod, CRA `proxy` field in dev).

## Desktop Swing — NetBeans GUI Builder rules (mandatory)

The team edits Swing views visually in NetBeans (Matisse). Any generated/edited view class **must** stay GUI-Builder-compatible:

1. Keep the `// <editor-fold defaultstate="collapsed" desc="Generated Code">` block with `initComponents()` exactly as NetBeans exports it; never put business logic inside it.
2. Declare visual components at the bottom between `// Variables declaration - do not modify` and `// End of variables declaration`.
3. Use `GroupLayout` (Matisse's layout) as the primary layout manager.
4. Panels extend `JPanel`, frames extend `JFrame`, dialogs extend `JDialog`; call `initComponents()` first in the constructor.

Desktop image paths are configured in `desktop/SGH.config` (read by `ConfiguracionParametros`) — **do not delete it**. Edit the `IMAGES =` line to your local `desktop/src/main/resources/Images/` path; if missing, the app falls back to classpath images.

## Common commands

```bash
# Full web stack (SQL Server + backend + frontend + n8n) — recommended
docker compose up -d --build
docker compose logs -f backend
# Frontend http://localhost:3000 · API http://localhost:8080 · Swagger /swagger-ui.html
# SQL Server :1433 · n8n editor http://localhost:5678 (admin/admin)

# Backend (needs SQL Server reachable on localhost:1433; offline arg avoids re-resolving deps)
cd backend && mvn -o spring-boot:run
mvn -o compile            # quick compile check
mvn -o test               # run tests
mvn -o test -Dtest=ClassName#method   # single test

# Frontend
cd frontend && npm install
npm start                 # dev server
CI=true npm run build     # production build, warnings-as-errors (use this to verify)

# Desktop
cd desktop && mvn clean compile
mvn exec:java             # runs pe.edu.uni.AplicativoCentroMedico (or run from NetBeans)
```

The backend tolerates a missing DB at startup (`hikari.initialization-fail-timeout=-1`) so the Spring context boots even without SQL Server; the seed runner creates the DB on first real connection.

## docs/

`docs/` holds `esquema-bd.sql`, `seed.sql`, `postman-collection.json`, and `diagrama-clases.jpeg`. Note these may reflect the older PostgreSQL design — the authoritative web schema is `backend/src/main/resources/init_db.sql`.

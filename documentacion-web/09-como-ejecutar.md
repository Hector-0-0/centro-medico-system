# 09 · Cómo ejecutar el apartado web

Ya entiendes las piezas. Aquí ves cómo **encenderlas**. Hay dos caminos: con **Docker** (todo junto, recomendado)
o **manual** (cada pieza por separado, útil para desarrollar).

---

## 1. Las piezas que deben estar corriendo

Para que la web funcione completa necesitas, como mínimo:

1. **SQL Server** (la base de datos) escuchando en el puerto `1433`.
2. **Backend** (Spring Boot) en el puerto `8080`.
3. **Frontend** (React) en el puerto `3000` (desarrollo) o servido por nginx/Caddy (producción).

Y opcionalmente:
4. **n8n** (puerto `5678`): herramienta externa que envía la receta por correo. Si su URL queda vacía,
   simplemente no se envía nada y el resto funciona igual.

---

## 2. Opción A — Docker Compose (todo junto, recomendado)

**Docker** empaqueta cada pieza en un "contenedor" (una caja aislada con todo lo que necesita).
**Docker Compose** levanta varias cajas a la vez con un solo comando, leyendo el archivo `docker-compose.yml`.

```bash
# Desde la raíz del proyecto
docker compose up -d --build      # construye y levanta todo en segundo plano (-d)
docker compose logs -f backend    # ver los logs del backend en vivo
docker compose down               # apagar todo
```

Qué levanta el `docker-compose.yml` de este proyecto:

| Servicio | Imagen / origen | Puerto | Para qué |
|----------|-----------------|--------|----------|
| `sqlserver` | SQL Server 2022 | 1433 | La base de datos |
| `backend` | se construye desde `./backend` | 8080 | La API REST |
| `frontend` | se construye desde `./frontend` | (interno) | La web (servida por nginx) |
| `caddy` | Caddy 2 | 80 / 443 | Proxy con HTTPS automático, expone el frontend |
| `n8n` | n8n | 5678 | Automatización (correo de recetas) |

Detalles que conviene entender:
- El backend **espera** a que SQL Server esté "sano" antes de arrancar (`depends_on` + `healthcheck`).
- Dentro de Docker, las piezas se llaman por su **nombre de servicio**: el backend ve la base de datos como
  `sqlserver:1433` (no `localhost`), y a n8n como `http://n8n:5678`.
- Los **volúmenes** (`sqlserver_data`, `n8n_data`...) guardan los datos aunque apagues los contenedores.
- Al primer arranque, el backend ejecuta `init_db.sql` y **crea/rellena** la base de datos solo.

### Dónde queda todo (con Docker)
- Web (vía Caddy): `https://localhost` (o tu dominio si configuras `DOMAIN` en un archivo `.env`).
- API: `http://localhost:8080`
- Documentación interactiva de la API (Swagger): `http://localhost:8080/swagger-ui.html`
- Editor de n8n: `http://localhost:5678` (usuario `admin` / clave `admin`).

---

## 3. Opción B — Manual (cada pieza por separado)

Útil mientras programas, porque ves los cambios al instante. Necesitas tener instalados:
**Java 21**, **Maven**, **Node.js + npm**, y un **SQL Server** accesible en `localhost:1433`.

### 3.1 Base de datos
Lo más fácil es levantar **solo** SQL Server con Docker y dejar el resto manual:

```bash
docker compose up -d sqlserver
```

(No tienes que crear las tablas a mano: el backend lo hace al arrancar con `init_db.sql`.)

### 3.2 Backend (Spring Boot)

```bash
cd backend
mvn -o spring-boot:run      # arranca la API en http://localhost:8080
# otros comandos útiles:
mvn -o compile              # solo compilar (chequeo rápido)
mvn -o test                 # correr las pruebas
```

> El backend tolera que la base de datos aún no exista al arrancar
> (`hikari.initialization-fail-timeout=-1`): el contexto de Spring igual inicia y la base se crea
> en la primera conexión real.

### 3.3 Frontend (React)

```bash
cd frontend
npm install                 # instala las librerías (solo la primera vez) → crea node_modules/
npm start                   # arranca la web en http://localhost:3000
CI=true npm run build       # genera la versión de producción (verifica que no haya warnings)
```

En desarrollo, el `proxy` de `package.json` (`"proxy": "http://localhost:8080"`) hace que las llamadas
del frontend a `/api/...` se reenvíen al backend. Así no tienes problemas de CORS en local.

---

## 4. Configuración importante (dónde se ajustan las cosas)

| Qué | Archivo | Detalle |
|-----|---------|---------|
| Conexión a la base de datos | `backend/src/main/resources/application.properties` | `spring.datasource.url/username/password` |
| Clave y duración del JWT | `application.properties` | `app.jwt.secret`, `app.jwt.expiration` (24 h) |
| Orígenes permitidos (CORS) | `backend/.../config/SecurityConfig.java` | la lista de dominios del frontend |
| URL de la API en el frontend | `frontend/.env` (variable `REACT_APP_API_URL`) | si está vacío, usa `/api` |
| Esquema y datos de la BD | `backend/src/main/resources/init_db.sql` | tablas + semilla |
| Dominio en producción | `.env` (a partir de `.env.example`) | `DOMAIN=...` para el HTTPS de Caddy |

> ⚠️ La contraseña de SQL Server (`Rodrigo3%`) aparece en `application.properties` y en `docker-compose.yml`.
> Si la cambias, cámbiala en **ambos** sitios (y en la app de escritorio, que comparte la base).

---

## 5. Credenciales para probar

Una vez todo arriba, entra a la web e inicia sesión con cualquiera de estas cuentas sembradas:

| Rol | Usuario | Contraseña | Qué verás |
|-----|---------|-----------|-----------|
| Administrador | `ADM001` | `adm123` | Pacientes, Médicos, Todas las Citas |
| Doctor | `D001` | `pass123` | Mis Citas, Disponibilidad, Ver Stock |
| Estudiante | `U001` | `1234` | Horarios, Mis Citas |
| Farmacia | `FAR001` | `far123` | Recetas, Gestión Stock |

---

## 6. Verificación rápida (¿está todo bien?)

1. **¿La base responde?** El log del backend debe mostrar `✅ Base de datos SQL Server inicializada`.
2. **¿La API vive?** Abre `http://localhost:8080/swagger-ui.html`: deberías ver la lista de endpoints.
3. **¿El frontend carga?** Abre `http://localhost:3000` (manual) o `https://localhost` (Docker) y mira el login.
4. **¿El login funciona?** Entra con `U001 / 1234`. Si te lleva a "Horarios", las tres capas están conectadas. 🎉

---

## ✅ Resumen final del recorrido

A lo largo de estos 9 archivos viste:

- **01–02** · Qué es la app, sus tres piezas y el idioma con el que se hablan (HTTP, JSON, REST).
- **03** · Dónde viven los datos: tablas de SQL Server, creadas por `init_db.sql`.
- **04–05** · El backend Spring Boot: capas (controller→service→BD), DTOs, JdbcTemplate/JPA,
  y la seguridad con login, JWT, roles y CORS.
- **06–07** · El frontend React: componentes, JSX, estado, hooks, y cómo llama al backend con axios,
  maneja la sesión y protege las rutas.
- **08** · Todo junto en dos recorridos reales (login y agendar cita).
- **09** · Cómo levantar y probar el sistema.

Si quieres seguir aprendiendo, el mejor ejercicio es **elegir un módulo** (por ejemplo, "Medicamentos")
y seguirlo de punta a punta: `pages/VerStock.jsx` → `services/medicamentoService.js` →
`MedicamentoController.java` → `MedicamentoService.java` → tabla `medicamentos`. Verás el mismo patrón
que ya conoces, aplicado a otros datos.

⬅️ Volver al **[índice](00-INDICE.md)**.

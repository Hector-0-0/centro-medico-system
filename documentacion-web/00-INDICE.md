# 📚 Documentación del apartado WEB — Centro Médico UNI

> Esta documentación explica **solo la parte web** del proyecto (no la app de escritorio).
> La parte web está formada por tres piezas: **backend** (Spring Boot), **frontend** (React) y **base de datos** (SQL Server).
>
> Está escrita **desde cero**: no se asume que sepas nada de Spring Boot, React, APIs ni bases de datos.
> Lee los archivos **en orden**. Cada uno se apoya en el anterior.

---

## 🗺️ Cómo leer esta guía

Imagina que vas a construir un restaurante. Para entenderlo necesitas saber:
1. Qué es un restaurante y cómo se reparten el trabajo la cocina, los meseros y la despensa (**arquitectura**).
2. El idioma con el que se comunican (**conceptos base: HTTP, JSON, REST**).
3. Dónde se guardan los ingredientes (**base de datos**).
4. La cocina que prepara los platos (**backend**).
5. El portero que revisa quién entra (**seguridad / login**).
6. El salón que ve el cliente (**frontend**).
7. Cómo el salón le pide platos a la cocina (**conexión frontend ↔ backend**).
8. Un pedido completo de principio a fin (**flujo completo**).
9. Cómo encender todo el restaurante (**ejecución**).

---

## 📂 Índice de archivos

| # | Archivo | Qué aprenderás |
|---|---------|----------------|
| 00 | **00-INDICE.md** (este) | Mapa general y orden de lectura |
| 01 | [01-introduccion-y-arquitectura.md](01-introduccion-y-arquitectura.md) | Qué es la app, las 3 piezas y cómo encajan |
| 02 | [02-conceptos-base.md](02-conceptos-base.md) | Cliente/servidor, HTTP, JSON, API REST, frameworks |
| 03 | [03-base-de-datos.md](03-base-de-datos.md) | SQL Server, tablas, relaciones y datos de prueba |
| 04 | [04-backend-spring-boot.md](04-backend-spring-boot.md) | Qué es Spring Boot, las "anotaciones" y las capas (controller/service/dto/model) |
| 05 | [05-backend-seguridad-jwt.md](05-backend-seguridad-jwt.md) | Login, tokens JWT, filtros, permisos por rol y CORS |
| 06 | [06-frontend-react.md](06-frontend-react.md) | Qué es React, JSX, componentes, props, estado y hooks |
| 07 | [07-frontend-conexion-api.md](07-frontend-conexion-api.md) | Axios, services, interceptores, contexto de sesión y rutas protegidas |
| 08 | [08-flujo-completo.md](08-flujo-completo.md) | Un caso real recorriendo TODAS las capas (agendar una cita) |
| 09 | [09-como-ejecutar.md](09-como-ejecutar.md) | Cómo levantar todo (Docker o manual) |

---

## 🧭 Glosario rápido (para tener a mano)

- **Frontend**: lo que ve y toca el usuario en el navegador (la web). Aquí hecho con **React**.
- **Backend**: el programa que vive en un servidor, recibe peticiones y responde con datos. Aquí hecho con **Spring Boot** (Java).
- **Base de datos**: donde se guardan los datos de forma permanente. Aquí **SQL Server**.
- **API**: el "menú de operaciones" que el backend ofrece al frontend (pedir citas, agendar, etc.).
- **JSON**: el formato de texto con el que viajan los datos entre frontend y backend.
- **JWT**: una "pulsera de festival" que prueba que ya iniciaste sesión, sin volver a poner la contraseña.

> Cada término se explica con calma en su archivo. Aquí solo es para ubicarte.

Cuando estés listo, abre **[01-introduccion-y-arquitectura.md](01-introduccion-y-arquitectura.md)**.

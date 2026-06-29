# 02 · Conceptos base (el "idioma" de la web)

Antes de mirar código necesitas entender unas pocas ideas. Son las mismas en **cualquier** aplicación web,
no solo en este proyecto. Tómalas con calma: con esto, todo lo demás encaja.

---

## 1. Cliente y servidor

- **Cliente**: el programa que **pide**. En la web, normalmente el **navegador** (Chrome, Firefox...).
- **Servidor**: el programa que **responde**. Está en una computadora encendida esperando peticiones.

Analogía: tú (cliente) llamas por teléfono a una pizzería (servidor) y pides una pizza.
Tú no cocinas; solo pides y esperas. El servidor cocina y te entrega.

En este proyecto:
- **Cliente = frontend React** (corre en tu navegador).
- **Servidor = backend Spring Boot** (corre en el puerto 8080).

---

## 2. HTTP — el protocolo de la conversación

**HTTP** es el conjunto de reglas con el que cliente y servidor se hablan por internet.
Cada mensaje del cliente es una **petición (request)** y la respuesta del servidor es una **respuesta (response)**.

Una petición HTTP tiene principalmente:

| Parte | Qué es | Ejemplo |
|-------|--------|---------|
| **Método (verbo)** | La acción que quieres | `GET`, `POST`, `PUT`, `DELETE` |
| **URL / ruta** | A qué recurso | `/api/citas/mias` |
| **Cabeceras (headers)** | Datos extra del mensaje | `Authorization: Bearer eyJ...` |
| **Cuerpo (body)** | Los datos que envías | `{ "idSlot": 5, "motivo": "dolor" }` |

### Los verbos HTTP más usados
- **GET** → "dame información" (leer). Ej: dame mis citas. *No cambia nada.*
- **POST** → "crea algo" o "ejecuta una acción". Ej: agenda esta cita.
- **PUT** → "actualiza algo" que ya existe.
- **DELETE** → "borra algo".

### Códigos de respuesta (status)
El servidor siempre responde con un número que indica cómo fue:
- **200** OK → todo bien.
- **400** Bad Request → enviaste algo mal (dato inválido, regla rota).
- **401** Unauthorized → no estás autenticado (no iniciaste sesión o tu token venció).
- **403** Forbidden → estás autenticado, pero **no tienes permiso** para eso.
- **404** Not Found → no existe esa ruta o recurso.
- **500** Internal Server Error → falló algo dentro del servidor.

> 💡 En este proyecto verás mucho el **400** (errores de reglas, como "el horario ya no está disponible")
> y el **401/403** (problemas de sesión o permisos). Los códigos importan mucho en el frontend.

---

## 3. JSON — el formato de los datos

**JSON** (JavaScript Object Notation) es un formato de texto para representar datos.
Es el "idioma común" en el que viajan los datos entre frontend y backend. Se ve así:

```json
{
  "id": 5,
  "nombreDoctor": "Dr. Carlos Medina",
  "especialidad": "Endocrinologia",
  "estado": "PENDIENTE",
  "horaInicio": "08:30"
}
```

Reglas básicas de JSON:
- Objetos entre llaves `{ }`, con pares `"clave": valor`.
- Listas entre corchetes `[ ]`.
- Tipos: texto (`"hola"`), número (`5`), booleano (`true`/`false`), nulo (`null`).

Una lista de citas en JSON sería:

```json
[
  { "id": 1, "estado": "ATENDIDA"  },
  { "id": 2, "estado": "PENDIENTE" }
]
```

> El backend convierte automáticamente sus objetos Java a JSON al responder,
> y el frontend convierte sus objetos JavaScript a JSON al enviar. No tienes que hacerlo a mano.

---

## 4. API y API REST

Una **API** (Interfaz de Programación de Aplicaciones) es el **conjunto de operaciones** que un programa
ofrece a otros. Piensa en ella como **el menú de un restaurante**: la lista de cosas que puedes pedir.

El menú del backend de este proyecto incluye, por ejemplo:

| Quiero... | Petición HTTP |
|-----------|---------------|
| Iniciar sesión | `POST /api/auth/login` |
| Ver mis citas (estudiante) | `GET /api/citas/estudiante` |
| Agendar una cita | `POST /api/citas/agendar` |
| Cancelar una cita | `POST /api/citas/5/cancelar` |
| Ver todos los pacientes (admin) | `GET /api/estudiantes` |

**REST** es simplemente un **estilo** muy común de diseñar esa API. Sus ideas:
- Cada cosa es un **recurso** identificado por una URL (`/api/citas`, `/api/medicos`).
- Usas los **verbos HTTP** para actuar sobre ellos (GET para leer, POST para crear...).
- Las respuestas suelen ser **JSON**.

Cuando alguien dice "una API REST", quiere decir "un menú de operaciones organizado con URLs + verbos HTTP + JSON".
Eso es exactamente lo que expone este backend.

> 📝 Fíjate que todas las rutas empiezan con `/api/`. Es una convención para distinguir
> "esto es el menú de datos" de "esto es una página web".

---

## 5. ¿Qué es un "framework"?

Un **framework** es un conjunto de herramientas y reglas ya hechas que te dan una **estructura** para
no programar todo desde cero. Te resuelve lo aburrido y repetitivo, y tú solo escribes lo propio de tu app.

- **Spring Boot** es un framework de **Java** para hacer **backends** (servidores).
  Te da gratis: recibir peticiones HTTP, seguridad, conexión a base de datos, conversión a JSON, etc.
- **React** es una librería de **JavaScript** para hacer **frontends** (interfaces de usuario).
  Te da gratis: actualizar la pantalla cuando cambian los datos, organizar la UI en piezas reutilizables, etc.

Analogía: un framework es como una **plantilla de cocina industrial ya montada**.
No construyes los hornos ni la tubería: llegas y cocinas tus platos.

---

## 6. Los dos "ecosistemas": Java/Maven y JavaScript/npm

Cada pieza tiene su lenguaje y su gestor de paquetes (el programa que descarga las librerías que usas).

| | Backend | Frontend |
|---|---------|----------|
| Lenguaje | **Java 21** | **JavaScript** |
| Framework | **Spring Boot** | **React** |
| Gestor de paquetes | **Maven** (archivo `pom.xml`) | **npm** (archivo `package.json`) |
| Carpeta de librerías | `target/` (compilado) | `node_modules/` |
| Cómo se ejecuta | `mvn spring-boot:run` | `npm start` |

- **Maven** lee `backend/pom.xml`, descarga las librerías de Java y compila el proyecto.
- **npm** lee `frontend/package.json`, descarga las librerías de JavaScript a `node_modules/`.

No necesitas memorizar esto; solo saber que **`pom.xml` = recetas del backend** y
**`package.json` = recetas del frontend**.

---

## ✅ Lo que debes llevarte de este archivo

1. **Cliente** pide, **servidor** responde.
2. Hablan por **HTTP**: verbo (GET/POST...) + URL + cabeceras + cuerpo, y la respuesta trae un **código de estado**.
3. Los datos viajan en **JSON**.
4. El backend ofrece una **API REST**: un menú de URLs `/api/...`.
5. **Spring Boot** y **React** son los frameworks que evitan reinventar la rueda.

➡️ Siguiente: **[03-base-de-datos.md](03-base-de-datos.md)** — dónde viven realmente los datos.

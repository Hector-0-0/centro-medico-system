# 08 · Un flujo completo de principio a fin

Hasta aquí viste cada pieza por separado. Ahora vamos a juntarlas siguiendo **dos recorridos reales**,
capa por capa. Si entiendes estos dos, entiendes el sistema entero.

---

## Recorrido A · Iniciar sesión (login)

**Situación:** un estudiante escribe `U001` / `1234` y pulsa "Ingresar".

```
NAVEGADOR (React)                     RED          SERVIDOR (Spring Boot)        BASE DE DATOS
─────────────────                    ─────         ──────────────────────        ─────────────
1. Login.jsx: el usuario
   escribe y pulsa "Ingresar"
        │
2. llama login(user, pass)
   en authService.js
        │
3. api.post('/auth/login',
     {username,password})  ───POST /api/auth/login──►
                                                  4. SecurityConfig: /login es PÚBLICO, deja pasar
                                                  5. AuthController.login()
                                                        │
                                                  6. AuthService.login():
                                                     busca el usuario ───SELECT * FROM usuarios──►
                                                                       ◄──── fila U001 ──────────
                                                     compara password (texto plano) ✔
                                                     busca el nombre ──SELECT nombre FROM estudiantes─►
                                                                       ◄──── "Juan Perez" ─────────
                                                     genera el JWT (firmado)
                                          ◄──200 { token, id, rol, nombre } (JSON)──
7. authService guarda en localStorage:
   token, rol, username, nombre
        │
8. AuthContext.handleLogin():
   marca loggedIn=true, rol="ESTUDIANTE"
        │
9. navigate(landingPath("ESTUDIANTE"))
   → te lleva a /horarios
```

Puntos clave del recorrido:
- El login es **la única** petición sin token (es pública en `SecurityConfig`).
- El backend devuelve el **JWT**; el frontend lo **guarda en `localStorage`** para usarlo en todo lo demás.
- Tras iniciar sesión, te manda a la **página inicial de tu rol** (`landingPath`).

---

## Recorrido B · Agendar una cita

**Situación:** ese mismo estudiante (ya logueado) elige un horario libre y agenda una cita.
Este recorrido toca **todas** las capas y reglas que vimos.

### Paso a paso

```
NAVEGADOR (React)                    RED                SERVIDOR (Spring Boot)            BASE DE DATOS
─────────────────                   ─────               ──────────────────────            ─────────────
1. Horarios.jsx: el estudiante
   hace clic en "Agendar" un slot
        │
2. llama agendarCita(idSlot, motivo)
   en citaService.js
        │
3. api.post('/citas/agendar',
     {idSlot:5, motivo:"dolor"})
        │
   [interceptor de axios añade
    Authorization: Bearer <token>]
        │
   ───POST /api/citas/agendar──────►
       (con el token en la cabecera)
                                          4. JwtAuthFilter: valida el token,
                                             identifica al usuario U001
                                          5. SecurityConfig: la ruta exige sesión ✔
                                          6. @PreAuthorize("hasRole('ESTUDIANTE')") ✔
                                          7. CitaController.agendar():
                                             @Valid revisa que idSlot no sea nulo ✔
                                             toma auth.getName() = "U001"
                                                  │
                                          8. CitaService.agendar()  [@Transactional]
                                             a) ¿el slot existe y está libre?
                                                ──SELECT disponible FROM slots... WHERE id=5──►
                                                ◄──────── disponible = 1 ───────────────────────
                                             b) crea la cita
                                                ──INSERT INTO citas (...) VALUES (...'PENDIENTE')──►
                                             c) ocupa el slot
                                                ──UPDATE slots... SET disponible=0 WHERE id=5──►
                                             (a+b+c se confirman juntas: transacción)
                                ◄────200 OK───────────
9. el service del frontend
   recarga la lista de citas
   (recargar() del hook useCargar)
        │
10. la tabla "Mis Citas" se
    redibuja con la cita nueva
```

### ¿Qué pasa si el horario ya estaba ocupado?

Imagina que entre que cargaste la página y diste clic, **otra persona** reservó ese slot.
En el paso 8a, `disponible` valdría `0`, y el service haría:

```java
if (!disponible) throw new RuntimeException("El horario ya no está disponible");
```

A partir de ahí:

```
SERVIDOR                                          NAVEGADOR
────────                                          ─────────
GlobalExceptionHandler atrapa la excepción
  → responde 400 { "error": "El horario ya no está disponible" }
        │
  ──400──────────────────────────────────────►  axios entra en el .catch()
                                                 mensajeError(err) saca el texto
                                                 alerta("El horario ya no está disponible")
                                                 (la cita NO se crea; el slot sigue como estaba)
```

Y como `agendar` es **`@Transactional`**, si algo fallara a mitad, **nada** queda escrito:
no habría una cita huérfana ni un slot ocupado por error.

---

## Mapa mental: quién hace qué

Junta todo lo que aprendiste. Para **cualquier** acción del sistema, el reparto de responsabilidades es siempre el mismo:

| Capa | Archivo típico | Su responsabilidad | Lo que NO hace |
|------|----------------|--------------------|----------------|
| **Página React** | `pages/Horarios.jsx` | Mostrar la UI, capturar clics, redibujar | No sabe SQL ni reglas |
| **Service React** | `services/citaService.js` | Llamar a la URL correcta con axios | No decide permisos |
| **api.js** | `services/api.js` | Añadir el token, manejar 401, base URL | — |
| **Filtro + Security** | `config/*` | Validar token, exigir sesión y rol | No conoce las reglas del negocio |
| **Controller** | `controller/CitaController.java` | Recibir, validar formato, delegar | No tiene lógica de negocio |
| **Service backend** | `service/CitaService.java` | **Las reglas** + el SQL + transacciones | No habla de HTTP |
| **Base de datos** | tablas SQL Server | Guardar/devolver datos con integridad | No decide nada |

Una petición **baja** por la izquierda (página → service → red → controller → service → BD) y la respuesta
**sube** de vuelta hasta redibujar la pantalla. Cada capa solo se preocupa de lo suyo.

---

## Por qué esta separación es buena

- **Cambiar el aspecto** de "Mis Citas" → solo tocas `pages/MisCitas.jsx`. Nada más se entera.
- **Cambiar una regla** (ej. permitir cancelar citas atendidas) → solo tocas `CitaService.java`.
- **Añadir un módulo nuevo** → copias el patrón: `XController` + `XService` + DTOs en el backend,
  y `pages/X.jsx` + `services/xService.js` en el frontend. Mismo molde siempre.

➡️ Siguiente: **[09-como-ejecutar.md](09-como-ejecutar.md)** — cómo levantar todo esto en tu máquina.

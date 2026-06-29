# 04 · El backend con Spring Boot

Este es el archivo más largo, porque el backend es el cerebro. Vamos por partes y con ejemplos **reales** del proyecto.

---

## 1. ¿Qué es Spring Boot y qué hace por nosotros?

**Spring Boot** es un framework de **Java** para crear servidores (backends). Cuando arranca, hace cosas
automáticamente que de otro modo tendrías que programar tú:

- Levanta un **servidor web** (en el puerto `8080`) que escucha peticiones HTTP.
- Convierte tus objetos Java en **JSON** al responder, y el JSON entrante en objetos Java.
- Conecta con la **base de datos**.
- Crea y conecta solas tus clases (esto se llama **inyección de dependencias**, lo vemos abajo).
- Aplica **seguridad** (login, permisos) — eso lo veremos en el archivo 05.

El programa empieza en una sola clase con `main`:

```java
// CentromedicoApplication.java
@SpringBootApplication
public class CentromedicoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CentromedicoApplication.class, args);
    }
}
```

`@SpringBootApplication` es el interruptor que enciende toda la "magia" de Spring Boot.

---

## 2. ¿Qué son las "anotaciones" (`@Algo`)?

En Java, una **anotación** es una etiqueta que empieza con `@` y se pone encima de una clase, método o variable.
**No ejecuta nada por sí sola**: es una **instrucción para el framework**. Spring las lee y actúa según ellas.

Analogía: son como **etiquetas adhesivas** en cajas de una mudanza. La etiqueta "FRÁGIL" no hace nada,
pero quien mueve las cajas (Spring) la lee y las trata distinto.

Las que más verás en este proyecto:

| Anotación | Significa... |
|-----------|--------------|
| `@RestController` | "Esta clase recibe peticiones HTTP y responde con JSON." |
| `@Service` | "Esta clase contiene lógica de negocio." |
| `@Repository` | "Esta clase accede a la base de datos." |
| `@Component` | "Esta clase es una pieza que Spring debe gestionar." (genérica) |
| `@Configuration` | "Esta clase define configuración del sistema." |
| `@GetMapping`, `@PostMapping` | "Este método responde a un GET / POST en cierta URL." |
| `@RequestMapping("/api/citas")` | "Todas las URLs de esta clase empiezan así." |
| `@PreAuthorize("hasRole('ADMIN')")` | "Solo un ADMIN puede llamar a este método." |
| `@Valid` | "Valida los datos que llegan antes de usarlos." |
| `@Transactional` | "Haz todo esto como una sola operación: o todo, o nada." |

Hay otras de la librería **Lombok**, que explico al final.

---

## 3. La inyección de dependencias (la idea clave de Spring)

Mira este patrón, que se repite en TODO el backend:

```java
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor          // ← Lombok: crea el constructor automáticamente
public class CitaController {

    private final CitaService citaService;   // ← lo necesito, pero NO hago "new CitaService()"
    ...
}
```

Fíjate: el controlador **necesita** un `CitaService`, pero **nunca lo crea con `new`**.
En su lugar, lo declara como campo `final` y **Spring se lo entrega ya construido**. Eso es la
**inyección de dependencias**: tú dices qué necesitas, y el framework te lo da montado.

¿Por qué es bueno? Porque Spring crea **una sola instancia** de cada clase (`@Service`, `@Repository`...)
y la reparte donde haga falta. Tú no te preocupas de fabricar ni conectar objetos.

> `@RequiredArgsConstructor` (de Lombok) es lo que genera el constructor que recibe esos campos `final`.
> Spring usa ese constructor para inyectar. Es por eso que casi todas las clases del backend lo tienen.

---

## 4. Las capas del backend (muy importante)

El backend está organizado en **capas**, cada una con un único trabajo. Una petición las atraviesa en orden:

```
  Petición HTTP
       │
       ▼
 ┌───────────────┐   controller/   "La puerta": recibe la petición, revisa permisos,
 │  CONTROLLER   │                  llama al service y devuelve la respuesta.
 └───────┬───────┘
         ▼
 ┌───────────────┐   service/       "La cocina": la lógica de negocio y las consultas SQL.
 │    SERVICE    │                  Aquí están las reglas ("no puedes cancelar una cita ajena").
 └───────┬───────┘
         ▼
 ┌───────────────┐   Base de datos  Lee/escribe filas vía JdbcTemplate o Repository.
 │  BASE DE DATOS│
 └───────────────┘

  (Atravesando todo:  dto/ = los objetos de datos que entran y salen)
```

Veámoslas una por una con código real.

### 4.1 Controller — la puerta de entrada

El controlador asocia **URLs** con **métodos Java**. No debe tener lógica de negocio: solo recibe, delega y responde.

```java
// controller/CitaController.java
@RestController
@RequestMapping("/api/citas")          // todas las rutas empiezan en /api/citas
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping("/estudiante")          // → GET /api/citas/estudiante
    @PreAuthorize("hasRole('ESTUDIANTE')")   // solo estudiantes
    public List<CitaDTO> deEstudiante(Authentication auth) {
        return citaService.obtenerPorEstudiante(auth.getName());
    }

    @PostMapping("/agendar")            // → POST /api/citas/agendar
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<Void> agendar(@Valid @RequestBody AgendarRequest req,
                                        Authentication auth) {
        citaService.agendar(auth.getName(), req.getIdSlot(), req.getMotivo());
        return ResponseEntity.ok().build();
    }
}
```

Cosas a entender aquí:

- **`@GetMapping("/estudiante")`**: este método responde a `GET /api/citas/estudiante`
  (la base `/api/citas` + `/estudiante`).
- **`@PostMapping("/agendar")`**: responde a `POST /api/citas/agendar`.
- **`@RequestBody AgendarRequest req`**: toma el **JSON del cuerpo** de la petición y lo convierte
  en un objeto Java `AgendarRequest`. (Si el frontend envía `{ "idSlot": 5, "motivo": "dolor" }`,
  llega como `req.getIdSlot()` = 5.)
- **`@Valid`**: antes de entrar al método, valida ese objeto (que no falten campos obligatorios, etc.).
- **`Authentication auth`**: Spring lo inyecta solo; representa **quién hizo la petición**.
  `auth.getName()` es el id del usuario logueado (ej. `U001`). Así el backend sabe de quién son las citas
  **sin que el frontend lo diga** (más seguro).
- **`ResponseEntity`**: una forma de devolver la respuesta controlando el código de estado
  (`.ok()` = 200). Cuando devuelves directamente un objeto (`List<CitaDTO>`), Spring asume 200 y lo convierte a JSON.

### 4.2 Service — la lógica y el SQL

Aquí vive lo que realmente importa: las **reglas** y las **consultas a la base de datos**.

```java
// service/CitaService.java
@Service
@RequiredArgsConstructor
public class CitaService {

    private final JdbcTemplate jdbc;   // herramienta de Spring para ejecutar SQL

    @Transactional                     // o se hacen los dos cambios, o ninguno
    public void agendar(String idEstudiante, int idSlot, String motivo) {
        // 1) ¿Existe el slot y está libre?
        List<Map<String,Object>> filas = jdbc.queryForList(
            "SELECT disponible, id_doctor FROM slots_disponibilidad WHERE id = ? AND eliminado = 0",
            idSlot);
        if (filas.isEmpty()) throw new RuntimeException("El horario no existe");

        Map<String,Object> slot = filas.get(0);
        boolean disponible = /* ...lee el BIT... */;
        if (!disponible) throw new RuntimeException("El horario ya no está disponible");

        // 2) Crea la cita
        jdbc.update(
            "INSERT INTO citas (id_estudiante, id_doctor, id_slot, motivo, estado) " +
            "VALUES (?, ?, ?, ?, 'PENDIENTE')",
            idEstudiante, slot.get("id_doctor"), idSlot, motivo);

        // 3) Marca el slot como ocupado
        jdbc.update("UPDATE slots_disponibilidad SET disponible = 0 WHERE id = ?", idSlot);
    }
}
```

Ideas clave:

- **`JdbcTemplate`** es la herramienta de Spring para ejecutar SQL "a mano":
  - `jdbc.query(...)` / `queryForList(...)` → para **leer** (SELECT).
  - `jdbc.update(...)` → para **escribir** (INSERT, UPDATE, DELETE).
- Los **`?`** en el SQL son **parámetros**: se rellenan con los valores que pasas después.
  Esto evita el ataque "inyección SQL" y es la forma correcta de meter datos variables.
- **`throw new RuntimeException("...")`**: cuando una regla no se cumple, el service "lanza un error"
  con un mensaje. Ese mensaje terminará llegando al frontend como un 400 (lo vemos en la sección 6).
- **`@Transactional`**: agendar hace **dos** cambios (crear la cita y ocupar el slot). Esta anotación
  garantiza que se hagan **los dos o ninguno**. Si el segundo fallara, el primero se deshace
  automáticamente. Así nunca queda una cita sin slot ocupado, ni al revés.

> 🧠 **Por qué tanto SQL a mano y no algo automático:** el backend web debe comportarse **exactamente igual**
> que la app de escritorio sobre la misma base de datos. Por eso copia sus consultas tal cual,
> usando `JdbcTemplate` en lugar de dejar que el framework genere el SQL.

### 4.3 DTO — los objetos que entran y salen

**DTO** = *Data Transfer Object* (objeto de transferencia de datos). Es una clase simple que solo **agrupa datos**
para enviarlos o recibirlos. Son las "comandas" que viajan en el JSON.

Hay dos tipos en el proyecto:
- Los que **entran** (lo que el frontend envía), suelen llamarse `...Request`:

```java
// dto/AgendarRequest.java  → coincide con el JSON { "idSlot": 5, "motivo": "..." }
@Data
public class AgendarRequest {
    @NotNull  private Integer idSlot;   // @NotNull: obligatorio (lo revisa @Valid)
    private String motivo;
}
```

- Los que **salen** (lo que el backend responde), como `CitaDTO`:

```java
// dto/CitaDTO.java
@Data @NoArgsConstructor @AllArgsConstructor
public class CitaDTO {
    private int id;
    private String nombreEstudiante;
    private String nombreDoctor;
    private String especialidad;
    private String estado;
    private String horaInicio;
    // ...
}
```

Cuando el service devuelve un `CitaDTO`, Spring lo convierte a este JSON automáticamente:

```json
{ "id": 1, "nombreDoctor": "Dr. Carlos Medina", "especialidad": "Endocrinologia", "estado": "ATENDIDA" }
```

> ¿Por qué no devolver directamente las tablas? Porque el DTO te deja **elegir y combinar** justo lo que el
> frontend necesita (ej. una cita ya viene con el nombre del doctor y la especialidad juntos),
> y **ocultar** lo que no debe salir (ej. la contraseña).

### 4.4 ¿Cómo se llena un DTO desde la base de datos? El RowMapper

La base de datos devuelve **filas**; el `RowMapper` es la función que convierte **una fila → un objeto Java**:

```java
// dentro de CitaService.java
private static final RowMapper<CitaDTO> MAPPER = (rs, n) -> new CitaDTO(
    rs.getInt("id"),
    rs.getString("id_estudiante"),
    rs.getString("nombre_estudiante"),
    // ...una llamada por cada columna del SELECT...
);
```

`rs` es la fila actual; `rs.getString("nombre_doctor")` lee esa columna. El resultado es un `CitaDTO` listo.
`jdbc.query(SQL, MAPPER)` aplica este mapper a **cada fila** y te devuelve la lista completa.

### 4.5 Model y Repository — la otra forma (JPA)

Para tres entidades concretas (`Usuario`, `Estudiante`, `Doctor`) el proyecto **sí** usa el modo automático
de Spring, llamado **JPA**. Funciona así:

```java
// model/Usuario.java  → una clase "espejo" de la tabla usuarios
@Entity                       // "esta clase es una tabla"
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id  private String id;   // @Id = clave primaria
    private String password;
    @Enumerated(EnumType.STRING) private Rol rol;   // se guarda el texto del rol
    private boolean eliminado;
}
```

```java
// repository/UsuarioRepository.java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByIdAndEliminadoFalse(String id);   // ← ¡no escribes el SQL!
}
```

Lo asombroso de **JPA**: tú declaras un **método con un nombre descriptivo**
(`findByIdAndEliminadoFalse`) y Spring **genera el SQL solo** (un `SELECT ... WHERE id = ? AND eliminado = 0`).
`JpaRepository` ya trae gratis `save()`, `findAll()`, `deleteById()`, etc.

> Entonces, ¿JdbcTemplate o JPA? En este proyecto conviven a propósito:
> - **JPA** para lo simple (login, perfiles de estudiante/doctor).
> - **JdbcTemplate (SQL a mano)** para todo lo demás (citas, slots, recetas...), para clonar exactamente
>   las consultas del escritorio.

---

## 5. Recorrido de carpetas del backend

```
backend/src/main/java/edu/universidad/centromedico/
├── CentromedicoApplication.java   ← arranque (main)
├── controller/   ← reciben HTTP y devuelven JSON          (la puerta)
├── service/      ← lógica de negocio + SQL                (la cocina)
├── dto/          ← objetos de datos que entran/salen      (las comandas)
├── model/        ← clases-tabla para JPA (Usuario, etc.)
├── repository/   ← acceso JPA automático (3 entidades)
├── config/       ← seguridad, JWT, arranque de BD         (ver archivo 05)
└── exception/    ← manejo central de errores
```

Cada "módulo" sigue el mismo patrón: `XController` → `XService` → DTOs. Por ejemplo:
- Citas: `CitaController` → `CitaService` → `CitaDTO`, `AgendarRequest`.
- Medicamentos: `MedicamentoController` → `MedicamentoService` → `MedicamentoDTO`...
- Recetas: `RecetaController` → `RecetaService` (+ `RecetaPdfService` que genera el PDF, y
  `N8nNotificacionService` que envía un correo por una herramienta externa llamada n8n).

Si entiendes el módulo de Citas, entiendes todos: cambian los nombres y el SQL, no la estructura.

---

## 6. El manejo de errores (cómo un error se vuelve un 400)

Cuando un service hace `throw new RuntimeException("El horario ya no está disponible")`,
¿cómo llega ese texto al usuario? Gracias a un **manejador global de errores**:

```java
// exception/GlobalExceptionHandler.java  (idea)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handle(RuntimeException ex) {
        return ResponseEntity.badRequest()          // código 400
            .body(Map.of("error", ex.getMessage())); // { "error": "El horario ya no está disponible" }
    }
    // y otro para errores de validación de @Valid → { "campos": { "idSlot": "obligatorio" } }
}
```

`@RestControllerAdvice` significa "vigila los errores de **todos** los controladores". Así no tienes que
escribir `try/catch` en cada método: cualquier `RuntimeException` se transforma automáticamente en un JSON
de error con código **400**, y el frontend lo muestra. (El frontend lo lee con la función `mensajeError`, archivo 07.)

---

## 7. Lombok (las anotaciones que ahorran código)

**Lombok** es una librería que **genera código repetitivo por ti** en tiempo de compilación.
Verás estas etiquetas en muchas clases:

| Anotación de Lombok | Genera automáticamente... |
|---------------------|---------------------------|
| `@Data` | Los *getters* y *setters* (`getNombre()`, `setNombre()`), `toString()`, `equals()`... |
| `@NoArgsConstructor` | Un constructor vacío `new CitaDTO()`. |
| `@AllArgsConstructor` | Un constructor con todos los campos `new CitaDTO(1, "Juan", ...)`. |
| `@RequiredArgsConstructor` | Un constructor con los campos `final` (el que usa la inyección de dependencias). |

Sin Lombok, una clase DTO tendría 60 líneas de getters/setters escritos a mano. Con `@Data`, una.

---

## ✅ Resumen

1. **Spring Boot** levanta el servidor, conecta clases solo (**inyección de dependencias**) y traduce a/desde JSON.
2. Las **anotaciones** `@...` son etiquetas que le dicen a Spring qué hacer con cada clase/método.
3. El flujo es **Controller** (puerta) → **Service** (lógica + SQL) → **base de datos**, con **DTOs** transportando datos.
4. El SQL se hace casi todo a mano con **JdbcTemplate**; solo 3 entidades usan **JPA** automático.
5. Los errores se centralizan y se vuelven **400 + JSON** legible.
6. **Lombok** elimina el código repetitivo.

➡️ Siguiente: **[05-backend-seguridad-jwt.md](05-backend-seguridad-jwt.md)** — el login y los permisos.

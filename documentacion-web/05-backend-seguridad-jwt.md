# 05 · Seguridad: login, JWT y permisos

Una app médica no puede dejar que cualquiera vea las citas de otros. Aquí se explica cómo el backend
sabe **quién eres** (autenticación) y **qué puedes hacer** (autorización).

---

## 1. El problema: HTTP no recuerda

HTTP es **sin memoria** (*stateless*): cada petición es independiente y el servidor no recuerda la anterior.
Entonces, si inicias sesión en una petición... ¿cómo sabe el servidor, en la **siguiente** petición, que ya
te identificaste? No le vas a mandar tu contraseña cada vez (sería inseguro y molesto).

La solución de este proyecto: **JWT**.

---

## 2. Qué es un JWT (la "pulsera del festival")

Un **JWT** (JSON Web Token) es un **texto firmado** que el servidor te entrega cuando inicias sesión correctamente.
A partir de ahí, lo adjuntas en cada petición como prueba de identidad.

Analogía: en un festival pagas en la entrada y te ponen una **pulsera**. Dentro ya no muestras el boleto ni
pagas de nuevo: enseñas la pulsera. Nadie puede falsificarla porque tiene un sello especial.

Un JWT se ve así (tres partes separadas por puntos):

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVMDAxIiwicm9sIjoiRVNUVURJQU5URSJ9.4f9c0...firma
└── cabecera ──────┘ └── datos (claims) ──────────────────────────┘ └─ firma ─┘
```

- **Datos (claims)**: información dentro del token. Aquí guarda el `sub` (subject = id del usuario, ej. `U001`),
  el `rol` y la fecha de expiración.
- **Firma**: un sello hecho con una **clave secreta** que solo conoce el servidor. Si alguien altera el token,
  la firma deja de cuadrar y el servidor lo rechaza. **No se puede falsificar sin la clave secreta.**

> ⚠️ El contenido del token **no está cifrado**, solo firmado. Cualquiera puede leer los datos (están en base64).
> Por eso nunca se mete información sensible dentro; solo el id y el rol.

En este proyecto el JWT lo gestiona `config/JwtService.java`:

```java
// JwtService.java — generar el token al iniciar sesión
public String generateToken(String username, String rol) {
    return Jwts.builder()
        .setSubject(username)               // sub = id del usuario (U001)
        .claim("rol", rol)                  // guarda el rol dentro
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration)) // caduca (24 h)
        .signWith(getSignKey(), SignatureAlgorithm.HS256)   // lo firma con la clave secreta
        .compact();
}
```

La clave secreta y la duración están en `application.properties`:

```properties
app.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
app.jwt.expiration=86400000   # milisegundos = 24 horas
```

---

## 3. El login paso a paso

El login es el **único** punto donde sí envías usuario y contraseña. Está en `AuthController` → `AuthService`.

```java
// service/AuthService.java
public LoginResponse login(LoginRequest request) {
    // 1) Busca el usuario por su id (descartando los eliminados)
    Usuario usuario = usuarioRepository.findByIdAndEliminadoFalse(request.getUsername())
        .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

    // 2) Compara la contraseña EN TEXTO PLANO
    if (!usuario.getPassword().equals(request.getPassword())) {
        throw new RuntimeException("Credenciales incorrectas");
    }

    // 3) Genera el token y lo devuelve junto con id, rol y nombre
    String token = jwtService.generateToken(usuario.getId(), usuario.getRol().name());
    return new LoginResponse(token, usuario.getId(), usuario.getRol().name(), resolverNombre(usuario));
}
```

Detalles importantes:

- **Contraseña en texto plano (sin cifrar):** mira que compara con `.equals()`. Esto es **a propósito**:
  la app web comparte la tabla `usuarios` con la app de escritorio, que guarda las contraseñas así.
  En un sistema real **esto no se hace nunca** (se usaría cifrado tipo BCrypt), pero aquí se mantiene
  para que ambas apps funcionen sobre los mismos datos. Es una decisión consciente del proyecto.
- **El nombre vive en otra tabla:** `resolverNombre()` mira el rol y busca el nombre legible en la tabla
  correcta (`estudiantes`, `doctores`, `administradores` o `farmacia_usuarios`). Recuerda del archivo 03 que
  `usuarios` solo tiene login + rol.
- La respuesta (`LoginResponse`) lleva: `token`, `id`, `rol` y `nombre`. El frontend guardará todo eso.

---

## 4. ¿Qué pasa en CADA petición después del login? El filtro JWT

Una vez que el frontend tiene el token, lo manda en **todas** las peticiones, en una cabecera:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

(La palabra `Bearer` —"portador"— es la convención estándar antes del token.)

En el servidor, un **filtro** intercepta cada petición **antes** de que llegue al controlador y valida ese token.
Es `config/JwtAuthFilter.java`:

```java
// JwtAuthFilter.java (resumido)
String authHeader = request.getHeader("Authorization");

if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);   // sin token → sigue (y luego será rechazado si la ruta lo exige)
    return;
}

String token = authHeader.substring(7);        // quita "Bearer "

if (jwtService.isTokenValid(token)) {           // ¿la firma cuadra y no expiró?
    String username = jwtService.extractUsername(token);     // saca el id del token
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    // Registra al usuario como "autenticado" para esta petición:
    var authToken = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());    // authorities = sus roles
    SecurityContextHolder.getContext().setAuthentication(authToken);
}

filterChain.doFilter(request, response);        // continúa hacia el controlador
```

Qué hace, en cristiano:
1. Lee la cabecera `Authorization`.
2. Si no hay token, deja pasar (la petición seguirá sin estar autenticada).
3. Si hay token, comprueba que sea **válido** (firma correcta y no caducado).
4. Si es válido, mete al usuario y sus roles en el "contexto de seguridad" de esta petición.
   Desde aquí, el `Authentication auth` que veías en los controladores ya tiene el id y el rol.

> Un "filtro" es un guardia que revisa cada petición **antes** de que llegue a su destino.
> `OncePerRequestFilter` garantiza que se ejecuta una sola vez por petición.

---

## 5. Las reglas: qué rutas son públicas y cuáles no

`config/SecurityConfig.java` define las reglas generales:

```java
// SecurityConfig.java (resumido)
http
  .cors(...)                                              // permite al frontend llamar (ver sección 7)
  .csrf(csrf -> csrf.disable())                           // se desactiva CSRF (no usamos cookies de sesión)
  .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))  // sin sesión en el servidor: todo va en el JWT
  .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/auth/login").permitAll()     // login: PÚBLICO
      .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()  // documentación: pública
      .anyRequest().authenticated()                       // TODO lo demás: requiere token válido
  )
  .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // mete nuestro filtro
```

Traducción:
- `/api/auth/login` y la documentación (Swagger) son **públicas**.
- **Cualquier otra ruta** exige estar autenticado (token válido). Si no, responde **401**.
- `STATELESS` = el servidor **no guarda** ninguna sesión; la identidad va siempre en el token.

---

## 6. Permisos por rol: `@PreAuthorize`

Estar logueado no basta: un estudiante no debe ver el panel del admin. Eso se controla método por método con
`@PreAuthorize`, que ya viste en los controladores:

```java
@GetMapping                              // GET /api/citas  → "todas las citas"
@PreAuthorize("hasRole('ADMIN')")        // SOLO admin
public List<CitaDTO> listar() { ... }

@GetMapping("/mias")                     // GET /api/citas/mias
@PreAuthorize("hasRole('DOCTOR')")       // SOLO doctor
public List<CitaDTO> mias(Authentication auth) { ... }

@PostMapping("/{id}/cancelar")
@PreAuthorize("hasAnyRole('ESTUDIANTE','DOCTOR')")   // estudiante O doctor
public ResponseEntity<Void> cancelar(...) { ... }
```

- `hasRole('ADMIN')` → solo ese rol.
- `hasAnyRole('ESTUDIANTE','DOCTOR')` → cualquiera de esos.
- Si un usuario sin permiso lo intenta, Spring responde **403 Forbidden** antes de ejecutar el método.

> 🔒 **Dos niveles de seguridad sobre el dueño del dato:** además del rol, el *service* vuelve a comprobar
> la propiedad. En `cancelar()`, verifica que la cita sea realmente del estudiante/doctor que la pide
> (`esDueño`). Así, aunque tengas el rol DOCTOR, no puedes cancelar la cita de **otro** doctor.

`@EnableMethodSecurity` (en `SecurityConfig`) es lo que activa que `@PreAuthorize` funcione.

---

## 7. CORS: permitir que el frontend llame al backend

Por seguridad, los navegadores **bloquean** que una web de un origen (ej. `http://localhost:3000`, el frontend)
llame a un servidor de **otro** origen (ej. `http://localhost:8080`, el backend). Eso se llama política de
**CORS** (Cross-Origin Resource Sharing). Para permitirlo, el backend declara explícitamente quién puede llamarlo:

```java
// SecurityConfig.java
config.setAllowedOrigins(List.of(
    "https://centromedicouni.me", "https://www.centromedicouni.me", "http://localhost:3000"));
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
config.setAllowedHeaders(List.of("*"));
config.setAllowCredentials(true);
```

Esto dice: "acepto peticiones que vengan del frontend en `localhost:3000` (desarrollo) o del dominio real
(producción), con estos métodos y cualquier cabecera". Sin esto, el navegador rechazaría las llamadas.

> En producción, si el frontend y el backend van detrás del mismo servidor (nginx/Caddy haciendo de proxy),
> comparten origen y CORS deja de ser un problema. En desarrollo local sí hace falta esta lista.

---

## 8. ¿Y la creación de la base de datos al arrancar?

También vive en `config/`. La clase `DataInitializer.java` se ejecuta una vez al arrancar
(implementa `CommandLineRunner`, que Spring corre al terminar de iniciar):

```java
// DataInitializer.java (idea)
String baseUrl = url.replaceFirst(";databaseName=[^;]+", ""); // conecta a 'master', sin elegir BD
try (Connection conn = DriverManager.getConnection(baseUrl, username, password)) {
    String script = /* lee init_db.sql */;
    for (String batch : script.split("(?im)^\\s*GO\\s*$")) {   // parte por cada 'GO'
        conn.createStatement().execute(batch);                 // ejecuta cada trozo
    }
}
```

Es decir: lee `init_db.sql` (archivo 03), lo parte por los `GO` y lo ejecuta, creando la base si no existe.
Como el script es idempotente, arrancar muchas veces no rompe nada.

---

## ✅ Resumen

1. Tras el **login** correcto, el backend entrega un **JWT** firmado (la "pulsera").
2. El frontend manda ese token en cada petición (`Authorization: Bearer ...`).
3. El **filtro JWT** valida el token en cada petición e identifica al usuario.
4. `SecurityConfig` define qué es público y qué exige token (**401** si falta).
5. `@PreAuthorize` restringe cada método por **rol** (**403** si no corresponde), y el service revalida la propiedad.
6. **CORS** autoriza al frontend a llamar al backend desde otro origen.
7. Las contraseñas están **en texto plano a propósito** (para compartir datos con el escritorio).

➡️ Siguiente: **[06-frontend-react.md](06-frontend-react.md)** — ahora la parte que ve el usuario.

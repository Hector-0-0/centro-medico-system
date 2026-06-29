# 07 · La conexión frontend ↔ backend

Aquí se cierra el círculo: cómo el frontend (React) le pide datos al backend (Spring Boot),
cómo maneja la sesión y cómo protege las pantallas. Esta es la "tubería" entre las dos piezas.

---

## 1. Axios: la herramienta para llamar al backend

**Axios** es una librería de JavaScript para hacer **peticiones HTTP** desde el navegador.
Es el "teléfono" con el que el frontend llama a la API del backend.

El proyecto crea **una instancia central** de axios en `frontend/src/services/api.js` y la reutiliza en todos lados:

```js
// services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '/api',   // a dónde apuntan todas las llamadas
});
```

- **`baseURL`**: el prefijo que se añade a todas las peticiones. Si pides `/citas/estudiante`, en realidad va a
  `<baseURL>/citas/estudiante`.
- Por defecto es `/api` (relativo). En **desarrollo**, el `proxy` de `package.json`
  (`"proxy": "http://localhost:8080"`) reenvía `/api/...` al backend en el puerto 8080.
  En **producción**, un servidor (nginx/Caddy) hace de intermediario hacia el backend.
- Se puede sobreescribir con la variable de entorno `REACT_APP_API_URL` si el backend vive en otra dirección.

---

## 2. Interceptores: lo que axios hace automáticamente en cada llamada

Un **interceptor** es código que axios ejecuta **antes de cada petición** o **después de cada respuesta**,
sin que tengas que repetirlo. Este proyecto usa dos, y son muy importantes.

### Interceptor de petición: adjunta el token JWT

Recuerda del archivo 05 que cada petición debe llevar el token. En vez de añadirlo a mano cada vez,
el interceptor lo pone solo:

```js
// services/api.js
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');   // el token guardado al iniciar sesión
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;   // lo añade a la cabecera
  }
  return config;
});
```

> **`localStorage`** es un almacén de texto del navegador que **sobrevive a recargas y a cerrar la pestaña**.
> Aquí se guarda el token, el rol y el nombre del usuario tras el login. Por eso, si recargas la página,
> sigues con la sesión iniciada.

### Interceptor de respuesta: maneja el 401 (sesión vencida)

```js
// services/api.js
api.interceptors.response.use(
  (response) => response,                     // si todo va bien, no hace nada
  (error) => {
    const enLogin = window.location.pathname === '/login' || window.location.pathname === '/';
    if (error.response?.status === 401 && !enLogin) {
      localStorage.clear();                   // borra la sesión
      window.location.href = '/login';        // y te manda al login
    }
    return Promise.reject(error);             // deja que la página también vea el error
  }
);
```

Traducción: si el backend responde **401** (token vencido o inválido) y no estás ya en el login,
borra la sesión y te lleva a iniciar sesión de nuevo. El **403** (sin permiso) **no** cierra sesión:
se deja pasar para que la pantalla muestre el mensaje correspondiente.

---

## 3. Los "services": un archivo por módulo

Para no esparcir llamadas a axios por toda la app, cada módulo tiene su archivo de funciones en
`frontend/src/services/`. Cada función representa **una operación del menú del backend**. Por ejemplo:

```js
// services/citaService.js
import api from './api';

// GET /api/citas/estudiante  → lista de citas del estudiante logueado
export const listarCitasEstudiante = () =>
  api.get('/citas/estudiante').then((r) => r.data);

// POST /api/citas/agendar  → agenda una cita
export const agendarCita = (idSlot, motivo) =>
  api.post('/citas/agendar', { idSlot, motivo }).then((r) => r.data);

// POST /api/citas/5/cancelar  → cancela la cita 5
export const cancelarCita = (idCita) =>
  api.post(`/citas/${idCita}/cancelar`).then((r) => r.data);
```

Detalles:
- `api.get(url)` hace un **GET**; `api.post(url, cuerpo)` hace un **POST** con un cuerpo JSON.
- `.then((r) => r.data)` extrae **solo los datos** de la respuesta (axios envuelve todo en un objeto `r`;
  los datos están en `r.data`).
- Estas funciones son lo que las páginas (archivo 06) importan y usan. La página
  **no sabe** que por debajo hay axios, tokens ni URLs: solo llama `listarCitasEstudiante()`.

Hay un service por módulo del backend: `authService.js`, `citaService.js`, `medicamentoService.js`,
`doctorService.js`, `estudianteService.js`, `recetaService.js`, etc. Cada uno **refleja** a su controlador del backend.

---

## 4. Mensajes de error legibles: `mensajeError`

Recuerda (archivo 04) que el backend devuelve errores como `{ "error": "El horario ya no está disponible" }`
o `{ "campos": { "idSlot": "obligatorio" } }`. La función `mensajeError` en `api.js` saca de ahí un texto bonito:

```js
// services/api.js
export const mensajeError = (err, porDefecto = 'Ocurrió un error. Inténtalo de nuevo.') => {
  const data = err?.response?.data;
  if (!data) return porDefecto;
  if (data.campos) return Object.values(data.campos)[0];  // primer error de validación
  return data.error || porDefecto;                         // o el mensaje general
};
```

Por eso en las páginas ves cosas como:

```jsx
catch (err) {
  alerta(mensajeError(err, 'No se pudo cancelar la cita.'));
}
```

Así el usuario ve "El horario ya no está disponible" en lugar de un error técnico.

---

## 5. La sesión global: AuthContext

¿Cómo saben **todas** las pantallas si hay alguien logueado, su rol y su nombre, sin pasárselo de una en una?
Con un **Context** de React: un almacén de datos **global** que cualquier componente puede leer.

```jsx
// context/AuthContext.jsx (resumido)
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [loggedIn, setLoggedIn] = useState(isLoggedIn);   // ¿hay token?
  const [rol,      setRol]      = useState(getRol);       // rol guardado
  const [username, setUsername] = useState(getUsername);

  const handleLogin = async (data) => {     // se llama tras un login correcto
    setLoggedIn(true);
    setRol(data.rol);
    setUsername(data.username);
    await cargarPerfil();                   // trae el nombre completo (GET /auth/me)
  };

  const handleLogout = () => { /* limpia todo y vuelve al login */ };

  return (
    <AuthContext.Provider value={{ loggedIn, rol, username, handleLogin, handleLogout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);   // atajo para leer la sesión
```

Cualquier componente lee la sesión con una línea:

```jsx
const { loggedIn, rol, handleLogout } = useAuth();
```

- El `<AuthProvider>` envuelve toda la app (en `App.jsx`), así que **todos** los componentes ven la sesión.
- `useAuth()` es el atajo para leerla. Lo usan el menú (para mostrar opciones según el rol),
  la barra superior (para el nombre) y la protección de rutas.

> El flujo completo del login: `Login.jsx` llama a `login()` del service → guarda token/rol en `localStorage`
> → llama a `handleLogin()` del contexto → el contexto pide el nombre con `GET /api/auth/me` → y navega a la
> página inicial del rol.

---

## 6. Las rutas: qué URL muestra qué página (`App.jsx`)

**React Router** es la librería que asocia **URLs del navegador** con **componentes de página**.
Todo el mapa está en `App.jsx`:

```jsx
// App.jsx (resumido)
const RUTAS = [
  { path: '/pacientes',   element: <Pacientes />,  roles: ['ADMIN'] },
  { path: '/citas-medico',element: <CitasMedico/>, roles: ['DOCTOR'] },
  { path: '/horarios',    element: <Horarios />,   roles: ['ESTUDIANTE'] },
  { path: '/mis-citas',   element: <MisCitas />,   roles: ['ESTUDIANTE'] },
  { path: '/recetas',     element: <Recetas />,    roles: ['FARMACIA'] },
  { path: '/perfil',      element: <Perfil />,     roles: TODOS },
];

<BrowserRouter>
  <Routes>
    <Route path="/login" element={<Login />} />            {/* público */}

    <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>   {/* área con sesión */}
      {RUTAS.map((r) => (
        <Route key={r.path} path={r.path}
               element={<ProtectedRoute roles={r.roles}>{r.element}</ProtectedRoute>} />
      ))}
    </Route>

    <Route path="*" element={<Navigate to="/login" replace />} />  {/* cualquier otra URL → login */}
  </Routes>
</BrowserRouter>
```

- Cada `<Route path=... element=.../>` dice: "cuando la URL sea esta, muestra este componente".
- Las páginas privadas van envueltas en `<Layout>` (el marco con menú lateral + barra superior).
- Cada ruta declara qué **roles** pueden verla. Fíjate que **coincide** con los permisos del backend
  (`@PreAuthorize`). El frontend duplica la regla solo para la experiencia; **la seguridad real está en el backend**.

---

## 7. Rutas protegidas: `ProtectedRoute`

Este componente decide si te deja ver una página o te redirige:

```jsx
// components/ProtectedRoute.jsx
export default function ProtectedRoute({ children, roles }) {
  const { loggedIn, rol } = useAuth();
  if (!loggedIn) return <Navigate to="/login" replace />;          // sin sesión → al login
  if (roles && !roles.includes(rol))                              // con rol equivocado →
    return <Navigate to={landingPath(rol)} replace />;            // a la página inicial de TU rol
  return children;                                                 // todo bien → muestra la página
}
```

Esto evita que, por ejemplo, un ESTUDIANTE escriba `/pacientes` en la barra de direcciones y vea un panel vacío.
Lo reenvía a su propia página de inicio.

> Importante volver a decirlo: esto es solo **comodidad visual**. Aunque alguien burle esta comprobación,
> el backend rechazará la petición con **403** gracias a `@PreAuthorize`. **El frontend nunca es la seguridad real.**

---

## 8. El menú por rol (`menu.js`)

Qué opciones aparecen en la barra lateral depende del rol, y se define en `components/menu.js`:

```js
// components/menu.js (resumido)
export const MENU = {
  ESTUDIANTE: [ {label:'Horarios', path:'/horarios'}, {label:'Mis Citas', path:'/mis-citas'}, ... ],
  DOCTOR:     [ {label:'Mis Citas', path:'/citas-medico'}, {label:'Disponibilidad', path:'/disponibilidad'}, ... ],
  ADMIN:      [ {label:'Pacientes', path:'/pacientes'}, {label:'Médicos', path:'/medicos'}, ... ],
  FARMACIA:   [ {label:'Recetas', path:'/recetas'}, {label:'Gestión Stock', path:'/gestion-stock'}, ... ],
};

// Página a la que ir tras el login: el primer item del menú de tu rol
export const landingPath = (rol) => MENU[rol]?.[0]?.path || '/login';
```

`Sidebar.jsx` lee este objeto según tu rol y pinta solo tus opciones. `landingPath(rol)` decide a dónde te
lleva justo después de iniciar sesión (la primera opción de tu menú).

---

## ✅ Resumen

1. **Axios** (`api.js`) es el teléfono al backend; su `baseURL` apunta a `/api`.
2. Un **interceptor** añade el **token JWT** a cada petición; otro maneja el **401** cerrando la sesión.
3. Los **services** (uno por módulo) envuelven cada llamada; las páginas solo usan funciones con nombre claro.
4. **AuthContext** guarda la sesión global; `useAuth()` la lee desde cualquier componente.
5. **React Router** (`App.jsx`) mapea URLs → páginas; **ProtectedRoute** y **menu.js** filtran por rol
   (comodidad visual; la seguridad real vive en el backend).

➡️ Siguiente: **[08-flujo-completo.md](08-flujo-completo.md)** — un caso real recorriendo TODAS las capas.

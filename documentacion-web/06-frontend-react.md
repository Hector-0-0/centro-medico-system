# 06 · El frontend con React

Ahora la parte que **ve y toca el usuario**: la página web. Está en la carpeta `frontend/` y usa **React**.

---

## 1. ¿Qué es React y qué problema resuelve?

**React** es una librería de **JavaScript** para construir interfaces de usuario (lo que se ve en el navegador).

El problema que resuelve: en una web, la pantalla **cambia constantemente** según los datos
(llegan citas nuevas, el usuario escribe en un buscador, se abre una ventana...). Hacer esto "a mano"
(buscar elementos del HTML y modificarlos uno por uno) es tedioso y propenso a errores.

La idea central de React:

> **Tú describes cómo se ve la pantalla para un estado dado. Cuando el estado cambia, React vuelve a dibujar
> solo lo que cambió.** Tú no tocas el HTML directamente; cambias los datos y React actualiza la vista.

Analogía: en vez de mover cada pieza del tablero tú mismo, le dices a React "el marcador ahora es 3-1"
y él redibuja el marcador.

---

## 2. Componentes: la pieza fundamental

Un **componente** es una pieza reutilizable de interfaz, escrita como una **función de JavaScript** que
**devuelve lo que se debe mostrar**. Una página es un componente; un botón puede ser un componente;
el menú lateral es un componente.

```jsx
// El componente más simple posible
function Saludo() {
  return <h1>Hola, bienvenido al Centro Médico</h1>;
}
```

- El nombre va en **Mayúscula** (`Saludo`, `MisCitas`, `Login`).
- Devuelve algo que **parece HTML** pero está dentro de JavaScript: eso es **JSX** (siguiente sección).
- Se usan como si fueran etiquetas: `<Saludo />`.

En este proyecto, cada pantalla es un componente en `frontend/src/pages/` (ej. `MisCitas.jsx`, `Login.jsx`,
`Pacientes.jsx`) y las piezas reutilizables están en `frontend/src/components/` (ej. `Sidebar.jsx`, `Layout.jsx`).

---

## 3. JSX: HTML dentro de JavaScript

**JSX** es una sintaxis que te deja escribir algo muy parecido a HTML dentro del código JavaScript.
No es HTML de verdad: React lo convierte en instrucciones para dibujar.

```jsx
function Tarjeta() {
  const nombre = "Juan";
  const citas = 3;
  return (
    <div className="tarjeta">
      <h2>Paciente: {nombre}</h2>          {/* {} = "aquí va JavaScript" */}
      <p>Tienes {citas} citas pendientes</p>
    </div>
  );
}
```

Reglas de JSX que notarás:
- **`{ }`** sirve para meter **JavaScript** dentro del HTML: `{nombre}`, `{2 + 2}`, `{lista.length}`.
- Se usa **`className`** en lugar de `class` (porque `class` es palabra reservada en JavaScript).
- Todo lo que devuelve un componente debe tener **un solo elemento raíz** (por eso se envuelve en un `<div>`).
- Las etiquetas que no se cierran solas deben cerrarse: `<img ... />`, `<input ... />`.

### Listas en JSX (muy común aquí)
Para pintar una tabla de citas, se recorre el arreglo con `.map()`:

```jsx
{citas.map((c) => (
  <tr key={c.id}>
    <td>{c.especialidad}</td>
    <td>{c.nombreDoctor}</td>
    <td>{c.estado}</td>
  </tr>
))}
```

`.map()` transforma **cada cita** en una fila `<tr>`. El `key={c.id}` es un identificador único que React
necesita para saber qué fila es cuál cuando la lista cambia.

### Condicionales en JSX
Para mostrar algo solo a veces:

```jsx
{error && <p className="error">{error}</p>}     // muestra el <p> SOLO si hay error
{cargando ? <p>Cargando…</p> : <Tabla datos={datos} />}   // si/no
```

---

## 4. Props: pasar datos a un componente

Las **props** (propiedades) son los datos que un componente **recibe de quien lo usa**, como los argumentos
de una función. Van como "atributos" en la etiqueta.

```jsx
// Definición: recibe props (aquí, "nombre" y "edad")
function Paciente({ nombre, edad }) {
  return <p>{nombre}, {edad} años</p>;
}

// Uso: le paso las props
<Paciente nombre="Juan" edad={21} />
```

Las props van **de padre a hijo** y el hijo **no las modifica**: son de solo lectura.
En este proyecto, por ejemplo, el componente `FilaTablaEstado` recibe props como `cargando`, `error`, `vacio`
para mostrar el estado correcto de una tabla.

---

## 5. Estado (`useState`): datos que cambian y redibujan

Las **props** vienen de fuera; el **estado** son datos **propios** de un componente que **cambian con el tiempo**
(lo que el usuario escribe, qué pestaña está activa, si una ventana está abierta...).

Para tener estado se usa el "hook" **`useState`**:

```jsx
import { useState } from 'react';

function Buscador() {
  const [texto, setTexto] = useState('');   // texto = valor actual; setTexto = función para cambiarlo
  //         └ "" es el valor inicial

  return (
    <input
      value={texto}
      onChange={(e) => setTexto(e.target.value)}   // al escribir, actualiza el estado
    />
  );
}
```

La clave: **cuando llamas a `setTexto(...)`, React vuelve a ejecutar el componente y redibuja la pantalla**
con el nuevo valor. Nunca modificas la variable directamente (`texto = ...` no funcionaría); siempre usas la
función `setTexto`.

Ejemplo real, de `Login.jsx`:

```jsx
const [form, setForm] = useState({ username: '', password: '' });
const [error, setError] = useState('');
const [cargando, setCargando] = useState(false);
```

Aquí el login guarda en estado lo que el usuario escribe (`form`), un posible mensaje de `error`,
y si está `cargando` (para deshabilitar el botón mientras se procesa).

---

## 6. Hooks: funciones especiales de React

Un **hook** es una función especial de React cuyo nombre empieza por **`use`**. Te dan "superpoderes"
dentro de un componente. Los que verás:

| Hook | Para qué sirve |
|------|----------------|
| `useState` | Tener datos que cambian (estado). |
| `useEffect` | Ejecutar algo "como efecto" (ej. **cargar datos al abrir la página**). |
| `useContext` | Leer datos globales compartidos (ej. la sesión actual). Ver archivo 07. |
| `useNavigate` | Cambiar de página por código (de `react-router`). |

> Regla de los hooks: solo se llaman **arriba** del componente, nunca dentro de un `if` o un bucle.

### `useEffect`: "haz esto cuando..."
`useEffect` ejecuta código en momentos clave, típicamente **al montar el componente** (cuando aparece en pantalla).
El caso más común aquí es **cargar datos del backend al abrir una página**.

Este proyecto envuelve ese patrón en un hook propio, `useCargar` (`frontend/src/hooks/useCargar.js`),
para no repetirlo en cada página:

```jsx
// hooks/useCargar.js — versión simplificada de la idea
export function useCargar(loader) {
  const [datos, setDatos] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setCargando(true);
    loader()                                   // llama al backend
      .then((r) => setDatos(r))                // guarda los datos
      .catch((err) => setError(mensajeError(err)))  // o el error
      .finally(() => setCargando(false));
  }, []);                                      // [] = ejecutar una sola vez, al montar

  return { datos, cargando, error, recargar };
}
```

Gracias a esto, una página solo escribe **una línea** para tener datos + estado de carga + manejo de error:

```jsx
const { datos, cargando, error, recargar } = useCargar(listarCitasEstudiante);
```

> Esto resuelve dos problemas comunes: el parpadeo de "Sin datos" antes de la primera respuesta
> (por eso existe `cargando`) y los fallos de red silenciosos (por eso existe `error`).

---

## 7. Una página completa, leída con calma

Veamos trozos reales de `pages/MisCitas.jsx` (la pantalla "Mis Citas" del estudiante) aplicando todo lo anterior:

```jsx
export default function MisCitas() {
  // 1) Carga las citas del backend (datos + cargando + error, gracias al hook)
  const { datos, cargando, error, recargar } = useCargar(listarCitasEstudiante);
  const lista = datos || [];

  // 2) Estado local: pestaña activa y texto de búsqueda
  const [tab, setTab] = useState('PENDIENTE');
  const [busqueda, setBusqueda] = useState('');

  // 3) Lógica de presentación: filtrar por pestaña y por búsqueda
  const filtradas = lista
    .filter((c) => c.estado === tab)
    .filter((c) => !busqueda || c.nombreDoctor.toLowerCase().includes(busqueda.toLowerCase()));

  // 4) Una acción: cancelar (llama al backend y recarga)
  const cancelar = async (cita) => {
    await cancelarCita(cita.id);   // función del service (archivo 07)
    recargar();                    // vuelve a pedir la lista actualizada
  };

  // 5) Lo que se dibuja
  return (
    <div>
      <h1 className="panel__title">Mis Citas</h1>
      {/* botones de pestañas, buscador... */}
      <table className="table">
        <tbody>
          {filtradas.map((c) => (
            <tr key={c.id}>
              <td>{c.especialidad}</td>
              <td>{c.nombreDoctor}</td>
              <td>{c.estado}</td>
              <td><button onClick={() => cancelar(c)}>Cancelar</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
```

Fíjate cómo todo lo aprendido aparece junto: **hook de carga**, **estado** (`tab`, `busqueda`),
**lógica** (filtrar), una **acción** que llama al backend y **JSX** que pinta una lista con `.map()`.
La página **no sabe SQL ni seguridad**: solo pide datos y los muestra.

---

## 8. Estructura del frontend

```
frontend/src/
├── index.js          ← el punto de arranque: monta <App/> en el HTML
├── App.jsx           ← el mapa de rutas (qué URL muestra qué página) — ver archivo 07
├── pages/            ← una pantalla por archivo (Login, MisCitas, Pacientes, Recetas...)
├── components/       ← piezas reutilizables:
│   ├── Layout.jsx        el marco (barra lateral + barra superior)
│   ├── Sidebar.jsx       el menú lateral
│   ├── menu.js           qué opciones de menú ve cada rol
│   ├── ProtectedRoute.jsx  protege rutas según sesión/rol (archivo 07)
│   ├── Dialog.jsx        ventanas de alerta/confirmación reutilizables
│   └── Layout.css        los estilos (colores guinda+crema de la UNI)
├── context/          ← AuthContext.jsx: la sesión global (archivo 07)
├── hooks/            ← useCargar.js: cargar datos con estado
├── services/         ← funciones que llaman al backend con axios (archivo 07)
├── constants/        ← catálogos fijos
└── utils/            ← utilidades (ej. formateo de fechas)
```

`frontend/src/index.js` es donde todo empieza: toma el componente `<App/>` y lo "monta" dentro del archivo
`public/index.html`. A partir de ahí, React maneja toda la página.

---

## 9. Dato técnico: estos archivos no se ejecutan tal cual

El navegador no entiende JSX ni los `import` modernos directamente. Una herramienta (**react-scripts**,
de "Create React App") **compila** todo `src/` en archivos `.js`, `.css` y `.html` normales:
- `npm start` → modo desarrollo, con recarga automática al guardar.
- `npm run build` → genera la versión optimizada para producción (en `frontend/build/`).

No necesitas tocar nada de esto; solo saber que existe un paso de "compilación" del frontend.

---

## ✅ Resumen

1. **React** redibuja la pantalla automáticamente cuando cambian los datos; tú describes la vista.
2. La UI se arma con **componentes** (funciones que devuelven **JSX**).
3. **Props** = datos que un componente recibe; **estado** (`useState`) = datos propios que cambian y redibujan.
4. Los **hooks** (`use...`) dan capacidades; `useEffect`/`useCargar` cargan datos del backend al abrir una página.
5. Cada **página** vive en `pages/`, pide datos y los muestra, sin saber de SQL ni seguridad.

➡️ Siguiente: **[07-frontend-conexion-api.md](07-frontend-conexion-api.md)** — cómo el frontend habla con el backend.

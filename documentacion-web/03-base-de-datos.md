# 03 · La base de datos (SQL Server)

Empezamos por la base de datos porque es **la base de todo**: las reglas del backend
y las pantallas del frontend giran alrededor de estos datos.

---

## 1. ¿Qué es una base de datos relacional?

Una **base de datos** guarda información de forma permanente y ordenada.
La que usa este proyecto es **relacional**: organiza los datos en **tablas**, como hojas de Excel.

- Una **tabla** tiene **columnas** (los campos: id, nombre, edad...) y **filas** (cada registro concreto).
- **SQL** (Structured Query Language) es el lenguaje para pedirle cosas a la base de datos:
  `SELECT` (leer), `INSERT` (crear), `UPDATE` (modificar), `DELETE` (borrar).

Ejemplo de tabla `estudiantes`:

| id_usuario | nombre | edad | carrera | email |
|-----------|--------|------|---------|-------|
| U001 | Juan Perez | 21 | Ing. de Sistemas | hola@uni.pe |
| U002 | Maria Lopez | 22 | Ing. Industrial | correo@uni.pe |

Este proyecto usa el motor **SQL Server** (de Microsoft). El "dialecto" de SQL de SQL Server se llama **T-SQL**
(por eso verás cosas como `IDENTITY`, `GETDATE()`, `BIT`, `GO`).

---

## 2. Cómo se crea la base de datos en este proyecto

Normalmente uno crearía las tablas a mano. Aquí está **automatizado**: hay un único archivo SQL que crea
y rellena todo, y el backend lo ejecuta solo al arrancar.

- 📄 El script está en **`backend/src/main/resources/init_db.sql`**.
- 🚀 Lo ejecuta la clase **`config/DataInitializer.java`** al iniciar el backend (lo veremos en el archivo 04/05).

El script es **idempotente**: se puede correr muchas veces sin romper nada, porque antes de crear cada cosa
comprueba si ya existe (`IF NOT EXISTS`, `IF OBJECT_ID(...) IS NULL`). Así web y escritorio comparten el mismo esquema.

```sql
-- Crea la base de datos solo si no existe
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'centro_medico')
BEGIN
    CREATE DATABASE centro_medico;
END;
GO
```

> 🔎 **`GO`** no es SQL estándar: es un separador de "lotes" propio de SQL Server. El `DataInitializer`
> parte el archivo por cada `GO` y ejecuta cada trozo por separado.

---

## 3. Las tablas del sistema

Aquí están todas las tablas y para qué sirven. Quédate con la **idea**, no memorices columnas.

### 👤 Cuentas y personas

| Tabla | Para qué |
|-------|----------|
| `usuarios` | La **cuenta de login**: `id` (ej. `U001`), `password` (texto plano), `rol`. Es la tabla central de seguridad. |
| `estudiantes` | Datos del paciente: nombre, edad, carrera, email, foto. Su `id_usuario` apunta a `usuarios`. |
| `doctores` | Datos del médico: nombre, especialidad, consultorio, activo, foto. |
| `administradores` | Nombre del admin. |
| `farmacia_usuarios` | Nombre del usuario de farmacia. |

> Nota importante: **un usuario y su "perfil" están en tablas separadas**. `usuarios` guarda solo
> login + rol; el nombre real está en `estudiantes`/`doctores`/etc. según el rol. Por eso, al iniciar sesión,
> el backend busca el nombre en la tabla correcta (lo verás en `AuthService`).

### 🗓️ Disponibilidad y citas

| Tabla | Para qué |
|-------|----------|
| `disponibilidad_doctor` | Rangos amplios que define el médico (ej. "Lunes 08:00–10:00"). |
| `slots_disponibilidad` | Esos rangos partidos en **bloques de 30 min** (ej. 08:00–08:30). Cada slot tiene `disponible` (1 = libre, 0 = ocupado). **Es lo que el estudiante reserva.** |
| `citas` | Una cita: qué estudiante, qué doctor, qué slot, motivo y `estado` (`PENDIENTE`, `ATENDIDA`, `CANCELADA`). |
| `atencion_cita` | Lo que el doctor registra al atender: diagnóstico libre, comentarios y fecha. 1 atención por cita. |

### 💊 Diagnósticos, recetas y medicinas

| Tabla | Para qué |
|-------|----------|
| `codigos_cie` | Catálogo de enfermedades estándar (CIE-10), ej. `E11 = Diabetes tipo 2`. Viene precargado. |
| `atencion_diagnostico` | Une una atención con uno o varios códigos CIE (1 atención → N diagnósticos). |
| `medicamentos` | Inventario: id, nombre, stock, tipo, dosis en mg. |
| `recetas` | La receta generada en una atención. Estado `PENDIENTE` o `ENTREGADA`. |
| `receta_detalle` | Cada medicamento de la receta, con su dosis y duración. |
| `movimientos_stock` | Auditoría de entradas/salidas de inventario (quién, cuándo, cuánto). |

---

## 4. Las relaciones (cómo se conectan las tablas)

Las tablas no están sueltas: se enlazan con **claves foráneas** (foreign keys). Una clave foránea es una columna
que "apunta" a la fila de otra tabla. Esto garantiza que los datos sean coherentes
(no puedes crear una cita para un doctor que no existe).

Cadena principal de este sistema:

```
usuarios (la cuenta + rol)
   │
   ├──< estudiantes        (un usuario ESTUDIANTE tiene un perfil)
   └──< doctores           (un usuario DOCTOR tiene un perfil)
            │
            └──< disponibilidad_doctor   (el doctor define rangos)
                      │
                      └──< slots_disponibilidad   (rangos partidos en 30 min)
                                │
                                └──< citas   (el estudiante reserva un slot)
                                       │
                                       └──< atencion_cita   (el doctor la atiende)
                                                 │
                                                 ├──< atencion_diagnostico ──> codigos_cie
                                                 └──< recetas ──< receta_detalle ──> medicamentos
```

Lee `──<` como **"uno a muchos"**: un doctor tiene muchos slots; una receta tiene muchos detalles, etc.

---

## 5. Detalles que te ayudarán a leer el SQL

- **`VARCHAR(10)`**: texto de hasta 10 caracteres. `VARCHAR(MAX)`: texto muy largo (ej. fotos en base64).
- **`INT IDENTITY(1,1)`**: número entero que **se autoincrementa** solo (1, 2, 3...). Se usa como `id` automático.
- **`BIT`**: un booleano de base de datos. `1 = verdadero`, `0 = falso`.
  - `eliminado BIT DEFAULT 0`: en vez de borrar filas de verdad, se marcan como `eliminado = 1`
    (**borrado lógico**). Las consultas filtran `WHERE eliminado = 0`. Así no se pierde el historial.
  - `disponible BIT` en los slots: `1` libre, `0` ocupado.
- **`CHECK (estado IN ('PENDIENTE','ATENDIDA','CANCELADA'))`**: una regla que impide guardar valores fuera de esa lista.
- **`PRIMARY KEY`**: la columna que identifica de forma única cada fila.
- **`FOREIGN KEY ... REFERENCES ...`**: la clave foránea de la que hablamos arriba.

---

## 6. Datos de prueba (semilla)

El final de `init_db.sql` inserta datos para poder probar el sistema de inmediato.
Estas son las **credenciales sembradas** (usuario / contraseña — en texto plano, ver archivo 05):

| Rol | Usuario | Contraseña |
|-----|---------|-----------|
| Administrador | `ADM001` | `adm123` |
| Doctor | `D001` | `pass123` |
| Estudiante | `U001` | `1234` |
| Farmacia | `FAR001` | `far123` |

(También hay `U002`–`U005` y `D002`–`D005`.)

También se siembran: 5 médicos con especialidades, sus rangos de disponibilidad partidos en slots de 30 min,
6 medicamentos, 2 citas de ejemplo, una atención con receta y el catálogo CIE-10.

---

## ✅ Resumen

1. Los datos viven en **tablas** de **SQL Server**; se consultan con **SQL**.
2. Todo se crea/rellena automáticamente desde **`init_db.sql`** (idempotente).
3. La cadena central es: **usuario → perfil → disponibilidad → slot → cita → atención → receta**.
4. Casi nada se borra de verdad: se usa **borrado lógico** (`eliminado = 1`).

➡️ Siguiente: **[04-backend-spring-boot.md](04-backend-spring-boot.md)** — cómo el backend lee y escribe en estas tablas.

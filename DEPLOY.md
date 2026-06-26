# Subir el proyecto a internet (VM + Docker + dominio + HTTPS)

Guía para dejar la app en línea con una URL pública tipo `https://tudominio.com`.
Todo el sistema (SQL Server + backend + frontend + n8n) corre en **un solo
servidor** con Docker Compose, y **Caddy** se encarga del HTTPS automático.

Pensado para una **demo de curso** usando **créditos de estudiante (gratis)**.

---

## Resumen del flujo

```
Navegador ──HTTPS(443)──> Caddy ──> frontend (nginx) ──/api──> backend ──> SQL Server
                                                         └─ n8n (correo)
```

---

## Paso 1 — Conseguir lo gratis con tu correo @uni.pe

1. **GitHub Student Developer Pack**: https://education.github.com/pack
   Regístrate con tu correo `@uni.pe`. Aprobación normalmente en minutos/horas.
2. Dentro del pack activas:
   - **DigitalOcean** → **$200 de crédito** (para el servidor).
   - **Namecheap** (dominio `.me` gratis 1 año) o **.tech** (dominio gratis) → tu dominio.

> Alternativa: **Azure for Students** ($100, sin tarjeta). Los pasos de servidor
> cambian un poco; DigitalOcean es lo más simple para esta guía.

## Paso 2 — Crear el servidor (Droplet en DigitalOcean)

1. DigitalOcean → **Create → Droplets**.
2. Imagen: **Ubuntu 24.04 LTS**.
3. Plan: **Basic → Regular → 4 GB RAM / 2 vCPU**.
   ⚠️ No bajes de 4 GB: SQL Server solo ya consume ~2 GB.
4. Autenticación: agrega tu **clave SSH** (recomendado) o una contraseña.
5. Crea el Droplet y **copia su IP pública** (ej. `143.55.21.8`).

## Paso 3 — Apuntar el dominio a esa IP

En el panel DNS de tu dominio (Namecheap, etc.) crea un registro:

| Tipo | Host | Valor |
|------|------|-------|
| A | `@` | IP del Droplet |
| A | `www` | IP del Droplet |

Espera a que propague (unos minutos). Comprueba con: `ping tudominio.com`
(debe responder con la IP del Droplet). **Esto debe estar listo ANTES del paso 6**,
o Caddy no podrá emitir el certificado HTTPS.

## Paso 4 — Entrar al servidor e instalar Docker

```bash
ssh root@IP_DEL_DROPLET

curl -fsSL https://get.docker.com | sh
```

## Paso 5 — Abrir el firewall

```bash
ufw allow OpenSSH
ufw allow 80/tcp     # HTTP (Caddy lo usa para el certificado y redirige a HTTPS)
ufw allow 443/tcp    # HTTPS
ufw allow 5678/tcp   # opcional: editor de n8n
ufw --force enable
```

## Paso 6 — Clonar, configurar el dominio y levantar

```bash
git clone https://github.com/Hector-0-0/centro--medico-system.git
cd centro--medico-system

# Define tu dominio (Caddy lo usa para el certificado)
cp .env.example .env
nano .env          # pon DOMAIN=tudominio.com  (guarda con Ctrl+O, Enter, Ctrl+X)

# Levantar todo
docker compose up -d --build
docker compose logs -f caddy     # mira cómo obtiene el certificado; Ctrl+C para salir
```

El primer arranque tarda (compila backend + frontend, siembra la BD y pide el
certificado). Cuando termine:

➡️ **Abre `https://tudominio.com`** 🎉

Credenciales de prueba: `ADM001/adm123`, `D001/pass123`, `U001/1234`, `FAR001/far123`.

## Paso 7 — (Opcional) Activar el correo de la receta

1. Entra a `http://IP_DEL_DROPLET:5678` (usuario/clave del `docker-compose.yml`).
2. Importa `n8n/receta-medica-workflow.json`.
3. En el nodo **Enviar Gmail**, conecta tus credenciales de Gmail.
4. **Activa** el workflow.

Sin esto la app funciona igual; solo no se envía el correo.

---

## Después de la demo — no gastes el crédito

El crédito se consume mientras el Droplet exista. Al terminar:

- **Apagar** (sigue cobrando almacenamiento, poco): `Droplet → Power Off`.
- **Borrar del todo** (deja de cobrar): `Droplet → Destroy`.

## Antes de un uso "real" (no demo)

Cambia los secretos en `docker-compose.yml`: `MSSQL_SA_PASSWORD` +
`SPRING_DATASOURCE_PASSWORD` (iguales), `APP_JWT_SECRET` y
`N8N_BASIC_AUTH_PASSWORD`.

## Comandos útiles

```bash
docker compose ps                 # estado
docker compose logs -f backend    # logs del backend
docker compose logs -f caddy      # logs del HTTPS
docker compose down               # detener (conserva datos)
docker compose up -d --build      # re-desplegar tras cambios
```

## Si algo falla

- **El navegador no abre / "no seguro"**: revisa `docker compose logs caddy`.
  Casi siempre es que el dominio aún no apuntaba a la IP cuando Caddy pidió el
  certificado. Corrige el DNS y reinicia: `docker compose restart caddy`.
- **La app carga pero el login falla**: `docker compose logs backend`. Espera a
  que SQL Server esté "healthy" (`docker compose ps`); el backend reintenta solo.
- **Se queda sin memoria**: confirma que el Droplet es de 4 GB.

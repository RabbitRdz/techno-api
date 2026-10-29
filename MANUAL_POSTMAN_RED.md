# Mini manual de red y pruebas en Postman

## 1. Objetivo

Este manual explica cómo permitir que el profesor acceda a TechShop desde otro equipo conectado al mismo router y cómo probar sus controladores desde Postman.

La API escucha en el puerto `8080`. En una red local, el nombre del dominio debe resolverse a la IP local del equipo que ejecuta la aplicación. No se reemplaza la IP por el dominio en el código Java.

## 2. Preparar el equipo que ejecuta la API

1. Conecta el equipo servidor y el equipo donde se usará Postman al mismo router o red Wi-Fi.
2. En Windows, abre PowerShell y ejecuta:

   ```powershell
   ipconfig
   ```

   Identifica la dirección IPv4 del adaptador conectado a ese router (por ejemplo, `192.168.1.50`). No uses `127.0.0.1`: esa dirección solo funciona en el propio equipo.
3. En la configuración del router, crea una **reserva DHCP** para que el equipo servidor conserve esa IP. Los menús y nombres cambian según el modelo del router.
4. Inicia PostgreSQL y asegúrate de que la base de datos configurada para la aplicación está disponible.
5. Desde la carpeta del proyecto, inicia la API:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   La configuración predeterminada usa el puerto `8080`. Spring Boot escucha en las interfaces de red disponibles salvo que se configure `server.address` de forma explícita.
6. Si Windows Firewall pregunta, permite el acceso para redes **Privadas**. Si no aparece el aviso, una regla de entrada limitada al puerto de la API se puede crear en PowerShell como administrador:

   ```powershell
   New-NetFirewallRule -DisplayName "TechShop API 8080" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow -Profile Private
   ```

7. Desde el propio equipo servidor, verifica `http://localhost:8080/api/productos`. Desde el equipo de Postman, prueba primero `http://192.168.1.50:8080/api/productos`, sustituyendo la IP de ejemplo por la IP reservada real.

## 3. Usar un nombre en lugar de la IP

Un nombre de dominio solo funciona si un servicio DNS lo traduce a la IP del servidor.

### Opción A: DNS local del router

Si el router permite definir nombres DNS locales, agrega una entrada como `techshop.lan` que apunte a la IP reservada del servidor, por ejemplo `192.168.1.50`. Conecta ambos equipos a esa red y prueba:

```text
http://techshop.lan:8080/api/productos
```

### Opción B: archivo `hosts` del equipo de Postman

Si el router no ofrece DNS local, en **cada equipo cliente** agrega una línea al archivo `C:\Windows\System32\drivers\etc\hosts` (abre el editor como administrador):

```text
192.168.1.50 techshop.lan
```

Reemplaza la IP de ejemplo por la IP reservada real. Luego usa `http://techshop.lan:8080` como dirección base en Postman. El archivo `hosts` solo cambia la resolución en ese equipo; no configura un dominio público ni el router.

### Acceso desde fuera de la red

Un nombre local como `techshop.lan` funciona únicamente dentro de la red configurada. Para usar un dominio público desde Internet se necesita un dominio y DNS público, una IP pública alcanzable, reglas de router/firewall y HTTPS con certificado. No abras PostgreSQL (`5432`) a Internet. Para una demostración en el router del salón, usa la red local y no configures redirección de puertos WAN.

## 4. Configuración de Postman

1. Crea un Environment, por ejemplo `TechShop local`.
2. Define la variable `baseUrl` con el valor que corresponda:

   ```text
   http://192.168.1.50:8080
   ```

   o, si el nombre ya resuelve en el equipo:

   ```text
   http://techshop.lan:8080
   ```
3. En cada solicitud usa `{{baseUrl}}` seguido de la ruta indicada abajo.
4. Para solicitudes con cuerpo, selecciona **Body → raw → JSON**. Postman enviará `Content-Type: application/json`.
5. Los IDs se generan en la base de datos. Después de crear un recurso, copia el `id` devuelto y úsalo en las solicitudes que lo requieren.

## 5. Controladores y rutas

### Categorías — `/api/categorias`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/categorias` | Listar categorías |
| GET | `/api/categorias/{id}` | Consultar una categoría |
| POST | `/api/categorias` | Crear categoría |
| PUT | `/api/categorias/{id}` | Reemplazar sus campos editables |
| DELETE | `/api/categorias/{id}` | Eliminar categoría |

Ejemplo POST/PUT:

```json
{
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

### Marcas — `/api/marcas`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/marcas` | Listar marcas |
| GET | `/api/marcas/{id}` | Consultar una marca |
| POST | `/api/marcas` | Crear marca |
| PUT | `/api/marcas/{id}` | Reemplazar sus campos editables |
| DELETE | `/api/marcas/{id}` | Eliminar marca |

Ejemplo POST/PUT:

```json
{
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

### Roles — `/api/roles`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/roles` | Listar roles |
| GET | `/api/roles/{id}` | Consultar un rol |
| POST | `/api/roles` | Crear rol |
| PUT | `/api/roles/{id}` | Reemplazar sus campos editables |
| DELETE | `/api/roles/{id}` | Eliminar rol |

Ejemplo POST/PUT:

```json
{
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

### Personas — `/api/personas`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/personas` | Listar personas |
| GET | `/api/personas/{id}` | Consultar una persona |
| POST | `/api/personas` | Crear persona |
| PUT | `/api/personas/{id}` | Actualizar nombre, apellido y fecha de nacimiento |
| DELETE | `/api/personas/{id}` | Eliminar persona |

Ejemplo POST/PUT:

```json
{
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15"
}
```

La fecha debe usar el formato `AAAA-MM-DD`. Al crear una persona, sus preferencias de privacidad comienzan habilitadas por defecto.

### Productos — `/api/productos`

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Consultar un producto |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Reemplazar sus campos editables |
| DELETE | `/api/productos/{id}` | Eliminar producto |

Ejemplo POST/PUT:

```json
{
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

Usa IDs de categoría y marca existentes.

### Derechos ARCO — `/api/arco`

Estos endpoints operan sobre una persona existente. La consulta y cada operación registran solicitudes en el historial.

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/arco/acceso/{idPersona}` | Consultar datos, preferencias e historial |
| PUT | `/api/arco/rectificacion/{idPersona}` | Rectificar nombre, apellido y fecha de nacimiento |
| DELETE | `/api/arco/cancelacion/{idPersona}` | Cancelar y anonimizar los datos personales |
| PATCH | `/api/arco/oposicion/{idPersona}` | Cambiar una o ambas preferencias |
| GET | `/api/arco/solicitudes/{idPersona}` | Consultar historial de solicitudes ARCO |

Ejemplo PUT de rectificación:

```json
{
  "nombre": "Ana María",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15"
}
```

Ejemplo PATCH de oposición (se puede enviar uno o ambos campos):

```json
{
  "permiteMarketing": false,
  "permiteTratamientoDatos": false
}
```

**Precaución:** `DELETE /api/arco/cancelacion/{idPersona}` anonimiza y desactiva la persona; no es una baja reversible desde este endpoint. Para pruebas, usa un registro de prueba.

## 6. Secuencia rápida de prueba

1. `POST {{baseUrl}}/api/categorias` con el JSON de categoría; guarda el `id` de la respuesta.
2. `POST {{baseUrl}}/api/marcas` con el JSON de marca; guarda el `id`.
3. `POST {{baseUrl}}/api/productos` usando ambos IDs.
4. Prueba `GET {{baseUrl}}/api/productos` y luego `GET {{baseUrl}}/api/productos/{id}`.
5. Crea una persona y prueba `GET {{baseUrl}}/api/arco/acceso/{idPersona}`, `PATCH {{baseUrl}}/api/arco/oposicion/{idPersona}` y `GET {{baseUrl}}/api/arco/solicitudes/{idPersona}`.
6. Prueba `PUT` y `DELETE` solo con registros de prueba. En particular, el DELETE de cancelación ARCO anonimiza el registro.

## 7. Respuestas esperadas

| Caso | Código HTTP esperado |
|---|---:|
| Consulta correcta, actualización o endpoint ARCO correcto | `200 OK` |
| Creación correcta | `201 Created` |
| Eliminación CRUD correcta | `204 No Content` |
| ID inexistente en consulta/actualización/eliminación o ARCO | `404 Not Found` |
| Rectificación ARCO sin nombre o oposición sin preferencias | `400 Bad Request` |

Una lista sin registros se devuelve como `200 OK` con `[]`. Si Postman no puede conectar, verifica que la aplicación siga ejecutándose, que ambos equipos estén en la misma red, que la IP sea la actual/reservada y que el firewall permita TCP `8080`.

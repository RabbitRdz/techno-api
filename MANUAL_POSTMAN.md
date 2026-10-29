# Manual de red y controladores para Postman

Guía para ejecutar TechShop en una red local y probar los seis controladores desde Postman.

## 1. Preparar el equipo servidor

1. Conecta el equipo donde se ejecuta TechShop y el equipo del profesor al mismo router.
2. En el equipo servidor, abre PowerShell o Símbolo del sistema y ejecuta:

   ```powershell
   ipconfig
   ```

   Anota la **Dirección IPv4** del adaptador conectado al router (por ejemplo, `192.168.1.50`). Conviene reservar esta IP para el equipo en la configuración DHCP del router, para que no cambie.
3. Inicia PostgreSQL y la aplicación. Desde la raíz del proyecto:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   Spring Boot escucha en `0.0.0.0:8080`, por lo que acepta conexiones en las interfaces de red del equipo. La base PostgreSQL sigue ejecutándose en el mismo equipo (`localhost:5432`).

## 2. Resolver el nombre de dominio local

Usa un nombre de red local, por ejemplo `api.techshop.local`. El nombre debe resolver a la IPv4 del equipo servidor. La dirección que se utiliza en Postman será `http://api.techshop.local:8080`. El puerto `8080` se conserva en la URL; no se incluye en el archivo `hosts` ni en el DNS.

### Opción A: archivo `hosts` en Windows

En **cada equipo** que vaya a usar ese nombre (servidor y equipo del profesor), abre el Bloc de notas como administrador y abre:

```text
C:\Windows\System32\drivers\etc\hosts
```

Agrega una línea al final y sustituye la IP de ejemplo por la IPv4 real del servidor:

```text
192.168.1.50 api.techshop.local
```

Guarda el archivo y prueba en Postman:

```text
GET http://api.techshop.local:8080/api/productos
```

El archivo `hosts` solo configura el equipo donde se edita. Para evitar problemas con resolución multicast, si `.local` no resuelve correctamente en los equipos de la evaluación, utiliza el nombre de prueba `api.techshop.test` en ambos archivos `hosts` y en Postman.

Para permitir conexiones en el Firewall de Windows, abre PowerShell **como administrador** en el equipo servidor y crea una regla para TCP 8080 en redes privadas:

```powershell
New-NetFirewallRule -DisplayName "TechShop API 8080" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow -Profile Private
```

No abras PostgreSQL (`5432`) al resto de la red ni a Internet. Para retirar la regla al terminar la evaluación:

```powershell
Remove-NetFirewallRule -DisplayName "TechShop API 8080"
```

### Opción B: DNS local del router

En la administración del router busca un apartado con nombre similar a **DNS local**, **DNS estático**, **LAN DNS**, **Host Mapping**, **Static DHCP/DHCP Reservation** o **Local domain**. Los nombres dependen del fabricante. Reserva primero la IPv4 del equipo servidor y registra el nombre `api.techshop.local` asociado a esa IPv4. Después, conecta ambos equipos a esa red; si el router distribuye su DNS por DHCP, el profesor podrá usar el nombre sin modificar su archivo `hosts`.

Los nombres locales solo son válidos dentro de la red configurada. No uses `techshop.com` salvo que controles ese dominio y su DNS. Para esta evaluación no configures redirección de puertos desde Internet.

## 3. Preparar Postman

- Selecciona **Body → raw → JSON** para cada `POST`, `PUT` y `PATCH`. Usa `Content-Type: application/json`.
- Reemplaza `{id}` y `{idPersona}` en las rutas con los IDs numéricos devueltos por las creaciones o consultas.
- En las respuestas de creación (`201 Created`), guarda el `id` devuelto para las siguientes pruebas.

URL base de los ejemplos:

```text
http://api.techshop.local:8080
```

## 4. Orden sugerido para probar

1. Crea una categoría y una marca, pues un producto requiere IDs existentes de ambas tablas por llaves foráneas.
2. Crea productos con los IDs devueltos. Los endpoints de `Rol` son independientes en estos controladores: no hay un endpoint de usuario/rol ni una relación con `Persona` expuesta aquí.
3. Crea una persona y guarda su ID.
4. Prueba los endpoints ARCO con el ID de esa persona. Cada operación ARCO escribe su solicitud automáticamente en `solicitudes_arco`; no existe un endpoint para crear una `SolicitudArco` directamente.
5. Prueba actualizaciones y eliminaciones con datos de prueba. En particular, la cancelación ARCO anonimiza la persona.

## 5. Controladores y endpoints

### Categorías — `CategoriaController` — `/api/categorias`

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/categorias` | Lista las categorías. Sin parámetros de ruta. | `200 OK` (lista vacía: `[]`) |
| `GET http://api.techshop.local:8080/api/categorias/{id}` | Consulta por ID. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `POST http://api.techshop.local:8080/api/categorias` | Crea una categoría; hacerlo antes de los productos. Sin parámetros de ruta. | `201 Created` con el recurso |
| `PUT http://api.techshop.local:8080/api/categorias/{id}` | Actualiza nombre y descripción. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `DELETE http://api.techshop.local:8080/api/categorias/{id}` | Elimina por ID. `{id}` es el ID numérico. | `204 No Content`; `404 Not Found` si no existe; puede fallar si un producto aún la referencia |

Body para `POST` y `PUT`:

```json
{
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

### Marcas — `MarcaController` — `/api/marcas`

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/marcas` | Lista las marcas. Sin parámetros de ruta. | `200 OK` (lista vacía: `[]`) |
| `GET http://api.techshop.local:8080/api/marcas/{id}` | Consulta por ID. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `POST http://api.techshop.local:8080/api/marcas` | Crea una marca; hacerlo antes de los productos. Sin parámetros de ruta. | `201 Created` con el recurso |
| `PUT http://api.techshop.local:8080/api/marcas/{id}` | Actualiza nombre y país de origen. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `DELETE http://api.techshop.local:8080/api/marcas/{id}` | Elimina por ID. `{id}` es el ID numérico. | `204 No Content`; `404 Not Found` si no existe; puede fallar si un producto aún la referencia |

Body para `POST` y `PUT`:

```json
{
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

### Roles — `RolController` — `/api/roles`

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/roles` | Lista los roles. Sin parámetros de ruta. | `200 OK` (lista vacía: `[]`) |
| `GET http://api.techshop.local:8080/api/roles/{id}` | Consulta por ID. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `POST http://api.techshop.local:8080/api/roles` | Crea un rol. Sin parámetros de ruta. | `201 Created` con el recurso |
| `PUT http://api.techshop.local:8080/api/roles/{id}` | Actualiza nombre y descripción. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `DELETE http://api.techshop.local:8080/api/roles/{id}` | Elimina por ID. `{id}` es el ID numérico. | `204 No Content`; `404 Not Found` si no existe |

Body para `POST` y `PUT`:

```json
{
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

### Personas — `PersonaController` — `/api/personas`

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/personas` | Lista personas. Sin parámetros de ruta. | `200 OK` (lista vacía: `[]`) |
| `GET http://api.techshop.local:8080/api/personas/{id}` | Consulta por ID. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `POST http://api.techshop.local:8080/api/personas` | Crea una persona; guarda el ID para usarlo en ARCO. Sin parámetros de ruta. | `201 Created` con el recurso |
| `PUT http://api.techshop.local:8080/api/personas/{id}` | Actualiza nombre, apellido y fecha de nacimiento. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `DELETE http://api.techshop.local:8080/api/personas/{id}` | Elimina por ID. `{id}` es el ID numérico. | `204 No Content`; `404 Not Found` si no existe; puede fallar si tiene solicitudes ARCO asociadas |

Body para `POST` y `PUT`:

```json
{
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15"
}
```

`fechaNacimiento` es opcional y utiliza `AAAA-MM-DD`. Al crear, `activo`, `permiteMarketing` y `permiteTratamientoDatos` inician en `true` por defecto.

### Productos — `ProductoController` — `/api/productos`

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/productos` | Lista los productos. Sin parámetros de ruta. | `200 OK` (lista vacía: `[]`) |
| `GET http://api.techshop.local:8080/api/productos/{id}` | Consulta por ID. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si no existe |
| `POST http://api.techshop.local:8080/api/productos` | Crea producto. Requiere `idCategoria` e `idMarca` existentes. | `201 Created` con el recurso |
| `PUT http://api.techshop.local:8080/api/productos/{id}` | Actualiza los campos del producto. `{id}` es el ID numérico. | `200 OK`; `404 Not Found` si el producto no existe |
| `DELETE http://api.techshop.local:8080/api/productos/{id}` | Elimina por ID. `{id}` es el ID numérico. | `204 No Content`; `404 Not Found` si no existe |

Body para `POST` y `PUT` (sustituye los IDs por los creados en Categorías y Marcas):

```json
{
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

### Derechos ARCO — `ArcoController` — `/api/arco`

Las cinco rutas requieren que `{idPersona}` sea el ID de una persona existente. El controlador crea automáticamente una fila de `SolicitudArco` en las operaciones de acceso, rectificación, cancelación y oposición. El modelo `SolicitudArco` contiene `id`, `persona`, `tipoDerecho`, `motivo`, `fechaSolicitud` y `estado`, pero esos valores se generan desde el controlador; no se envía un body directo de solicitud ARCO.

| Método y URL completa | Descripción y ruta | Respuesta esperada |
|---|---|---|
| `GET http://api.techshop.local:8080/api/arco/acceso/{idPersona}` | Devuelve persona, preferencias e historial; también registra una solicitud de acceso. `{idPersona}` es el ID de persona. | `200 OK`; `404 Not Found` si no existe |
| `PUT http://api.techshop.local:8080/api/arco/rectificacion/{idPersona}` | Rectifica nombre, apellido y fecha de nacimiento. `{idPersona}` es el ID de persona. | `200 OK`; `400 Bad Request` si falta el nombre; `404 Not Found` si no existe |
| `DELETE http://api.techshop.local:8080/api/arco/cancelacion/{idPersona}` | Desactiva y anonimiza la persona; guarda la fecha de baja y registra la solicitud. `{idPersona}` es el ID de persona. | `200 OK` con la persona anonimizada; `404 Not Found` si no existe |
| `PATCH http://api.techshop.local:8080/api/arco/oposicion/{idPersona}` | Cambia una o ambas preferencias. `{idPersona}` es el ID de persona. | `200 OK`; `400 Bad Request` si no se envía ninguna preferencia; `404 Not Found` si no existe |
| `GET http://api.techshop.local:8080/api/arco/solicitudes/{idPersona}` | Lista las solicitudes registradas. `{idPersona}` es el ID de persona. | `200 OK` (puede ser `[]`); `404 Not Found` si no existe |

Body para `PUT /api/arco/rectificacion/{idPersona}`:

```json
{
  "nombre": "Ana María",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15"
}
```

Body para `PATCH /api/arco/oposicion/{idPersona}` (envía al menos uno de los campos):

```json
{
  "permiteMarketing": false,
  "permiteTratamientoDatos": false
}
```

**Importante:** la cancelación ARCO es distinta del DELETE de `/api/personas/{id}`. La cancelación no borra físicamente la fila: anonimiza y desactiva los datos. Haz esta prueba únicamente con una persona de prueba; no hay una operación ARCO para revertirla.

## 6. CORS y Postman

Postman envía solicitudes HTTP directamente y no aplica la política CORS que los navegadores imponen a JavaScript. Por tanto, probar desde Postman no requiere `@CrossOrigin(origins = "*")` y esta API no habilita CORS global. Si más adelante se consume desde una aplicación web en otro origen, configura CORS para los orígenes concretos de esa aplicación en vez de permitir indiscriminadamente cualquier origen.

Si Postman informa que no puede conectarse, confirma que la aplicación sigue activa, que ambos equipos están en la misma red, que el nombre apunta a la IPv4 actual del servidor y que el Firewall permite TCP `8080` en el perfil de red privada.

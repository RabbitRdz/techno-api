# Manual de red local y pruebas en Postman

Guía para ejecutar TechShop en la computadora anfitriona y probar los seis controladores desde otra computadora conectada a la misma red.

## 1. Configuración de Spring Boot

En `src/main/resources/application.properties` deben estar estas propiedades:

```properties
server.address=0.0.0.0
server.port=8080
```

`server.address=0.0.0.0` hace que Spring Boot escuche en las interfaces de red del equipo, no solamente en `localhost`. La aplicación ya tiene esta configuración. PostgreSQL debe seguir disponible en el equipo anfitrión; no es necesario ni recomendable exponer su puerto `5432` a la LAN.

Postman no está sujeto a CORS: esa restricción la aplican los navegadores a las peticiones hechas desde JavaScript. Por tanto, para estas pruebas no agregues `@CrossOrigin` ni habilites CORS global. Si después se crea una aplicación web, configura CORS para el origen concreto de esa aplicación.

## 2. Hacer que el nombre resuelva en la LAN

El nombre debe apuntar a la IPv4 de la computadora que ejecuta TechShop. La configuración necesaria se hace en el equipo anfitrión y/o en el router; el profesor no tiene que editar su archivo `hosts`.

### Opción recomendada: reserva DHCP y DNS local del router

1. Conecta ambos equipos a la misma LAN y comprueba que el router no tenga activado el aislamiento entre clientes Wi-Fi.
2. En la computadora anfitriona, ejecuta `ipconfig` y anota la IPv4 del adaptador conectado a esa LAN (por ejemplo, `192.168.1.50`).
3. En la configuración del router, crea una **reserva DHCP** para esa computadora, para que su dirección IPv4 no cambie.
4. Si el router ofrece **DNS local/estático**, **LAN DNS** o **Host Mapping**, crea una entrada que apunte el nombre elegido a esa IPv4. Usa `techshop.local` si el router y los clientes resuelven ese nombre correctamente; si no, usa el nombre local que admita el router, por ejemplo `techshop` o su dominio LAN.
5. Configura el router para anunciar su servicio DNS por DHCP (normalmente ya es así). El equipo del profesor deberá estar conectado a esa misma LAN y obtener la configuración de red por DHCP.

`.local` suele reservarse para mDNS/multicast y no todos los routers lo publican como DNS convencional. Si se usa mDNS, la computadora anfitriona puede anunciar el nombre de equipo `techshop` como `techshop.local`, pero tanto el sistema operativo como la red deben soportar mDNS y permitir multicast. Comprueba la resolución desde el equipo del profesor antes de la evaluación; no asumas que cambiar únicamente el nombre de Windows configura el DNS del router.

El nombre local solo debe usarse dentro de esa red. No configures reenvío de puertos desde Internet.

### Firewall de Windows

En la computadora anfitriona, abre PowerShell **como administrador** y permite el puerto de la API únicamente en redes privadas:

```powershell
New-NetFirewallRule -DisplayName "TechShop API 8080" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow -Profile Private
```

Al terminar la evaluación puedes retirar la regla:

```powershell
Remove-NetFirewallRule -DisplayName "TechShop API 8080"
```

No abras el puerto `5432` de PostgreSQL al resto de la red. Inicia PostgreSQL y la aplicación en el anfitrión desde la raíz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

## 3. Preparar Postman

- URL base de los ejemplos: `http://techshop.local:8080`.
- En cada `POST`, `PUT` o `PATCH`, selecciona **Body → raw → JSON** y usa el encabezado `Content-Type: application/json`.
- Para solicitudes sin cuerpo (`GET` y `DELETE`) no se necesita `Content-Type`; se puede enviar `Accept: application/json`.
- Sustituye `{id}` y `{idPersona}` por los IDs reales que devuelva la API. Los IDs de ejemplo no necesariamente serán `1`.
- Los ejemplos muestran respuestas satisfactorias. Un ID inexistente produce `404 Not Found`; la forma del JSON de error depende de la configuración de Spring Boot.

## 4. Orden de ejecución recomendado

1. Crea un rol, una categoría y una marca. Los endpoints de rol no tienen relación con persona en estos controladores; la categoría y la marca sí son prerrequisitos de un producto.
2. Crea un producto usando los IDs reales de categoría y marca.
3. Crea **dos personas**: una para probar el CRUD y eliminarla antes de crear solicitudes ARCO; otra para conservarla durante todas las pruebas ARCO. Las solicitudes ARCO tienen una llave foránea a persona, por lo que eliminar físicamente una persona que ya tenga solicitudes puede fallar.
4. Prueba los endpoints ARCO sobre la segunda persona. Acceso, rectificación, cancelación y oposición registran automáticamente una fila en `solicitudes_arco`. La cancelación anonimiza esa persona y no se puede revertir mediante estos endpoints.
5. Al final elimina primero el producto y después, si ya no están referenciadas, la categoría y la marca. El borrado de un rol también podría estar bloqueado si alguna fila de `usuarios_roles` lo referencia.

## 5. Endpoints CRUD

En los ejemplos `id: 1` representa el ID devuelto al crear el registro; usa siempre el ID real de tu respuesta.

### Roles — `/api/roles`

#### Listar roles

- **Método y URL:** `GET http://techshop.local:8080/api/roles`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista JSON; si no hay registros, `[]`.

```json
[
  {
    "id": 1,
    "nombreRol": "cliente",
    "descripcion": "Usuario cliente"
  }
]
```

#### Consultar un rol

- **Método y URL:** `GET http://techshop.local:8080/api/roles/1`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

#### Crear un rol

- **Método y URL:** `POST http://techshop.local:8080/api/roles`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

- **Respuesta esperada:** `201 Created`, el rol guardado con su ID generado.

```json
{
  "id": 1,
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

#### Actualizar un rol

- **Método y URL:** `PUT http://techshop.local:8080/api/roles/1`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombreRol": "cliente_actualizado",
  "descripcion": "Rol de cliente actualizado"
}
```

- **Respuesta esperada:** `200 OK`, el rol actualizado; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombreRol": "cliente_actualizado",
  "descripcion": "Rol de cliente actualizado"
}
```

#### Eliminar un rol

- **Método y URL:** `DELETE http://techshop.local:8080/api/roles/1`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `204 No Content`, sin cuerpo; `404 Not Found` si no existe. Puede fallar por una referencia existente en `usuarios_roles`.

### Categorías — `/api/categorias`

#### Listar categorías

- **Método y URL:** `GET http://techshop.local:8080/api/categorias`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista JSON; si no hay registros, `[]`.

```json
[
  {
    "id": 1,
    "nombre": "Periféricos",
    "descripcion": "Teclados, mouses y audífonos"
  }
]
```

#### Consultar una categoría

- **Método y URL:** `GET http://techshop.local:8080/api/categorias/1`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

#### Crear una categoría

- **Método y URL:** `POST http://techshop.local:8080/api/categorias`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

- **Respuesta esperada:** `201 Created`, la categoría guardada con su ID generado.

```json
{
  "id": 1,
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

#### Actualizar una categoría

- **Método y URL:** `PUT http://techshop.local:8080/api/categorias/1`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombre": "Accesorios para PC",
  "descripcion": "Periféricos y accesorios de computadora"
}
```

- **Respuesta esperada:** `200 OK`, la categoría actualizada; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Accesorios para PC",
  "descripcion": "Periféricos y accesorios de computadora"
}
```

#### Eliminar una categoría

- **Método y URL:** `DELETE http://techshop.local:8080/api/categorias/1`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `204 No Content`, sin cuerpo; `404 Not Found` si no existe. Elimina antes los productos que la referencien.

### Marcas — `/api/marcas`

#### Listar marcas

- **Método y URL:** `GET http://techshop.local:8080/api/marcas`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista JSON; si no hay registros, `[]`.

```json
[
  {
    "id": 1,
    "nombre": "TechBrand",
    "paisOrigen": "México"
  }
]
```

#### Consultar una marca

- **Método y URL:** `GET http://techshop.local:8080/api/marcas/1`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

#### Crear una marca

- **Método y URL:** `POST http://techshop.local:8080/api/marcas`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

- **Respuesta esperada:** `201 Created`, la marca guardada con su ID generado.

```json
{
  "id": 1,
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

#### Actualizar una marca

- **Método y URL:** `PUT http://techshop.local:8080/api/marcas/1`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombre": "TechBrand MX",
  "paisOrigen": "México"
}
```

- **Respuesta esperada:** `200 OK`, la marca actualizada; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "TechBrand MX",
  "paisOrigen": "México"
}
```

#### Eliminar una marca

- **Método y URL:** `DELETE http://techshop.local:8080/api/marcas/1`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `204 No Content`, sin cuerpo; `404 Not Found` si no existe. Elimina antes los productos que la referencien.

### Productos — `/api/productos`

`idCategoria` e `idMarca` deben ser IDs existentes obtenidos de las pruebas de categorías y marcas.

#### Listar productos

- **Método y URL:** `GET http://techshop.local:8080/api/productos`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista JSON; si no hay registros, `[]`.

```json
[
  {
    "id": 1,
    "nombre": "Teclado Mecánico RGB",
    "precio": 89.90,
    "stock": 20,
    "idCategoria": 1,
    "idMarca": 1
  }
]
```

#### Consultar un producto

- **Método y URL:** `GET http://techshop.local:8080/api/productos/1`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

#### Crear un producto

- **Método y URL:** `POST http://techshop.local:8080/api/productos`
- **Headers:** `Content-Type: application/json`.
- **Body:** reemplaza los dos IDs por los creados en la base.

```json
{
  "id": null,
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

- **Respuesta esperada:** `201 Created`, el producto guardado con su ID generado.

```json
{
  "id": 1,
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

#### Actualizar un producto

- **Método y URL:** `PUT http://techshop.local:8080/api/productos/1`
- **Headers:** `Content-Type: application/json`.
- **Body:** los IDs deben existir.

```json
{
  "id": null,
  "nombre": "Teclado Mecánico RGB Pro",
  "precio": 109.90,
  "stock": 15,
  "idCategoria": 1,
  "idMarca": 1
}
```

- **Respuesta esperada:** `200 OK`, el producto actualizado; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Teclado Mecánico RGB Pro",
  "precio": 109.90,
  "stock": 15,
  "idCategoria": 1,
  "idMarca": 1
}
```

#### Eliminar un producto

- **Método y URL:** `DELETE http://techshop.local:8080/api/productos/1`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `204 No Content`, sin cuerpo; `404 Not Found` si no existe.

### Personas — `/api/personas`

La entidad tiene los campos `id`, `nombre`, `apellido`, `fechaNacimiento`, `activo`, `fechaBaja`, `permiteMarketing` y `permiteTratamientoDatos`. La fecha de nacimiento usa `AAAA-MM-DD`; la fecha de baja es fecha-hora ISO y puede ser `null`. El ejemplo crea una persona activa con preferencias habilitadas.

#### Listar personas

- **Método y URL:** `GET http://techshop.local:8080/api/personas`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista JSON; si no hay registros, `[]`.

```json
[
  {
    "id": 1,
    "nombre": "Ana",
    "apellido": "López",
    "fechaNacimiento": "2000-01-15",
    "activo": true,
    "fechaBaja": null,
    "permiteMarketing": true,
    "permiteTratamientoDatos": true
  }
]
```

#### Consultar una persona

- **Método y URL:** `GET http://techshop.local:8080/api/personas/1`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

#### Crear una persona

- **Método y URL:** `POST http://techshop.local:8080/api/personas`
- **Headers:** `Content-Type: application/json`.
- **Body:**

```json
{
  "id": null,
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

- **Respuesta esperada:** `201 Created`, la persona guardada con su ID generado.

```json
{
  "id": 1,
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

#### Actualizar una persona

- **Método y URL:** `PUT http://techshop.local:8080/api/personas/1`
- **Headers:** `Content-Type: application/json`.
- **Body:** el controlador actualiza nombre, apellido y fecha de nacimiento; los otros campos del JSON no se copian al registro.

```json
{
  "id": null,
  "nombre": "Ana María",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

- **Respuesta esperada:** `200 OK`, la persona actualizada; `404 Not Found` si no existe.

```json
{
  "id": 1,
  "nombre": "Ana María",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

#### Eliminar una persona

- **Método y URL:** `DELETE http://techshop.local:8080/api/personas/1`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `204 No Content`, sin cuerpo; `404 Not Found` si no existe. No elimines físicamente la persona que usarás para ARCO: sus solicitudes la referencian.

## 6. Derechos ARCO

Las rutas de `/api/arco` necesitan el ID de una persona existente. El modelo `SolicitudArco` tiene `id`, `persona`, `tipoDerecho`, `motivo`, `fechaSolicitud` y `estado`; no existe un endpoint para crear solicitudes directamente. El controlador registra las solicitudes de acceso, rectificación, cancelación y oposición; el modelo oculta la propiedad `persona` en el JSON (`@JsonIgnore`).

### Acceso

- **Método y URL:** `GET http://techshop.local:8080/api/arco/acceso/2`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`; registra primero la solicitud y devuelve persona, preferencias e historial, incluyendo el acceso recién registrado. `404 Not Found` si la persona no existe.

```json
{
  "persona": {
    "id": 2,
    "nombre": "Luis",
    "apellido": "Pérez",
    "fechaNacimiento": "1998-06-20",
    "activo": true,
    "fechaBaja": null,
    "permiteMarketing": true,
    "permiteTratamientoDatos": true
  },
  "preferenciasPrivacidad": {
    "activo": true,
    "fechaBaja": null,
    "permiteMarketing": true,
    "permiteTratamientoDatos": true
  },
  "historialSolicitudes": [
    {
      "id": 1,
      "tipoDerecho": "ACCESO",
      "motivo": null,
      "fechaSolicitud": "2026-11-05T10:00:00",
      "estado": "ATENDIDA"
    }
  ]
}
```

La fecha y los IDs de la respuesta son ejemplos; se generan al ejecutar la petición.

### Rectificación

- **Método y URL:** `PUT http://techshop.local:8080/api/arco/rectificacion/2`
- **Headers:** `Content-Type: application/json`.
- **Body:** el controlador recibe `Persona` y aplica nombre, apellido y fecha de nacimiento; los campos restantes se muestran para documentar el modelo, pero no se actualizan aquí.

```json
{
  "id": null,
  "nombre": "Luis Alberto",
  "apellido": "Pérez",
  "fechaNacimiento": "1998-06-20",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

- **Respuesta esperada:** `200 OK`, la persona rectificada; `400 Bad Request` si falta el nombre o está vacío; `404 Not Found` si no existe.

```json
{
  "id": 2,
  "nombre": "Luis Alberto",
  "apellido": "Pérez",
  "fechaNacimiento": "1998-06-20",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": true,
  "permiteTratamientoDatos": true
}
```

### Cancelación

- **Método y URL:** `DELETE http://techshop.local:8080/api/arco/cancelacion/2`
- **Headers:** ninguno requerido (opcional `Accept: application/json`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, la persona anonimizada; `404 Not Found` si no existe. Esta ruta no borra físicamente la fila.

```json
{
  "id": 2,
  "nombre": "BLOQUEADO_ARCO",
  "apellido": "ELIMINADO",
  "fechaNacimiento": null,
  "activo": false,
  "fechaBaja": "2026-11-05T10:05:00",
  "permiteMarketing": false,
  "permiteTratamientoDatos": false
}
```

### Oposición

- **Método y URL:** `PATCH http://techshop.local:8080/api/arco/oposicion/2`
- **Headers:** `Content-Type: application/json`.
- **Body:** envía al menos una preferencia; ambas son opcionales en la solicitud.

```json
{
  "permiteMarketing": false,
  "permiteTratamientoDatos": false
}
```

- **Respuesta esperada:** `200 OK`, persona actualizada; `400 Bad Request` si el cuerpo está vacío o no contiene ninguna preferencia; `404 Not Found` si no existe.

```json
{
  "id": 2,
  "nombre": "Luis",
  "apellido": "Pérez",
  "fechaNacimiento": "1998-06-20",
  "activo": true,
  "fechaBaja": null,
  "permiteMarketing": false,
  "permiteTratamientoDatos": false
}
```

### Consultar solicitudes ARCO

- **Método y URL:** `GET http://techshop.local:8080/api/arco/solicitudes/2`
- **Headers:** `Accept: application/json` (no requiere `Content-Type`).
- **Body:** ninguno.
- **Respuesta esperada:** `200 OK`, lista de solicitudes (puede ser `[]`); `404 Not Found` si la persona no existe.

```json
[
  {
    "id": 1,
    "tipoDerecho": "ACCESO",
    "motivo": null,
    "fechaSolicitud": "2026-11-05T10:00:00",
    "estado": "ATENDIDA"
  },
  {
    "id": 2,
    "tipoDerecho": "RECTIFICACION",
    "motivo": null,
    "fechaSolicitud": "2026-11-05T10:02:00",
    "estado": "ATENDIDA"
  }
]
```

Las filas las crea el controlador. `motivo` es `null` porque esas operaciones no establecen uno; `estado` usa el valor por defecto `ATENDIDA`.

## 7. Solución de problemas de conexión

- Confirma que la aplicación esté ejecutándose y que la consola indique el puerto `8080`.
- Comprueba que ambas computadoras estén en la misma red y que el router no aísle a los clientes.
- Comprueba que el nombre elegido resuelva a la IPv4 actual del anfitrión y que el firewall permita TCP `8080` en el perfil privado.
- Si el nombre no resuelve, vuelve a probar la entrada DNS local/mDNS del router; usar `localhost` en la computadora del profesor apunta a esa misma computadora, no al servidor TechShop.

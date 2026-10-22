# TechShop API REST - Spring Boot

## Información del Estudiante
* **Alumno:** Aldo Yeray Rodriguez Hernandez
* **Grupo:** 8ids2
* **Proyecto:** API REST con Spring Boot y consulta filtrada por ID

---

## Descripción del Proyecto
Este proyecto consiste en una API REST desarrollada con **Spring Boot** para la gestión de un catálogo de productos tecnológicos (*TechShop*). Los recursos `productos`, `categorias`, `marcas`, `roles` y `personas` se almacenan en PostgreSQL y cuentan con operaciones completas de creación, consulta, actualización y eliminación (CRUD).

---

## Tecnologías Utilizadas
* **Lenguaje:** Java 17+
* **Framework:** Spring Boot (Spring Web y Spring Data JPA)
* **Base de datos:** PostgreSQL
* **Gestor de dependencias:** Maven
* **Cliente de pruebas:** Postman y Navegador Web
* **Control de versiones:** Git y GitHub

---

## Estructura del Proyecto

```text
techshop/
 ├── src/
 │    └── main/
 │         └── java/
 │              └── com/techshop/techshop/
 │                   ├── controller/
 │                   │    ├── ProductoController.java
 │                   │    └── CategoriaController.java
 │                   └── TechshopApplication.java
 ├── pom.xml
 └── README.md
```

---

## Requisitos Previos
* Java Development Kit (JDK) 17 o superior.
* Git instalado.
* Postman o cualquier navegador web para la ejecución de peticiones HTTP.

---

## Instrucciones de Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/TU_USUARIO/techshop-api.git
   ```

2. **Acceder a la carpeta del proyecto:**
   ```bash
   cd techshop
   ```

3. **Ejecutar la aplicación con Maven Wrapper:**
   * **Windows:**
     ```powershell
     .\mvnw spring-boot:run
     ```
   * **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```

  La conexión predeterminada busca una base `techshop` en `localhost:5432`, con usuario `postgres`. La contraseña se solicita mediante la variable obligatoria `DB_PASSWORD`. En PowerShell, configura la contraseña de forma que no aparezca en pantalla ni quede escrita en el historial del comando:

  ```powershell
$securePassword = Read-Host "Contraseña de PostgreSQL" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
  ```

  Si tu servidor, base de datos o usuario son distintos de los valores predeterminados, configura también `DB_URL` y `DB_USERNAME` con los valores de tu conexión.

  En Linux o macOS:

  ```bash
  export DB_URL="jdbc:postgresql://localhost:5432/mi_base"
  export DB_USERNAME="mi_usuario"
  export DB_PASSWORD="tu_contraseña"
  ```

  La aplicación inicia en el puerto local `8080`: `http://localhost:8080`. Hibernate usa `ddl-auto=update` para crear las tablas que falten y actualizar el esquema sin borrar los datos existentes.

  Las entidades usan las tablas y columnas definidas en `TechShop.sql`. Los recursos añadidos son `marcas` (`nombre`, `pais_origen`), `roles` (`nombre_rol`, `descripcion`) y `personas` (`nombre`, `apellido`, `fecha_nacimiento`). `productos` usa `nombre`, `precio`, `stock`, `id_categoria` e `id_marca`.

---

## Endpoints de la API REST

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **GET** | `/api/productos` | Obtiene la lista completa de productos. |
| **GET** | `/api/productos/{id}` | Obtiene un producto específico filtrado por ID. |
| **POST** | `/api/productos` | Crea un producto. |
| **PUT** | `/api/productos/{id}` | Actualiza un producto existente. |
| **DELETE** | `/api/productos/{id}` | Elimina un producto. |
| **GET** | `/api/categorias` | Obtiene la lista completa de categorías. |
| **GET** | `/api/categorias/{id}` | Obtiene una categoría específica filtrada por ID. |
| **POST** | `/api/categorias` | Crea una categoría. |
| **PUT** | `/api/categorias/{id}` | Actualiza una categoría existente. |
| **DELETE** | `/api/categorias/{id}` | Elimina una categoría. |
| **GET** | `/api/marcas` | Obtiene todas las marcas. |
| **GET** | `/api/marcas/{id}` | Obtiene una marca por ID. |
| **POST** | `/api/marcas` | Crea una marca. |
| **PUT** | `/api/marcas/{id}` | Actualiza una marca. |
| **DELETE** | `/api/marcas/{id}` | Elimina una marca. |
| **GET** | `/api/roles` | Obtiene todos los roles. |
| **GET** | `/api/roles/{id}` | Obtiene un rol por ID. |
| **POST** | `/api/roles` | Crea un rol. |
| **PUT** | `/api/roles/{id}` | Actualiza un rol. |
| **DELETE** | `/api/roles/{id}` | Elimina un rol. |
| **GET** | `/api/personas` | Obtiene todas las personas. |
| **GET** | `/api/personas/{id}` | Obtiene una persona por ID. |
| **POST** | `/api/personas` | Crea una persona. |
| **PUT** | `/api/personas/{id}` | Actualiza una persona. |
| **DELETE** | `/api/personas/{id}` | Elimina una persona. |

Para crear un producto, envía por ejemplo `POST /api/productos` con `Content-Type: application/json`:

```json
{
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.90,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

Para crear una categoría, envía `POST /api/categorias` con este cuerpo:

```json
{
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

Para crear una marca, envía `POST /api/marcas`:

```json
{
  "nombre": "TechBrand",
  "paisOrigen": "México"
}
```

Para crear un rol, envía `POST /api/roles`:

```json
{
  "nombreRol": "cliente",
  "descripcion": "Usuario cliente"
}
```

Para crear una persona, envía `POST /api/personas`:

```json
{
  "nombre": "Ana",
  "apellido": "López",
  "fechaNacimiento": "2000-01-15"
}
```

Los `PUT` reciben la misma estructura en el endpoint con ID. Las solicitudes a IDs inexistentes responden `404 Not Found`.

## Pruebas

Las pruebas de integración usan H2 en memoria (no modifican PostgreSQL) y recorren las operaciones CRUD de los cinco recursos:

```powershell
.\mvnw.cmd test
```

---

## Ejemplos de Respuesta JSON

### Consulta por ID de Producto (`GET http://localhost:8080/api/productos/1`)
```json
{
  "id": 1,
  "nombre": "Teclado Mecánico RGB",
  "precio": 89.9,
  "stock": 20,
  "idCategoria": 1,
  "idMarca": 1
}
```

### Consulta por ID de Categoría (`GET http://localhost:8080/api/categorias/1`)
```json
{
  "id": 1,
  "nombre": "Periféricos",
  "descripcion": "Teclados, mouses y audífonos"
}
```

---

## Evidencias de Pruebas
Las respuestas de la API fueron probadas y validadas exitosamente tanto desde el **navegador web** como en **Postman**, retornando correctamente los datos filtrados con un código de estado HTTP `200 OK`.
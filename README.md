# TechShop API REST - Spring Boot

## Información del Estudiante
* **Alumno:** Aldo Yeray Rodriguez Hernandez
* **Grupo:** 8ids2
* **Proyecto:** API REST con Spring Boot y consulta filtrada por ID

---

## Descripción del Proyecto
Este proyecto consiste en una API REST desarrollada con **Spring Boot** para la gestión de un catálogo de productos tecnológicos (*TechShop*). Cuenta con dos controladores REST que responden datos de prueba (*mock data*) en formato JSON y permiten realizar consultas generales o filtrar por identificador único (`id`).

---

## Tecnologías Utilizadas
* **Lenguaje:** Java 17+
* **Framework:** Spring Boot (Spring Web)
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

4. La aplicación iniciará en el puerto local `8080`: `http://localhost:8080`

---

## Endpoints de la API REST

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **GET** | `/api/productos` | Obtiene la lista completa de productos. |
| **GET** | `/api/productos/{id}` | Obtiene un producto específico filtrado por ID. |
| **GET** | `/api/categorias` | Obtiene la lista completa de categorías. |
| **GET** | `/api/categorias/{id}` | Obtiene una categoría específica filtrada por ID. |

---

## Ejemplos de Respuesta JSON

### Consulta por ID de Producto (`GET http://localhost:8080/api/productos/1`)
```json
{
  "id": 1,
  "nombre": "Teclado Mecánico RGB",
  "categoria": "Periféricos",
  "precio": 89.9
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
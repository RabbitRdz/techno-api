package com.techshop.techshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TechshopApplicationTests {

    @Autowired
    private MockMvc mockMvc;

	@Test
    void productoPermiteOperacionesCrud() throws Exception {
	MvcResult creado = mockMvc.perform(post("/api/productos")
			.contentType("application/json")
			.content("""
				{"nombre":"Teclado de prueba","precio":89.90,"stock":5,"idCategoria":1,"idMarca":1}
				"""))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").isNumber())
		.andReturn();

	String ubicacion = creado.getResponse().getHeader("Location");
	mockMvc.perform(get(ubicacion))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nombre").value("Teclado de prueba"));

	mockMvc.perform(put(ubicacion)
			.contentType("application/json")
			.content("""
				{"nombre":"Teclado actualizado","precio":99.90,"stock":3,"idCategoria":1,"idMarca":1}
				"""))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nombre").value("Teclado actualizado"));

	mockMvc.perform(get("/api/productos"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].nombre").value("Teclado actualizado"));

	mockMvc.perform(delete(ubicacion)).andExpect(status().isNoContent());
	mockMvc.perform(get(ubicacion)).andExpect(status().isNotFound());
    }

    @Test
    void categoriaPermiteOperacionesCrud() throws Exception {
	MvcResult creada = mockMvc.perform(post("/api/categorias")
			.contentType("application/json")
			.content("""
				{"nombre":"Categoría de prueba","descripcion":"Descripción inicial"}
				"""))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").isNumber())
		.andReturn();

	String ubicacion = creada.getResponse().getHeader("Location");
	mockMvc.perform(get(ubicacion))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nombre").value("Categoría de prueba"));

	mockMvc.perform(put(ubicacion)
			.contentType("application/json")
			.content("""
				{"nombre":"Categoría actualizada","descripcion":"Descripción nueva"}
				"""))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nombre").value("Categoría actualizada"));

	mockMvc.perform(get("/api/categorias"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].nombre").value("Categoría actualizada"));

	mockMvc.perform(delete(ubicacion)).andExpect(status().isNoContent());
	mockMvc.perform(get(ubicacion)).andExpect(status().isNotFound());
	}

	@Test
	void marcaPermiteOperacionesCrud() throws Exception {
		MvcResult creada = mockMvc.perform(post("/api/marcas")
				.contentType("application/json")
				.content("""
						{"nombre":"Marca de prueba","paisOrigen":"México"}
						"""))
			.andExpect(status().isCreated())
			.andReturn();

		String ubicacion = creada.getResponse().getHeader("Location");
		mockMvc.perform(get(ubicacion)).andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Marca de prueba"));
		mockMvc.perform(put(ubicacion).contentType("application/json")
				.content("""
						{"nombre":"Marca actualizada","paisOrigen":"Canadá"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.paisOrigen").value("Canadá"));
		mockMvc.perform(get("/api/marcas")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombre").value("Marca actualizada"));
		mockMvc.perform(delete(ubicacion)).andExpect(status().isNoContent());
		mockMvc.perform(get(ubicacion)).andExpect(status().isNotFound());
	}

	@Test
	void rolPermiteOperacionesCrud() throws Exception {
		MvcResult creado = mockMvc.perform(post("/api/roles")
				.contentType("application/json")
				.content("""
						{"nombreRol":"Rol de prueba","descripcion":"Descripción inicial"}
						"""))
			.andExpect(status().isCreated())
			.andReturn();

		String ubicacion = creado.getResponse().getHeader("Location");
		mockMvc.perform(get(ubicacion)).andExpect(status().isOk())
				.andExpect(jsonPath("$.nombreRol").value("Rol de prueba"));
		mockMvc.perform(put(ubicacion).contentType("application/json")
				.content("""
						{"nombreRol":"Rol actualizado","descripcion":"Descripción nueva"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombreRol").value("Rol actualizado"));
		mockMvc.perform(get("/api/roles")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombreRol").value("Rol actualizado"));
		mockMvc.perform(delete(ubicacion)).andExpect(status().isNoContent());
		mockMvc.perform(get(ubicacion)).andExpect(status().isNotFound());
	}

	@Test
	void personaPermiteOperacionesCrud() throws Exception {
		MvcResult creada = mockMvc.perform(post("/api/personas")
				.contentType("application/json")
				.content("""
						{"nombre":"Persona de prueba","apellido":"Apellido","fechaNacimiento":"2000-01-15"}
						"""))
			.andExpect(status().isCreated())
			.andReturn();

		String ubicacion = creada.getResponse().getHeader("Location");
		mockMvc.perform(get(ubicacion)).andExpect(status().isOk())
				.andExpect(jsonPath("$.fechaNacimiento").value("2000-01-15"));
		mockMvc.perform(put(ubicacion).contentType("application/json")
				.content("""
						{"nombre":"Persona actualizada","apellido":"Apellido nuevo","fechaNacimiento":"2001-02-16"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("Persona actualizada"));
		mockMvc.perform(get("/api/personas")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombre").value("Persona actualizada"));
		mockMvc.perform(delete(ubicacion)).andExpect(status().isNoContent());
		mockMvc.perform(get(ubicacion)).andExpect(status().isNotFound());
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void arcoRegistraYAtiendeLosDerechos() throws Exception {
		MvcResult creada = mockMvc.perform(post("/api/personas")
				.contentType("application/json")
				.content("""
						{"nombre":"Persona ARCO","apellido":"Prueba","fechaNacimiento":"2000-01-15"}
						"""))
			.andExpect(status().isCreated())
			.andReturn();
		String idPersona = creada.getResponse().getContentAsString()
				.replaceAll("(?s).*\"id\":(\\d+).*", "$1");

		mockMvc.perform(get("/api/arco/acceso/" + idPersona))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.persona.nombre").value("Persona ARCO"))
			.andExpect(jsonPath("$.preferenciasPrivacidad.fechaBaja").doesNotExist())
			.andExpect(jsonPath("$.historialSolicitudes[0].tipoDerecho").value("ACCESO"));

		mockMvc.perform(put("/api/arco/rectificacion/" + idPersona)
				.contentType("application/json")
				.content("""
						{"nombre":"Nombre rectificado","apellido":"Apellido actualizado","fechaNacimiento":"2001-02-16"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("Nombre rectificado"));

		mockMvc.perform(patch("/api/arco/oposicion/" + idPersona)
				.contentType("application/json")
				.content("""
						{"permiteMarketing":false,"permiteTratamientoDatos":false}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.permiteMarketing").value(false));

		mockMvc.perform(delete("/api/arco/cancelacion/" + idPersona))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.activo").value(false))
			.andExpect(jsonPath("$.nombre").value("BLOQUEADO_ARCO"));

		mockMvc.perform(get("/api/arco/solicitudes/" + idPersona))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(4));
	}
}

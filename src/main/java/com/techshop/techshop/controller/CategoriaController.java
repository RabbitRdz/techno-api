package com.techshop.techshop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private List<Map<String, Object>> getCategoriasMock() {
        List<Map<String, Object>> categorias = new ArrayList<>();

        Map<String, Object> cat1 = new HashMap<>();
        cat1.put("id", 1);
        cat1.put("nombre", "Periféricos");
        cat1.put("descripcion", "Teclados, mouses y audífonos");

        Map<String, Object> cat2 = new HashMap<>();
        cat2.put("id", 2);
        cat2.put("nombre", "Pantallas");
        cat2.put("descripcion", "Monitores para gaming y oficina");

        categorias.add(cat1);
        categorias.add(cat2);
        return categorias;
    }

    // Obtener todas las categorías
    @GetMapping
    public List<Map<String, Object>> obtenerCategorias() {
        return getCategoriasMock();
    }

    // Filtrar categoría por ID
    @GetMapping("/{id}")
    public Map<String, Object> obtenerCategoriaPorId(@PathVariable Integer id) {
        return getCategoriasMock().stream()
                .filter(c -> c.get("id").equals(id))
                .findFirst()
                .orElse(null);
    }
}
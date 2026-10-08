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
@RequestMapping("/api/productos")
public class ProductoController {

    private List<Map<String, Object>> getProductosMock() {
        List<Map<String, Object>> productos = new ArrayList<>();

        Map<String, Object> prod1 = new HashMap<>();
        prod1.put("id", 1);
        prod1.put("nombre", "Teclado Mecánico RGB");
        prod1.put("categoria", "Periféricos");
        prod1.put("precio", 89.90);

        Map<String, Object> prod2 = new HashMap<>();
        prod2.put("id", 2);
        prod2.put("nombre", "Monitor 144Hz 27'");
        prod2.put("categoria", "Pantallas");
        prod2.put("precio", 249.50);

        productos.add(prod1);
        productos.add(prod2);
        return productos;
    }

    // Obtener todos los productos
    @GetMapping
    public List<Map<String, Object>> obtenerProductos() {
        return getProductosMock();
    }

    // Filtrar producto por ID
    @GetMapping("/{id}")
    public Map<String, Object> obtenerProductoPorId(@PathVariable Integer id) {
        return getProductosMock().stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .orElse(null);
    }
}
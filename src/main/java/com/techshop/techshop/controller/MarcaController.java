package com.techshop.techshop.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techshop.techshop.model.Marca;
import com.techshop.techshop.repository.MarcaRepository;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaRepository marcaRepository;

    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @GetMapping
    public List<Marca> obtenerMarcas() {
        return marcaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Marca obtenerMarcaPorId(@PathVariable Integer id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Marca> crearMarca(@RequestBody Marca marca) {
        marca.setId(null);
        Marca guardada = marcaRepository.save(marca);
        return ResponseEntity.created(URI.create("/api/marcas/" + guardada.getId())).body(guardada);
    }

    @PutMapping("/{id}")
    public Marca actualizarMarca(@PathVariable Integer id, @RequestBody Marca datos) {
        Marca marca = obtenerMarcaPorId(id);
        marca.setNombre(datos.getNombre());
        marca.setPaisOrigen(datos.getPaisOrigen());
        return marcaRepository.save(marca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Integer id) {
        if (!marcaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada");
        }
        marcaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
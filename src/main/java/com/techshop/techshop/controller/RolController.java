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

import com.techshop.techshop.model.Rol;
import com.techshop.techshop.repository.RolRepository;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolRepository rolRepository;

    public RolController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @GetMapping
    public List<Rol> obtenerRoles() {
        return rolRepository.findAll();
    }

    @GetMapping("/{id}")
    public Rol obtenerRolPorId(@PathVariable Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        rol.setId(null);
        Rol guardado = rolRepository.save(rol);
        return ResponseEntity.created(URI.create("/api/roles/" + guardado.getId())).body(guardado);
    }

    @PutMapping("/{id}")
    public Rol actualizarRol(@PathVariable Integer id, @RequestBody Rol datos) {
        Rol rol = obtenerRolPorId(id);
        rol.setNombreRol(datos.getNombreRol());
        rol.setDescripcion(datos.getDescripcion());
        return rolRepository.save(rol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado");
        }
        rolRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
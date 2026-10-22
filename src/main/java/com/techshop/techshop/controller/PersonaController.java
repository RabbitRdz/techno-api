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

import com.techshop.techshop.model.Persona;
import com.techshop.techshop.repository.PersonaRepository;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaRepository personaRepository;

    public PersonaController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @GetMapping
    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Persona obtenerPersonaPorId(@PathVariable Integer id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        persona.setId(null);
        Persona guardada = personaRepository.save(persona);
        return ResponseEntity.created(URI.create("/api/personas/" + guardada.getId())).body(guardada);
    }

    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable Integer id, @RequestBody Persona datos) {
        Persona persona = obtenerPersonaPorId(id);
        persona.setNombre(datos.getNombre());
        persona.setApellido(datos.getApellido());
        persona.setFechaNacimiento(datos.getFechaNacimiento());
        return personaRepository.save(persona);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Integer id) {
        if (!personaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        personaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
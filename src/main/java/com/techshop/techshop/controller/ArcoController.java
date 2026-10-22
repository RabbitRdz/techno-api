package com.techshop.techshop.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techshop.techshop.model.Persona;
import com.techshop.techshop.model.SolicitudArco;
import com.techshop.techshop.repository.PersonaRepository;
import com.techshop.techshop.repository.SolicitudArcoRepository;

@RestController
@RequestMapping("/api/arco")
public class ArcoController {

    private final PersonaRepository personaRepository;
    private final SolicitudArcoRepository solicitudArcoRepository;

    public ArcoController(PersonaRepository personaRepository, SolicitudArcoRepository solicitudArcoRepository) {
        this.personaRepository = personaRepository;
        this.solicitudArcoRepository = solicitudArcoRepository;
    }

    @GetMapping("/acceso/{idPersona}")
    @Transactional
    public Map<String, Object> acceder(@PathVariable Integer idPersona) {
        Persona persona = obtenerPersona(idPersona);
        registrarSolicitud(persona, "ACCESO", null);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("persona", persona);
        Map<String, Object> preferencias = new LinkedHashMap<>();
        preferencias.put("activo", persona.isActivo());
        preferencias.put("fechaBaja", persona.getFechaBaja());
        preferencias.put("permiteMarketing", persona.isPermiteMarketing());
        preferencias.put("permiteTratamientoDatos", persona.isPermiteTratamientoDatos());
        respuesta.put("preferenciasPrivacidad", preferencias);
        respuesta.put("historialSolicitudes", solicitudArcoRepository.findByPersonaId(idPersona));
        return respuesta;
    }

    @PutMapping("/rectificacion/{idPersona}")
    @Transactional
    public Persona rectificar(@PathVariable Integer idPersona, @RequestBody Persona datos) {
        Persona persona = obtenerPersona(idPersona);
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        persona.setNombre(datos.getNombre());
        persona.setApellido(datos.getApellido());
        persona.setFechaNacimiento(datos.getFechaNacimiento());
        Persona actualizada = personaRepository.save(persona);
        registrarSolicitud(actualizada, "RECTIFICACION", null);
        return actualizada;
    }

    @DeleteMapping("/cancelacion/{idPersona}")
    @Transactional
    public Persona cancelar(@PathVariable Integer idPersona) {
        Persona persona = obtenerPersona(idPersona);
        persona.setActivo(false);
        persona.setFechaBaja(LocalDateTime.now());
        persona.setPermiteMarketing(false);
        persona.setPermiteTratamientoDatos(false);
        persona.setNombre("BLOQUEADO_ARCO");
        persona.setApellido("ELIMINADO");
        persona.setFechaNacimiento(null);

        Persona cancelada = personaRepository.save(persona);
        registrarSolicitud(cancelada, "CANCELACION", null);
        return cancelada;
    }

    @PatchMapping("/oposicion/{idPersona}")
    @Transactional
    public Persona oponerse(@PathVariable Integer idPersona, @RequestBody OposicionRequest solicitud) {
        if (solicitud == null
                || (solicitud.getPermiteMarketing() == null && solicitud.getPermiteTratamientoDatos() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar al menos una preferencia");
        }

        Persona persona = obtenerPersona(idPersona);
        if (solicitud.getPermiteMarketing() != null) {
            persona.setPermiteMarketing(solicitud.getPermiteMarketing());
        }
        if (solicitud.getPermiteTratamientoDatos() != null) {
            persona.setPermiteTratamientoDatos(solicitud.getPermiteTratamientoDatos());
        }

        Persona actualizada = personaRepository.save(persona);
        registrarSolicitud(actualizada, "OPOSICION", null);
        return actualizada;
    }

    @GetMapping("/solicitudes/{idPersona}")
    public List<SolicitudArco> obtenerSolicitudes(@PathVariable Integer idPersona) {
        obtenerPersona(idPersona);
        return solicitudArcoRepository.findByPersonaId(idPersona);
    }

    private Persona obtenerPersona(Integer idPersona) {
        return personaRepository.findById(idPersona)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));
    }

    private void registrarSolicitud(Persona persona, String tipoDerecho, String motivo) {
        SolicitudArco solicitud = new SolicitudArco();
        solicitud.setPersona(persona);
        solicitud.setTipoDerecho(tipoDerecho);
        solicitud.setMotivo(motivo);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitudArcoRepository.save(solicitud);
    }

    public static class OposicionRequest {

        private Boolean permiteMarketing;
        private Boolean permiteTratamientoDatos;

        public Boolean getPermiteMarketing() {
            return permiteMarketing;
        }

        public void setPermiteMarketing(Boolean permiteMarketing) {
            this.permiteMarketing = permiteMarketing;
        }

        public Boolean getPermiteTratamientoDatos() {
            return permiteTratamientoDatos;
        }

        public void setPermiteTratamientoDatos(Boolean permiteTratamientoDatos) {
            this.permiteTratamientoDatos = permiteTratamientoDatos;
        }
    }
}

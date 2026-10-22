package com.techshop.techshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.SolicitudArco;

public interface SolicitudArcoRepository extends JpaRepository<SolicitudArco, Integer> {

    List<SolicitudArco> findByPersonaId(Integer idPersona);
}

package com.techshop.techshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
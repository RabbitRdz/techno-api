package com.techshop.techshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
}
package com.techshop.techshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
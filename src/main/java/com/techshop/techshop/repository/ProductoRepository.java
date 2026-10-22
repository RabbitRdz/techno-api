package com.techshop.techshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
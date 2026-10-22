package com.techshop.techshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techshop.techshop.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
}
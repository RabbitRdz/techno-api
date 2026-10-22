package com.techshop.techshop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 120)
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean activo = true;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "permite_marketing", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean permiteMarketing = true;

    @Column(name = "permite_tratamiento_datos", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean permiteTratamientoDatos = true;

    public Persona() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDateTime fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public boolean isPermiteMarketing() {
        return permiteMarketing;
    }

    public void setPermiteMarketing(boolean permiteMarketing) {
        this.permiteMarketing = permiteMarketing;
    }

    public boolean isPermiteTratamientoDatos() {
        return permiteTratamientoDatos;
    }

    public void setPermiteTratamientoDatos(boolean permiteTratamientoDatos) {
        this.permiteTratamientoDatos = permiteTratamientoDatos;
    }
}
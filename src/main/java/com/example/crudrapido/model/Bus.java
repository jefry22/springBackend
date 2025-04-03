package com.example.crudrapido.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroBus;
    @Column(unique = true)
    private String placa;
    @Column(nullable = false,updatable = false)
    private LocalDate fechaCreacion;
    private String caracteristica;
    private Boolean activo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="marca_id")
    private Marca marca;

    public Bus(){}

    public Bus(Long id, String numeroBus, String placa, LocalDate fechaCreacion, String caracteristica, Boolean activo, Marca marca) {
        this.id = id;
        this.numeroBus = numeroBus;
        this.placa = placa;
        this.fechaCreacion = fechaCreacion;
        this.caracteristica = caracteristica;
        this.activo = activo;
        this.marca = marca;
    }
    @PrePersist
    public void prePersist(){
        if(this.fechaCreacion== null){
            this.fechaCreacion = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroBus() {
        return numeroBus;
    }

    public void setNumeroBus(String numeroBus) {
        this.numeroBus = numeroBus;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}

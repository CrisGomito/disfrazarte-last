/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author cristian
 */
public class Evento {

    private List<String> disfracesAsignados;
    private List<String> usuariosAsignados;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private int id;
    private String titulo;
    private String detalles;
    private LocalDate fecha;
    private String location;
    private String notes;
    private String estado;
    private LocalDateTime createdAt;
    private String color;

    public List<String> getDisfracesAsignados() {
        return disfracesAsignados;
    }

    public void setDisfracesAsignados(List<String> disfracesAsignados) {
        this.disfracesAsignados = disfracesAsignados;
    }

    public List<String> getUsuariosAsignados() {
        return usuariosAsignados;
    }

    public void setUsuariosAsignados(List<String> usuariosAsignados) {
        this.usuariosAsignados = usuariosAsignados;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import java.time.LocalDateTime;

/**
 *
 * @author cristian
 */
public class EventoAsignacion {

    private int id;
    private int eventCostumeId;
    private int eventId;
    private int disfrazId;
    private String disfrazNombre;
    private int userId;
    private String userNombre;
    private Integer lendingId;
    private LocalDateTime assignedAt;
    
    public EventoAsignacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventCostumeId() {
        return eventCostumeId;
    }

    public void setEventCostumeId(int eventCostumeId) {
        this.eventCostumeId = eventCostumeId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getDisfrazId() {
        return disfrazId;
    }

    public void setDisfrazId(int disfrazId) {
        this.disfrazId = disfrazId;
    }

    public String getDisfrazNombre() {
        return disfrazNombre;
    }

    public void setDisfrazNombre(String disfrazNombre) {
        this.disfrazNombre = disfrazNombre;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNombre() {
        return userNombre;
    }

    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
    }

    public Integer getLendingId() {
        return lendingId;
    }

    public void setLendingId(Integer lendingId) {
        this.lendingId = lendingId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}

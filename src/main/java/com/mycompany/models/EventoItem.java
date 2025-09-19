/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

/**
 *
 * @author cristian
 */
public class EventoItem {

    private int id;
    private int eventoId;
    private int disfrazId;
    private int qtyReserved;
    private int qtyAssigned;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public int getDisfrazId() {
        return disfrazId;
    }

    public void setDisfrazId(int disfrazId) {
        this.disfrazId = disfrazId;
    }

    public int getQtyReserved() {
        return qtyReserved;
    }

    public void setQtyReserved(int qtyReserved) {
        this.qtyReserved = qtyReserved;
    }

    public int getQtyAssigned() {
        return qtyAssigned;
    }

    public void setQtyAssigned(int qtyAssigned) {
        this.qtyAssigned = qtyAssigned;
    }
}

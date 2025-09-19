/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

/**
 *
 * @author cristian
 */
public class UserCostume {

    private int id;
    private int userId;
    private int dsfrId;
    private int qty;
    private String status;
    private String disfrazNombre;
    private String usuarioNombre;

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDsfrId() {
        return dsfrId;
    }

    public void setDsfrId(int dsfrId) {
        this.dsfrId = dsfrId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisfrazNombre() {
        return disfrazNombre;
    }

    public void setDisfrazNombre(String disfrazNombre) {
        this.disfrazNombre = disfrazNombre;
    }
}

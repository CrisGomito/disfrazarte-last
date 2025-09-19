/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.interfaces;

import com.mycompany.models.EventoAsignacion;
import java.util.List;

/**
 *
 * @author cristian
 */
public interface DAOEventoAsignacion {

    public void insertar(EventoAsignacion ea) throws Exception;

    public void eliminar(int id) throws Exception;

    public List<EventoAsignacion> listarPorEvento(int eventId) throws Exception;

    public List<EventoAsignacion> listarPorUsuario(int userId) throws Exception;

    public int contarPorDisfraz(int dsfrId) throws Exception;

    public List<EventoAsignacion> listarPorEventCostume(int eventCostumeId) throws Exception;

    public boolean existeAsignacion(int eventCostumeId, int userId) throws Exception;
}

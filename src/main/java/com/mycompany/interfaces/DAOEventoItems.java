/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.interfaces;

import com.mycompany.models.EventoItem;
import java.util.List;

/**
 *
 * @author cristian
 */
public interface DAOEventoItems {

    void insertar(EventoItem e) throws Exception;

    void modificar(EventoItem e) throws Exception;

    void eliminar(int id) throws Exception;

    List<EventoItem> listarPorEvento(int eventId) throws Exception;

    void eliminarPorEvento(int eventoId) throws Exception;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.interfaces;

import com.mycompany.models.Evento;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author cristian
 */
public interface DAOEventos {

    void insertar(Evento e) throws Exception;

    void modificar(Evento e) throws Exception;

    void eliminar(int id) throws Exception;

    Evento obtenerPorId(int id) throws Exception;

    List<Evento> listar() throws Exception;

    List<Evento> listarPorFecha(LocalDate fecha) throws Exception;
}

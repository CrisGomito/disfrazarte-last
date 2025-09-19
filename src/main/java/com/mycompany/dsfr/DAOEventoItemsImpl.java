/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOEventoItems;
import com.mycompany.models.EventoItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristian
 */
public class DAOEventoItemsImpl extends Database implements DAOEventoItems {

    @Override
    public void insertar(EventoItem e) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "INSERT INTO event_costumes(event_id, dsfr_id, qty_reserved, qty_assigned) VALUES(?,?,?,?)"
            );
            st.setInt(1, e.getEventoId());
            st.setInt(2, e.getDisfrazId());
            st.setInt(3, e.getQtyReserved());
            st.setInt(4, e.getQtyAssigned());
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(EventoItem e) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE event_costumes SET qty_reserved=?, qty_assigned=? WHERE id=?"
            );
            st.setInt(1, e.getQtyReserved());
            st.setInt(2, e.getQtyAssigned());
            st.setInt(3, e.getId());
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM event_costumes WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<EventoItem> listarPorEvento(int eventId) throws Exception {
        List<EventoItem> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM event_costumes WHERE event_id=?");
            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                EventoItem ei = new EventoItem();
                ei.setId(rs.getInt("id"));
                ei.setEventoId(rs.getInt("event_id"));
                ei.setDisfrazId(rs.getInt("dsfr_id"));
                ei.setQtyReserved(rs.getInt("qty_reserved"));
                ei.setQtyAssigned(rs.getInt("qty_assigned"));
                lista.add(ei);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public void eliminarPorEvento(int eventoId) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "DELETE FROM event_costumes WHERE event_id = ?");
            st.setInt(1, eventoId);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }
}

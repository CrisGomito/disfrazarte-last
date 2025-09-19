/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOEventoAsignacion;
import com.mycompany.models.EventoAsignacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristian
 */
public class DAOEventoAsignacionesImpl extends Database implements DAOEventoAsignacion {

    @Override
    public void insertar(EventoAsignacion ea) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "INSERT INTO event_assignments (event_costume_id, user_id, lending_id) VALUES (?,?,?)"
            );
            st.setInt(1, ea.getEventCostumeId());
            st.setInt(2, ea.getUserId());
            if (ea.getLendingId() == null) {
                st.setNull(3, java.sql.Types.INTEGER);
            } else {
                st.setInt(3, ea.getLendingId());
            }
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
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM event_assignments WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<EventoAsignacion> listarPorEvento(int eventId) throws Exception {
        List<EventoAsignacion> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT ea.id AS ea_id, ea.event_costume_id, ec.event_id, ec.dsfr_id, d.nombre AS disfraz_nombre, "
                    + "ea.user_id, CONCAT(u.name, ' ', u.last_name_p) AS user_nombre, ea.lending_id, ea.assigned_at "
                    + "FROM event_assignments ea "
                    + "JOIN event_costumes ec ON ea.event_costume_id = ec.id "
                    + "JOIN dsfr d ON ec.dsfr_id = d.id "
                    + "JOIN users u ON ea.user_id = u.id "
                    + "WHERE ec.event_id = ?"
            );
            st.setInt(1, eventId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                EventoAsignacion a = new EventoAsignacion();
                a.setId(rs.getInt("ea_id"));
                a.setEventCostumeId(rs.getInt("event_costume_id"));
                a.setEventId(rs.getInt("event_id"));
                a.setDisfrazId(rs.getInt("dsfr_id"));
                a.setDisfrazNombre(rs.getString("disfraz_nombre"));
                a.setUserId(rs.getInt("user_id"));
                a.setUserNombre(rs.getString("user_nombre"));
                int lending = rs.getInt("lending_id");
                if (rs.wasNull()) {
                    a.setLendingId(null);
                } else {
                    a.setLendingId(lending);
                }
                Timestamp ts = rs.getTimestamp("assigned_at");
                if (ts != null) {
                    a.setAssignedAt(ts.toLocalDateTime());
                }
                lista.add(a);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public List<EventoAsignacion> listarPorUsuario(int userId) throws Exception {
        List<EventoAsignacion> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT ea.id AS ea_id, ea.event_costume_id, ec.event_id, ec.dsfr_id, d.nombre AS disfraz_nombre, "
                    + "ea.user_id, CONCAT(u.name, ' ', u.last_name_p) AS user_nombre, ea.lending_id, ea.assigned_at "
                    + "FROM event_assignments ea "
                    + "JOIN event_costumes ec ON ea.event_costume_id = ec.id "
                    + "JOIN dsfr d ON ec.dsfr_id = d.id "
                    + "JOIN users u ON ea.user_id = u.id "
                    + "WHERE ea.user_id = ?"
            );
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                EventoAsignacion a = new EventoAsignacion();
                a.setId(rs.getInt("ea_id"));
                a.setEventCostumeId(rs.getInt("event_costume_id"));
                a.setEventId(rs.getInt("event_id"));
                a.setDisfrazId(rs.getInt("dsfr_id"));
                a.setDisfrazNombre(rs.getString("disfraz_nombre"));
                a.setUserId(rs.getInt("user_id"));
                a.setUserNombre(rs.getString("user_nombre"));
                int lending = rs.getInt("lending_id");
                if (rs.wasNull()) {
                    a.setLendingId(null);
                } else {
                    a.setLendingId(lending);
                }
                Timestamp ts = rs.getTimestamp("assigned_at");
                if (ts != null) {
                    a.setAssignedAt(ts.toLocalDateTime());
                }
                lista.add(a);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public int contarPorDisfraz(int dsfrId) throws Exception {
        int count = 0;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT COUNT(*) AS cnt FROM event_assignments ea "
                    + "JOIN event_costumes ec ON ea.event_costume_id = ec.id "
                    + "WHERE ec.dsfr_id = ?"
            );
            st.setInt(1, dsfrId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return count;
    }

    @Override
    public List<EventoAsignacion> listarPorEventCostume(int eventCostumeId) throws Exception {
        List<EventoAsignacion> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT ea.id AS ea_id, ea.event_costume_id, ec.event_id, ec.dsfr_id, d.nombre AS disfraz_nombre, "
                    + "ea.user_id, CONCAT(u.name, ' ', u.last_name_p) AS user_nombre, ea.lending_id, ea.assigned_at "
                    + "FROM event_assignments ea "
                    + "JOIN event_costumes ec ON ea.event_costume_id = ec.id "
                    + "JOIN dsfr d ON ec.dsfr_id = d.id "
                    + "JOIN users u ON ea.user_id = u.id "
                    + "WHERE ea.event_costume_id = ?"
            );
            st.setInt(1, eventCostumeId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                EventoAsignacion a = new EventoAsignacion();
                a.setId(rs.getInt("ea_id"));
                a.setEventCostumeId(rs.getInt("event_costume_id"));
                a.setEventId(rs.getInt("event_id"));
                a.setDisfrazId(rs.getInt("dsfr_id"));
                a.setDisfrazNombre(rs.getString("disfraz_nombre"));
                a.setUserId(rs.getInt("user_id"));
                a.setUserNombre(rs.getString("user_nombre"));
                int lending = rs.getInt("lending_id");
                if (rs.wasNull()) {
                    a.setLendingId(null);
                } else {
                    a.setLendingId(lending);
                }
                Timestamp ts = rs.getTimestamp("assigned_at");
                if (ts != null) {
                    a.setAssignedAt(ts.toLocalDateTime());
                }
                lista.add(a);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public boolean existeAsignacion(int eventCostumeId, int userId) throws Exception {
        boolean existe = false;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT COUNT(*) AS cnt FROM event_assignments WHERE event_costume_id = ? AND user_id = ?"
            );
            st.setInt(1, eventCostumeId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                existe = rs.getInt("cnt") > 0;
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return existe;
    }

    public void eliminarPorEventoYDisfraz(int eventId, int disfrazId) throws Exception {
        try {
            this.Conectar();
            String sql = "DELETE ea FROM event_assignments ea "
                    + "JOIN event_costumes ec ON ea.event_costume_id = ec.id "
                    + "WHERE ec.event_id = ? AND ec.dsfr_id = ?";
            PreparedStatement st = this.conexion.prepareStatement(sql);
            st.setInt(1, eventId);
            st.setInt(2, disfrazId);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }
}

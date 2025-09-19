/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOEventos;
import com.mycompany.models.Evento;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cristian
 */
public class DAOEventosImpl extends Database implements DAOEventos {

    @Override
    public void insertar(Evento e) throws Exception {
        try {
            this.Conectar();

            String sql = "INSERT INTO events(name, detalles, event_date, location, notes, status) VALUES(?,?,?,?,?,?)";
            PreparedStatement st = this.conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, e.getTitulo());
            st.setString(2, e.getDetalles());
            st.setDate(3, java.sql.Date.valueOf(e.getFecha()));
            st.setString(4, e.getLocation());
            st.setString(5, e.getNotes());
            st.setString(6, e.getEstado());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                e.setId(rs.getInt(1));
            }
            rs.close();
            st.close();

            PreparedStatement stColor = this.conexion.prepareStatement(
                    "SELECT color FROM event_colors WHERE event_date=?"
            );
            stColor.setDate(1, java.sql.Date.valueOf(e.getFecha()));
            ResultSet rsColor = stColor.executeQuery();

            if (!rsColor.next()) {
                String color = generarColorAleatorioHex();
                PreparedStatement stInsertColor = this.conexion.prepareStatement(
                        "INSERT INTO event_colors(event_date, color) VALUES(?, ?)"
                );
                stInsertColor.setDate(1, java.sql.Date.valueOf(e.getFecha()));
                stInsertColor.setString(2, color);
                stInsertColor.executeUpdate();
                stInsertColor.close();
            }

            rsColor.close();
            stColor.close();

        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(Evento e) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE events SET name=?, detalles=?, event_date=?, location=?, notes=?, status=? WHERE id=?"
            );
            st.setString(1, e.getTitulo());
            st.setString(2, e.getDetalles());
            st.setDate(3, java.sql.Date.valueOf(e.getFecha()));
            st.setString(4, e.getLocation());
            st.setString(5, e.getNotes());
            st.setString(6, e.getEstado());
            st.setInt(7, e.getId());
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        eliminarEvento(id);
    }

    @Override
    public Evento obtenerPorId(int id) throws Exception {
        Evento e = null;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM events WHERE id=?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                e = mapearEvento(rs);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return e;
    }

    @Override
    public List<Evento> listar() throws Exception {
        List<Evento> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM events");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(mapearEvento(rs));
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public List<Evento> listarPorFecha(LocalDate fecha) throws Exception {
        List<Evento> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM events WHERE event_date=?");
            st.setDate(1, java.sql.Date.valueOf(fecha));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(mapearEvento(rs));
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    private Evento mapearEvento(ResultSet rs) throws Exception {
        Evento e = new Evento();
        e.setId(rs.getInt("id"));
        e.setTitulo(rs.getString("name"));
        e.setDetalles(rs.getString("detalles"));
        e.setFecha(rs.getDate("event_date").toLocalDate());
        e.setLocation(rs.getString("location"));
        e.setNotes(rs.getString("notes"));
        e.setEstado(rs.getString("status"));
        e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return e;
    }

    private String generarColorAleatorioHex() {
        Random rnd = new Random();
        int r = rnd.nextInt(156) + 100;
        int g = rnd.nextInt(156) + 100;
        int b = rnd.nextInt(156) + 100;
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public List<Object[]> listarEventosDetalladosPorFecha(LocalDate fecha) throws Exception {
        List<Object[]> lista = new ArrayList<>();
        try {
            this.Conectar();
            String sql = "SELECT e.id AS evento_id, "
                    + "       e.name AS titulo, "
                    + "       e.status AS estado, "
                    + "       GROUP_CONCAT(DISTINCT ec.dsfr_id ORDER BY ec.dsfr_id SEPARATOR ', ') AS disfraz_ids, "
                    + "       GROUP_CONCAT(DISTINCT d.nombre ORDER BY d.nombre SEPARATOR ', ') AS disfraces, "
                    + "       GROUP_CONCAT(DISTINCT (d.available - ec.qty_reserved) ORDER BY d.nombre SEPARATOR ', ') AS disponibles, "
                    + "       e.event_date AS fecha, "
                    + "       COALESCE(GROUP_CONCAT(DISTINCT CONCAT(SUBSTRING_INDEX(u.name, ' ', 1), ' ', u.last_name_p) SEPARATOR ', '), 'No Asignado') AS usuario_asignado "
                    + "FROM events e "
                    + "LEFT JOIN event_costumes ec ON e.id = ec.event_id "
                    + "LEFT JOIN dsfr d ON ec.dsfr_id = d.id "
                    + "LEFT JOIN event_assignments ea ON ec.id = ea.event_costume_id "
                    + "LEFT JOIN users u ON ea.user_id = u.id "
                    + "WHERE e.event_date = ? "
                    + "GROUP BY e.id, e.name, e.status, e.event_date "
                    + "ORDER BY e.created_at DESC";

            PreparedStatement st = this.conexion.prepareStatement(sql);
            st.setDate(1, java.sql.Date.valueOf(fecha));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("evento_id"),
                    rs.getString("titulo"),
                    rs.getString("estado"),
                    rs.getString("disfraz_ids"),
                    rs.getString("disfraces"),
                    rs.getString("disponibles"),
                    rs.getDate("fecha").toString(),
                    rs.getString("usuario_asignado")
                });
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public Evento obtenerDetallesPorId(int id) throws Exception {
        Evento e = null;
        try {
            this.Conectar();
            String sql = "SELECT e.*, ec.color AS color, "
                    + "GROUP_CONCAT(DISTINCT d.nombre ORDER BY d.nombre SEPARATOR ', ') AS disfraces, "
                    + "COALESCE(GROUP_CONCAT(DISTINCT CONCAT(u.name, ' ', u.last_name_p, ' ', u.last_name_m) SEPARATOR ', '), 'No Asignado') AS usuarios "
                    + "FROM events e "
                    + "LEFT JOIN event_costumes ec2 ON e.id = ec2.event_id "
                    + "LEFT JOIN dsfr d ON ec2.dsfr_id = d.id "
                    + "LEFT JOIN event_assignments ea ON ec2.id = ea.event_costume_id "
                    + "LEFT JOIN users u ON ea.user_id = u.id "
                    + "LEFT JOIN event_colors ec ON e.event_date = ec.event_date "
                    + "WHERE e.id = ? "
                    + "GROUP BY e.id";

            PreparedStatement st = this.conexion.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                e = new Evento();
                e.setId(rs.getInt("id"));
                e.setTitulo(rs.getString("name"));
                e.setDetalles(rs.getString("detalles"));
                e.setFecha(rs.getDate("event_date").toLocalDate());
                e.setLocation(rs.getString("location"));
                e.setNotes(rs.getString("notes"));
                e.setEstado(rs.getString("status"));
                e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                e.setColor(rs.getString("color"));

                String disfraces = rs.getString("disfraces");
                e.setDisfracesAsignados(List.of(disfraces.split(", ")));

                String usuarios = rs.getString("usuarios");
                if ("No Asignado".equals(usuarios)) {
                    e.setUsuariosAsignados(new ArrayList<>());
                } else {
                    e.setUsuariosAsignados(List.of(usuarios.split(", ")));
                }
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return e;
    }

    public void asignarUsuariosAEvento(int eventId, List<Integer> userIds) throws Exception {
        try {
            this.Conectar();

            String sqlDisfraces = "SELECT id FROM event_costumes WHERE event_id = ?";
            PreparedStatement stDisfraces = this.conexion.prepareStatement(sqlDisfraces);
            stDisfraces.setInt(1, eventId);
            ResultSet rsDisfraces = stDisfraces.executeQuery();

            List<Integer> eventCostumeIds = new ArrayList<>();
            while (rsDisfraces.next()) {
                eventCostumeIds.add(rsDisfraces.getInt("id"));
            }
            rsDisfraces.close();
            stDisfraces.close();

            if (eventCostumeIds.isEmpty()) {
                throw new Exception("No hay disfraces asignados a este evento, no se pueden asignar usuarios.");
            }

            String sqlInsert = "INSERT INTO event_assignments(event_costume_id, user_id) VALUES(?, ?)";
            PreparedStatement stInsert = this.conexion.prepareStatement(sqlInsert);

            for (Integer ecId : eventCostumeIds) {
                for (Integer userId : userIds) {
                    stInsert.setInt(1, ecId);
                    stInsert.setInt(2, userId);
                    stInsert.addBatch();
                }
            }

            stInsert.executeBatch();
            stInsert.close();

        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    public void eliminarUsuariosAsignados(int eventoId) throws Exception {
        try {
            this.Conectar();

            String sqlDisfraces = "SELECT id FROM event_costumes WHERE event_id = ?";
            PreparedStatement stDisfraces = this.conexion.prepareStatement(sqlDisfraces);
            stDisfraces.setInt(1, eventoId);
            ResultSet rsDisfraces = stDisfraces.executeQuery();

            List<Integer> eventCostumeIds = new ArrayList<>();
            while (rsDisfraces.next()) {
                eventCostumeIds.add(rsDisfraces.getInt("id"));
            }
            rsDisfraces.close();
            stDisfraces.close();

            if (!eventCostumeIds.isEmpty()) {

                String sqlDelete = "DELETE FROM event_assignments WHERE event_costume_id = ?";
                PreparedStatement stDelete = this.conexion.prepareStatement(sqlDelete);

                for (Integer ecId : eventCostumeIds) {
                    stDelete.setInt(1, ecId);
                    stDelete.addBatch();
                }

                stDelete.executeBatch();
                stDelete.close();
            }

        } finally {
            this.Cerrar();
        }
    }

    public void eliminarEvento(int eventId) throws Exception {
        try {
            this.Conectar();

            LocalDate fechaDelEvento = null;
            PreparedStatement stFecha = this.conexion.prepareStatement(
                    "SELECT event_date FROM events WHERE id=?"
            );
            stFecha.setInt(1, eventId);
            ResultSet rsFecha = stFecha.executeQuery();
            if (rsFecha.next()) {
                fechaDelEvento = rsFecha.getDate("event_date").toLocalDate();
            }
            rsFecha.close();
            stFecha.close();

            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM events WHERE id=?");
            st.setInt(1, eventId);
            st.executeUpdate();
            st.close();

            PreparedStatement stCheck = this.conexion.prepareStatement(
                    "SELECT COUNT(*) FROM events WHERE event_date=?"
            );
            stCheck.setDate(1, java.sql.Date.valueOf(fechaDelEvento));
            ResultSet rsCheck = stCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) == 0) {

                PreparedStatement stDelColor = this.conexion.prepareStatement(
                        "DELETE FROM event_colors WHERE event_date=?"
                );
                stDelColor.setDate(1, java.sql.Date.valueOf(fechaDelEvento));
                stDelColor.executeUpdate();
                stDelColor.close();
            }
            rsCheck.close();
            stCheck.close();

        } finally {
            this.Cerrar();
        }
    }

    public String obtenerColorPorFecha(LocalDate fecha) throws Exception {
        String color = null;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT color FROM event_colors WHERE event_date=?"
            );
            st.setDate(1, java.sql.Date.valueOf(fecha));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                color = rs.getString("color");
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return color;
    }

    public void insertarColorFecha(LocalDate fecha, Color color) throws Exception {
        try {
            this.Conectar();

            PreparedStatement stCheck = this.conexion.prepareStatement(
                    "SELECT 1 FROM event_colors WHERE event_date=?"
            );
            stCheck.setDate(1, java.sql.Date.valueOf(fecha));
            ResultSet rsCheck = stCheck.executeQuery();
            boolean existe = rsCheck.next();
            rsCheck.close();
            stCheck.close();

            if (!existe) {
                String colorHex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                PreparedStatement stInsert = this.conexion.prepareStatement(
                        "INSERT INTO event_colors(event_date, color) VALUES(?, ?)"
                );
                stInsert.setDate(1, java.sql.Date.valueOf(fecha));
                stInsert.setString(2, colorHex);
                stInsert.executeUpdate();
                stInsert.close();
            }
        } finally {
            this.Cerrar();
        }
    }
}

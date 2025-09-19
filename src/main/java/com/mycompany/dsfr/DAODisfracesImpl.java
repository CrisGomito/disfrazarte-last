package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.models.Disfraces;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.interfaces.DAODisfraces;
import java.time.LocalDate;

public class DAODisfracesImpl extends Database implements DAODisfraces {

    @Override
    public void registrar(Disfraces dsfr) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "INSERT INTO dsfr(nombre, tematica, description, talla, stock, available, estado, notes) VALUES(?,?,?,?,?,?,?,?)"
            );
            st.setString(1, dsfr.getTitle());
            st.setString(2, dsfr.getTematica());
            st.setString(3, dsfr.getDescription());
            st.setString(4, dsfr.getTalla());
            st.setInt(5, dsfr.getStock());
            st.setInt(6, dsfr.getAvailable());
            st.setString(7, dsfr.getEstado());
            st.setString(8, dsfr.getNotes());
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(Disfraces dsfr) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE dsfr SET nombre=?, tematica=?, description=?, talla=?, stock=?, available=?, estado=?, notes=? WHERE id=?"
            );
            st.setString(1, dsfr.getTitle());
            st.setString(2, dsfr.getTematica());
            st.setString(3, dsfr.getDescription());
            st.setString(4, dsfr.getTalla());
            st.setInt(5, dsfr.getStock());
            st.setInt(6, dsfr.getAvailable());
            st.setString(7, dsfr.getEstado());
            st.setString(8, dsfr.getNotes());
            st.setInt(9, dsfr.getId());
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void eliminar(int dsfrId) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM dsfr WHERE id=?");
            st.setInt(1, dsfrId);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<Disfraces> listar(String title) throws Exception {
        List<Disfraces> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st;
            if (title == null || title.isEmpty()) {
                st = this.conexion.prepareStatement("SELECT * FROM dsfr");
            } else {
                st = this.conexion.prepareStatement("SELECT * FROM dsfr WHERE nombre LIKE ?");
                st.setString(1, "%" + title + "%");
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disfraces d = new Disfraces();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("nombre"));
                d.setTematica(rs.getString("tematica"));
                d.setDescription(rs.getString("description"));
                d.setTalla(rs.getString("talla"));
                d.setStock(rs.getInt("stock"));
                d.setAvailable(rs.getInt("available"));
                d.setEstado(rs.getString("estado"));
                d.setNotes(rs.getString("notes"));
                lista.add(d);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public Disfraces getDsfrById(int dsfrId) throws Exception {
        Disfraces d = null;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM dsfr WHERE id=?");
            st.setInt(1, dsfrId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                d = new Disfraces();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("nombre"));
                d.setTematica(rs.getString("tematica"));
                d.setDescription(rs.getString("description"));
                d.setTalla(rs.getString("talla"));
                d.setStock(rs.getInt("stock"));
                d.setAvailable(rs.getInt("available"));
                d.setEstado(rs.getString("estado"));
                d.setNotes(rs.getString("notes"));
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return d;
    }

    @Override
    public List<Disfraces> listarDisponiblesPorFecha(LocalDate fecha, int eventoId) throws Exception {
        List<Disfraces> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st;

            if (eventoId > 0) {
                st = this.conexion.prepareStatement(
                        "SELECT d.* FROM dsfr d"
                );
            } else {
                st = this.conexion.prepareStatement(
                        "SELECT d.* FROM dsfr d"
                );
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disfraces d = new Disfraces();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("nombre"));
                d.setTematica(rs.getString("tematica"));
                d.setDescription(rs.getString("description"));
                d.setTalla(rs.getString("talla"));
                d.setStock(rs.getInt("stock"));
                d.setAvailable(rs.getInt("available"));
                d.setEstado(rs.getString("estado"));
                d.setNotes(rs.getString("notes"));
                lista.add(d);
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public List<Disfraces> listarConUsuarios(String title) throws Exception {
        List<Disfraces> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st;
            if (title == null || title.isEmpty()) {
                st = this.conexion.prepareStatement(
                        "SELECT d.*, "
                        + "GROUP_CONCAT(CONCAT(u.name, ' ', u.last_name_p) SEPARATOR ', ') AS usuarios "
                        + "FROM dsfr d "
                        + "LEFT JOIN event_costumes ec ON d.id = ec.dsfr_id "
                        + "LEFT JOIN event_assignments ea ON ec.id = ea.event_costume_id "
                        + "LEFT JOIN users u ON ea.user_id = u.id "
                        + "GROUP BY d.id"
                );
            } else {
                st = this.conexion.prepareStatement(
                        "SELECT d.*, "
                        + "GROUP_CONCAT(CONCAT(u.name, ' ', u.last_name_p) SEPARATOR ', ') AS usuarios "
                        + "FROM dsfr d "
                        + "LEFT JOIN event_costumes ec ON d.id = ec.dsfr_id "
                        + "LEFT JOIN event_assignments ea ON ec.id = ea.event_costume_id "
                        + "LEFT JOIN users u ON ea.user_id = u.id "
                        + "WHERE d.nombre LIKE ? "
                        + "GROUP BY d.id"
                );
                st.setString(1, "%" + title + "%");
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disfraces d = new Disfraces();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("nombre"));
                d.setTematica(rs.getString("tematica"));
                d.setDescription(rs.getString("description"));
                d.setTalla(rs.getString("talla"));
                d.setStock(rs.getInt("stock"));
                d.setAvailable(rs.getInt("available"));
                d.setEstado(rs.getString("estado"));
                d.setNotes(rs.getString("notes"));
                d.setUsuariosAsignados(rs.getString("usuarios"));
                lista.add(d);
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public Object[] obtenerDetallesPorId(int disfrazId) throws Exception {
        Object[] detalle = null;
        try {
            this.Conectar();
            String sql = "SELECT d.id, d.nombre, d.tematica, d.description, d.talla, "
                    + "       d.stock, d.available, d.estado, "
                    + "       COALESCE(GROUP_CONCAT(DISTINCT CONCAT(u.name, ' ', u.last_name_p, ' ', u.last_name_m) SEPARATOR ', '), 'No asignado') AS usuarios "
                    + "FROM dsfr d "
                    + "LEFT JOIN user_costumes uc ON d.id = uc.dsfr_id "
                    + "LEFT JOIN users u ON uc.user_id = u.id "
                    + "WHERE d.id = ? "
                    + "GROUP BY d.id, d.nombre, d.tematica, d.description, d.talla, d.stock, d.available, d.estado";

            PreparedStatement st = this.conexion.prepareStatement(sql);
            st.setInt(1, disfrazId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                detalle = new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tematica"),
                    rs.getString("description"),
                    rs.getString("talla"),
                    rs.getInt("stock"),
                    rs.getInt("available"),
                    rs.getString("estado"),
                    rs.getString("usuarios")
                };
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return detalle;
    }

    public List<Disfraces> listarConUsuariosDirectos(String title) throws Exception {
        List<Disfraces> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st;

            if (title == null || title.isEmpty()) {
                st = this.conexion.prepareStatement(
                        "SELECT d.*, "
                        + "COALESCE(GROUP_CONCAT(DISTINCT CONCAT(u.name, ' ', u.last_name_p) SEPARATOR ', '), '-') AS usuarios "
                        + "FROM dsfr d "
                        + "LEFT JOIN user_costumes uc ON d.id = uc.dsfr_id "
                        + "LEFT JOIN users u ON uc.user_id = u.id "
                        + "GROUP BY d.id"
                );
            } else {
                st = this.conexion.prepareStatement(
                        "SELECT d.*, "
                        + "COALESCE(GROUP_CONCAT(DISTINCT CONCAT(u.name, ' ', u.last_name_p) SEPARATOR ', '), '-') AS usuarios "
                        + "FROM dsfr d "
                        + "LEFT JOIN user_costumes uc ON d.id = uc.dsfr_id "
                        + "LEFT JOIN users u ON uc.user_id = u.id "
                        + "WHERE d.nombre LIKE ? "
                        + "GROUP BY d.id"
                );
                st.setString(1, "%" + title + "%");
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disfraces d = new Disfraces();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("nombre"));
                d.setTematica(rs.getString("tematica"));
                d.setDescription(rs.getString("description"));
                d.setTalla(rs.getString("talla"));
                d.setStock(rs.getInt("stock"));
                d.setAvailable(rs.getInt("available"));
                d.setEstado(rs.getString("estado"));
                d.setNotes(rs.getString("notes"));
                d.setUsuariosAsignados(rs.getString("usuarios"));
                lista.add(d);
            }

            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }
}

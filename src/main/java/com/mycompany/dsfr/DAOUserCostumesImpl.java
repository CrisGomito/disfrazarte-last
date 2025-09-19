/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOUserCostumes;
import com.mycompany.models.UserCostume;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristian
 */
public class DAOUserCostumesImpl extends Database implements DAOUserCostumes {

    @Override
    public void registrar(UserCostume uc) throws Exception {
        try {
            this.Conectar();

            PreparedStatement st = this.conexion.prepareStatement(
                    "INSERT INTO user_costumes(user_id, dsfr_id, qty, status) VALUES(?,?,?,?)"
            );
            st.setInt(1, uc.getUserId());
            st.setInt(2, uc.getDsfrId());
            st.setInt(3, uc.getQty());
            st.setString(4, uc.getStatus());
            st.executeUpdate();
            st.close();

            if (uc.getStatus().equalsIgnoreCase("Reservado") || uc.getStatus().equalsIgnoreCase("Asignado") || uc.getStatus().equalsIgnoreCase("Cancelado")) {
                PreparedStatement st2 = this.conexion.prepareStatement(
                        "UPDATE dsfr SET available = available - ? WHERE id = ? AND available >= ?"
                );
                st2.setInt(1, uc.getQty());
                st2.setInt(2, uc.getDsfrId());
                st2.setInt(3, uc.getQty());
                st2.executeUpdate();
                st2.close();
            }

        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(UserCostume uc) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE user_costumes SET status=? WHERE id=?"
            );
            st.setString(1, uc.getStatus());
            st.setInt(2, uc.getId());
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
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM user_costumes WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<UserCostume> listarPorUsuario(int userId) throws Exception {
        List<UserCostume> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT uc.*, d.nombre AS disfraz "
                    + "FROM user_costumes uc "
                    + "INNER JOIN dsfr d ON uc.dsfr_id = d.id "
                    + "WHERE uc.user_id = ?"
            );
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                UserCostume uc = new UserCostume();
                uc.setId(rs.getInt("id"));
                uc.setUserId(rs.getInt("user_id"));
                uc.setDsfrId(rs.getInt("dsfr_id"));
                uc.setQty(rs.getInt("qty"));
                uc.setStatus(rs.getString("status"));
                uc.setDisfrazNombre(rs.getString("disfraz"));
                lista.add(uc);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public void eliminarAsignacion(int userId, int dsfrId, boolean devolverDisponible) throws Exception {
        try {
            this.Conectar();

            PreparedStatement st = this.conexion.prepareStatement(
                    "DELETE FROM user_costumes WHERE user_id=? AND dsfr_id=?"
            );
            st.setInt(1, userId);
            st.setInt(2, dsfrId);
            st.executeUpdate();
            st.close();

            if (devolverDisponible) {
                PreparedStatement st2 = this.conexion.prepareStatement(
                        "UPDATE dsfr SET available = available + 1 WHERE id = ?"
                );
                st2.setInt(1, dsfrId);
                st2.executeUpdate();
                st2.close();
            }

        } finally {
            this.Cerrar();
        }
    }

    public void devolverDisponible(int dsfrId, int qty) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE dsfr SET available = available + ? WHERE id = ?"
            );
            st.setInt(1, qty);
            st.setInt(2, dsfrId);
            st.executeUpdate();
            st.close();
        } finally {
            this.Cerrar();
        }
    }

    public List<UserCostume> listarPorDisfraz(int dsfrId) throws Exception {
        List<UserCostume> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "SELECT uc.*, u.name, u.last_name_p, u.last_name_m, u.email "
                    + "FROM user_costumes uc "
                    + "INNER JOIN users u ON uc.user_id = u.id "
                    + "WHERE uc.dsfr_id = ?"
            );
            st.setInt(1, dsfrId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                UserCostume uc = new UserCostume();
                uc.setId(rs.getInt("id"));
                uc.setUserId(rs.getInt("user_id"));
                uc.setDsfrId(rs.getInt("dsfr_id"));
                uc.setQty(rs.getInt("qty"));
                uc.setStatus(rs.getString("status"));

                uc.setUsuarioNombre(
                        rs.getString("name") + " "
                        + rs.getString("last_name_p") + " "
                        + rs.getString("last_name_m") + " | "
                        + rs.getString("email")
                );

                lista.add(uc);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public void eliminarAsignacionesPorDisfraz(int dsfrId, boolean devolverDisponible) throws Exception {
        try {
            this.Conectar();

            if (devolverDisponible) {
                PreparedStatement st1 = this.conexion.prepareStatement(
                        "UPDATE dsfr SET available = available + "
                        + "(SELECT COUNT(*) FROM user_costumes WHERE dsfr_id=?) "
                        + "WHERE id=?"
                );
                st1.setInt(1, dsfrId);
                st1.setInt(2, dsfrId);
                st1.executeUpdate();
                st1.close();
            }

            PreparedStatement st2 = this.conexion.prepareStatement(
                    "DELETE FROM user_costumes WHERE dsfr_id=?"
            );
            st2.setInt(1, dsfrId);
            st2.executeUpdate();
            st2.close();

        } finally {
            this.Cerrar();
        }
    }
}

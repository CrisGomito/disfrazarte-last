package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOUsers;
import com.mycompany.models.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOUsersImpl extends Database implements DAOUsers {

    @Override
    public void registrar(Users user) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "INSERT INTO users(name, last_name_p, last_name_m, domicilio, tel, email) VALUES(?,?,?,?,?,?);"
            );
            st.setString(1, user.getName());
            st.setString(2, user.getLast_name_p());
            st.setString(3, user.getLast_name_m());
            st.setString(4, user.getDomicilio());
            st.setString(5, user.getTel());
            st.setString(6, user.getEmail());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(Users user) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement(
                    "UPDATE users SET name = ?, last_name_p = ?, last_name_m = ?, domicilio = ?, tel = ?, email = ? WHERE id = ?"
            );
            st.setString(1, user.getName());
            st.setString(2, user.getLast_name_p());
            st.setString(3, user.getLast_name_m());
            st.setString(4, user.getDomicilio());
            st.setString(5, user.getTel());
            st.setString(6, user.getEmail());
            st.setInt(7, user.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void eliminar(int userId) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM users WHERE id = ?;");
            st.setInt(1, userId);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<Users> listar(String name) throws Exception {
        List<Users> lista = null;
        try {
            this.Conectar();
            String Query = name.isEmpty() ? "SELECT * FROM users;" : "SELECT * FROM users WHERE name LIKE '%" + name + "%';";
            PreparedStatement st = this.conexion.prepareStatement(Query);

            lista = new ArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setLast_name_p(rs.getString("last_name_p"));
                user.setLast_name_m(rs.getString("last_name_m"));
                user.setDomicilio(rs.getString("domicilio"));
                user.setTel(rs.getString("tel"));
                user.setEmail(rs.getString("email"));
                user.setSanctions(rs.getInt("sanctions"));
                user.setSanc_money(rs.getInt("sanc_money"));
                lista.add(user);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public Users getUserById(int userId) throws Exception {
        Users user = null;

        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM users WHERE id = ? LIMIT 1;");
            st.setInt(1, userId);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                user = new Users();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setLast_name_p(rs.getString("last_name_p"));
                user.setLast_name_m(rs.getString("last_name_m"));
                user.setDomicilio(rs.getString("domicilio"));
                user.setTel(rs.getString("tel"));
                user.setEmail(rs.getString("email"));
                user.setSanctions(rs.getInt("sanctions"));
                user.setSanc_money(rs.getInt("sanc_money"));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return user;
    }

    @Override
    public void sancionar(Users user) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("UPDATE users SET sanctions = ?, sanc_money = ? WHERE id = ?");
            st.setInt(1, user.getSanctions());
            st.setInt(2, user.getSanc_money());
            st.setInt(3, user.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    public List<String> listarNombresUsuarios() throws Exception {
        List<String> lista = new ArrayList<>();
        try {
            this.Conectar();
            String sql = "SELECT name, last_name_p, last_name_m FROM users ORDER BY name";
            PreparedStatement st = this.conexion.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String nombreCompleto = rs.getString("name") + " "
                        + rs.getString("last_name_p") + " "
                        + rs.getString("last_name_m");
                lista.add(nombreCompleto.trim());
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public int getUserIdByNombreCompleto(String nombreCompleto) throws Exception {
        int userId = -1;

        try {
            this.Conectar();

            String[] partes = nombreCompleto.trim().split(" ");
            if (partes.length < 3) {
                throw new Exception("El nombre completo no tiene el formato esperado (mínimo: Nombre ApellidoP ApellidoM).");
            }

            String lastNameM = partes[partes.length - 1];
            String lastNameP = partes[partes.length - 2];
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 0; i < partes.length - 2; i++) {
                nameBuilder.append(partes[i]);
                if (i < partes.length - 3) {
                    nameBuilder.append(" ");
                }
            }
            String name = nameBuilder.toString();

            String sql = "SELECT id FROM users WHERE name = ? AND last_name_p = ? AND last_name_m = ? LIMIT 1";
            PreparedStatement st = this.conexion.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, lastNameP);
            st.setString(3, lastNameM);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }

        return userId;
    }

    public List<Users> listarConDisfraces(String name) throws Exception {
        List<Users> lista = new ArrayList<>();
        try {
            this.Conectar();
            PreparedStatement st;
            if (name == null || name.isEmpty()) {
                st = this.conexion.prepareStatement(
                        "SELECT u.*, "
                        + "GROUP_CONCAT(DISTINCT d.nombre SEPARATOR ', ') AS disfraces_asignados "
                        + "FROM users u "
                        + "LEFT JOIN user_costumes uc ON u.id = uc.user_id "
                        + "LEFT JOIN dsfr d ON uc.dsfr_id = d.id "
                        + "GROUP BY u.id"
                );
            } else {
                st = this.conexion.prepareStatement(
                        "SELECT u.*, "
                        + "GROUP_CONCAT(DISTINCT d.nombre SEPARATOR ', ') AS disfraces_asignados "
                        + "FROM users u "
                        + "LEFT JOIN user_costumes uc ON u.id = uc.user_id "
                        + "LEFT JOIN dsfr d ON uc.dsfr_id = d.id "
                        + "WHERE u.name LIKE ? "
                        + "GROUP BY u.id"
                );
                st.setString(1, "%" + name + "%");
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Users u = new Users();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setLast_name_p(rs.getString("last_name_p"));
                u.setLast_name_m(rs.getString("last_name_m"));
                u.setDomicilio(rs.getString("domicilio"));
                u.setTel(rs.getString("tel"));
                u.setEmail(rs.getString("email"));
                u.setSanctions(rs.getInt("sanctions"));
                u.setSanc_money(rs.getInt("sanc_money"));
                String disfraces = rs.getString("disfraces_asignados");
                u.setDisfracesAsignados((disfraces == null || disfraces.trim().isEmpty()) ? "-" : disfraces);
                lista.add(u);
            }
            rs.close();
            st.close();
        } finally {
            this.Cerrar();
        }
        return lista;
    }
}

package com.mycompany.dsfr;

import com.mycompany.db.Database;
import com.mycompany.interfaces.DAOLendings;
import com.mycompany.models.Disfraces;
import com.mycompany.models.Lendings;
import com.mycompany.models.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOLendingsImpl extends Database implements DAOLendings {

    @Override
    public void registrar(Lendings lending) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("INSERT INTO lendings(user_id, dsfr_id, date_out) VALUES(?,?,?);");
            st.setInt(1, lending.getUser_id());
            st.setInt(2, lending.getDsfr_id());
            st.setString(3, lending.getDate_out());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public void modificar(Lendings lending) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("UPDATE lendings SET user_id = ?, dsfr_id = ?, date_out = ?, date_return = ? WHERE id = ?");
            st.setInt(1, lending.getUser_id());
            st.setInt(2, lending.getDsfr_id());
            st.setString(3, lending.getDate_out());
            st.setString(4, lending.getDate_return());
            st.setInt(5, lending.getId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public Lendings getLending(Users user, Disfraces dsfr) throws Exception {
        Lendings lending = null;

        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM lendings WHERE user_id = ? AND dsfr_id = ? AND date_return IS NULL ORDER BY id DESC LIMIT 1");
            st.setInt(1, user.getId());
            st.setInt(2, dsfr.getId());

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lending = new Lendings();
                lending.setId(rs.getInt("id"));
                lending.setUser_id(rs.getInt("user_id"));
                lending.setDsfr_id(rs.getInt("dsfr_id"));
                lending.setDate_out(rs.getString("date_out"));
                lending.setDate_return(rs.getString("date_return"));
            }

            st.close();
            rs.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }

        return lending;
    }

    @Override
    public List<Lendings> listar() throws Exception {
        List<Lendings> lista = null;
        try {
            this.Conectar();
            PreparedStatement st = this.conexion.prepareStatement("SELECT * FROM lendings ORDER BY id DESC");

            lista = new ArrayList();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Lendings lending = new Lendings();
                lending.setId(rs.getInt("id"));
                lending.setUser_id(rs.getInt("user_id"));
                lending.setDsfr_id(rs.getInt("dsfr_id"));
                lending.setDate_out(rs.getString("date_out"));
                lending.setDate_return(rs.getString("date_return"));
                lista.add(lending);
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
}

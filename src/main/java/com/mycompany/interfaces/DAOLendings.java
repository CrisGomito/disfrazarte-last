package com.mycompany.interfaces;

import com.mycompany.models.Disfraces;
import com.mycompany.models.Lendings;
import com.mycompany.models.Users;
import java.util.List;

public interface DAOLendings {

    public void registrar(Lendings lending) throws Exception;

    public void modificar(Lendings lending) throws Exception;

    public Lendings getLending(Users user, Disfraces dsfr) throws Exception;

    public List<Lendings> listar() throws Exception;
}

package com.mycompany.interfaces;

import com.mycompany.models.Disfraces;
import java.time.LocalDate;
import java.util.List;

public interface DAODisfraces {

    public void registrar(Disfraces dsfr) throws Exception;

    public void modificar(Disfraces dsfr) throws Exception;

    public void eliminar(int dsfrId) throws Exception;

    public List<Disfraces> listar(String title) throws Exception;

    public Disfraces getDsfrById(int dsfrId) throws Exception;

    List<Disfraces> listarDisponiblesPorFecha(LocalDate fecha, int eventoId) throws Exception;
;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.interfaces;

import com.mycompany.models.UserCostume;
import java.util.List;

/**
 *
 * @author cristian
 */
public interface DAOUserCostumes {

    void registrar(UserCostume uc) throws Exception;

    void modificar(UserCostume uc) throws Exception;

    void eliminar(int id) throws Exception;

    List<UserCostume> listarPorUsuario(int userId) throws Exception;
}

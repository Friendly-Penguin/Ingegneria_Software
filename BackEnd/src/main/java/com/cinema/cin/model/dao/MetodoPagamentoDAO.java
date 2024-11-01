package com.cinema.cin.model.dao;


import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.MetodoPagamento;
import com.cinema.cin.model.mo.User;

import java.util.List;

public interface MetodoPagamentoDAO {

    public MetodoPagamento create(
            String NumCarta,
            String DataScad,
            String CVV,
            String Titolare,
            User usr
    ) throws DuplicatedObjectException;

    public void update(MetodoPagamento mtd) throws DuplicatedObjectException;

    public void delete(MetodoPagamento mtd) throws DuplicatedObjectException;

    public List<MetodoPagamento> findByUser(User user);


}

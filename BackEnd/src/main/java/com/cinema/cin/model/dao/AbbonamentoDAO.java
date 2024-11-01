package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Abbonamento;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface AbbonamentoDAO {

    public Abbonamento create(

            String numAccessi,
            Float prezzo,
            User user,
            Carrello carrello
            ) throws DuplicatedObjectException;

    public void update(Abbonamento abb) throws DuplicatedObjectException;

    public void delete(Abbonamento abb) throws DuplicatedObjectException;

    public Abbonamento findAbbByID(Long abbonamentoID);

    public Abbonamento findAbbByUser(User user);

    public Abbonamento getAbbonamentoByCartID(Long carrello_id);

    public void acquistaAbbonamento(Long carrelloID, Long abbonamentoID) throws DuplicatedObjectException;




}

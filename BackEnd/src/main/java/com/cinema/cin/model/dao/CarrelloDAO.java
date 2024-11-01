package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.User;


import java.util.List;

public interface CarrelloDAO {

    public Carrello create(User user
                                    ) throws DuplicatedObjectException;

    public Carrello create2(Carrello carrello) throws DuplicatedObjectException;

    public void update(Carrello carrello);

    public void delete(Carrello carrello);

    public Carrello findByUserEmail(String email);

    public Integer findCartItemsByCartID(Long carrello_id);

    public Carrello findByID(Long carrello_id);

    public Carrello findLoggedCartID();

    public void rimuoviBigliettoByID(String articoloID, String carrelloID) throws DuplicatedObjectException;


    public Integer findCountOfTicketsByCartID(Long carrello_id);


}

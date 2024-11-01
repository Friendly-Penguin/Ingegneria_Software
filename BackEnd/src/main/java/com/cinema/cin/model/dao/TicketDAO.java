package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.Proiezione;
import com.cinema.cin.model.mo.Ticket;
import com.cinema.cin.model.mo.User;

import java.util.List;

public interface TicketDAO {

    public Ticket create(
            Carrello carrello,
            User user,
            Proiezione proiezione,
            String n_riga,
            String n_colonna
    );

    public void update(Ticket ticket);

    public void delete(Ticket ticket);

    public Ticket getTicketByTicketID(Long ticketID);

    public List<Ticket> getTicketByProiezione(Proiezione proiezione);

    public List<Ticket> getTicketByUserEmail(String email);

    public List<Ticket> getTicketByCarrelloID(Long carrello_id);

    public void acquistaTickets(String carrelloID, String ticket_id) throws DuplicatedObjectException;

    public void createMod(User user,Proiezione proiezione, String n_riga, String n_colonna);




}


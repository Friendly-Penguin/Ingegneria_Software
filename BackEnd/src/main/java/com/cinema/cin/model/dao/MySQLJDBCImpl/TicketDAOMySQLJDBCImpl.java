package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.TicketDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOMySQLJDBCImpl implements TicketDAO {

    Connection conn;

    public TicketDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Ticket create(Carrello carrello,User user,Proiezione proiezione, String n_riga, String n_colonna) {

        PreparedStatement ps;
        Ticket ticket = new Ticket();
        ticket.setUsr(user);
        ticket.setCart(carrello);
        ticket.setProiezione(proiezione);
        ticket.setN_riga(n_riga);
        ticket.setN_colonna(n_colonna);


        try {

            String sql = "INSERT INTO biglietto (n_colonna, n_riga, proiezione,comprato,deleted,utente,carrello_id) VALUES (?,?,?,'N','N',?,?)";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, ticket.getN_colonna());
            ps.setString(i++, ticket.getN_riga());
            ps.setLong(i++, ticket.getProiezione().getProie_id());
            ps.setString(i++, ticket.getUsr().getEmail());
            ps.setLong(i++, ticket.getCart().getCarrello_id());
            try {
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);

        }

        return ticket;

    }

    @Override
    public void createMod(User user,Proiezione proiezione, String n_riga, String n_colonna) {

        PreparedStatement ps;
        Ticket ticket = new Ticket();
        ticket.setUsr(user);
        ticket.setProiezione(proiezione);
        ticket.setN_riga(n_riga);
        ticket.setN_colonna(n_colonna);


        try {

            String sql = "INSERT INTO biglietto (n_colonna, n_riga, proiezione,comprato,deleted,utente) VALUES (?,?,?,'Y','N',?)";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, ticket.getN_colonna());
            ps.setString(i++, ticket.getN_riga());
            ps.setLong(i++, ticket.getProiezione().getProie_id());
            ps.setString(i++, ticket.getUsr().getEmail());

            try {
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);

        }

    }

    @Override
    public void update(Ticket ticket) {

    }

    @Override
    public void delete(Ticket ticket) {

        PreparedStatement ps;

        try {

            String sql = "UPDATE biglietto SET deleted = 'Y' WHERE biglietto.id = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, ticket.getTicket_id());

            ps.executeUpdate();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Ticket getTicketByTicketID(Long ticketID) {

        PreparedStatement ps;
        Ticket ticket = null;


        try{

            String sql ="SELECT * FROM biglietto WHERE id = ? and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, ticketID);

            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                ticket = read(resultSet);
            }else throw new RuntimeException();
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  ticket;
    }

    public List<Ticket> getTicketByProiezione(Proiezione proiezione) {

        PreparedStatement ps;
        List<Ticket> tickets = new ArrayList<Ticket>();
        Ticket ticket = null;

        try{

            String sql = "SELECT * FROM biglietto WHERE proiezione = ? and deleted='N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,proiezione.getProie_id());
            ResultSet resultSet = null;
            resultSet = ps.executeQuery();


            try {
                while(resultSet.next()){

                    ticket = read(resultSet);
                    tickets.add(ticket);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return tickets;



    }

    public List<Ticket> getTicketByUserEmail(String email) {

        PreparedStatement ps;
        List<Ticket> tickets = new ArrayList<Ticket>();
        Ticket ticket = null;

        try{

            String sql = "SELECT * FROM biglietto WHERE utente = ? and biglietto.deleted='N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1,email);
            ResultSet resultSet = null;
            resultSet = ps.executeQuery();


            try {
                while(resultSet.next()){

                    ticket = read(resultSet);
                    tickets.add(ticket);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return tickets;



    }

    @Override
    public List<Ticket> getTicketByCarrelloID(Long carrello_id) {

        PreparedStatement ps;
        Ticket ticket;
        List<Ticket> tickets = new ArrayList<Ticket>();

        try{

            String sql ="SELECT biglietto.id, biglietto.n_colonna, biglietto.n_riga, biglietto.proiezione, biglietto.comprato FROM biglietto JOIN carrello on biglietto.carrello_id = carrello.carrello_id WHERE biglietto.carrello_id = ? and biglietto.deleted = 'N' ORDER BY biglietto.id ASC";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, carrello_id);

            ResultSet resultSet = null;
            try {
                //ResultSet resultSet = ps.executeQuery();
                resultSet = ps.executeQuery();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            while(resultSet.next()){
                ticket = read(resultSet);
                tickets.add(ticket);
            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return tickets;





    }

    public void acquistaTickets(String carrelloID, String ticketID) throws DuplicatedObjectException{

        PreparedStatement ps;


        try {

            String sql = "Select * from carrello natural join biglietto where carrello_id = ? and carrello.deleted = 'N' and biglietto.deleted = 'N' and biglietto.id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, Long.parseLong(carrelloID));
            ps.setLong(i, Long.parseLong(ticketID));

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (!exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di acquisto di un articolo non presente nel carrello");

            } else {

                sql = "UPDATE biglietto SET comprato = 'Y', carrello_id = NULL WHERE biglietto.id = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, Long.parseLong(ticketID));

                ps.executeUpdate();
                ps.close();

            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);

        }
    }



Ticket read(ResultSet rs) {

    Ticket ticket = new Ticket();

    User usr = new User();
    Proiezione proiezione = new Proiezione();
    Carrello carrello = new Carrello();

    ticket.setUsr(usr);
    ticket.setProiezione(proiezione);
    ticket.setCart(carrello);

    try {
        ticket.setTicket_id(rs.getLong("id"));
    } catch (SQLException sqle) {
    }
    try {
        ticket.setN_colonna(rs.getString("n_colonna"));
    } catch (SQLException sqle) {
    }
    try {
        ticket.setN_riga(rs.getString("n_riga"));
    } catch (SQLException sqle) {
    }
    try {
        ticket.getProiezione().setProie_id(rs.getLong("proiezione"));
    } catch (SQLException sqle) {
    }
    try {
        ticket.setComprato(rs.getBoolean("comprato"));
    } catch (SQLException sqle) {
    }
    try {
        ticket.setDeleted(rs.getString("deleted").equals('N'));
    } catch (SQLException sqle) {
    }
    try {
        ticket.getUsr().setEmail(rs.getString("utente"));
    } catch (SQLException sqle) {
    }
    try {

        ticket.getCart().setCarrello_id(rs.getLong("carrello_id"));
    } catch (SQLException sqle) {
    }


    return ticket;
}


}

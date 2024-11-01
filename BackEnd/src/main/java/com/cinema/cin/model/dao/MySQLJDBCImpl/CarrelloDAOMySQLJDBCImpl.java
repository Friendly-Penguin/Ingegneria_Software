package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.CarrelloDAO;
import com.cinema.cin.model.dao.UserDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.Review;
import com.cinema.cin.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarrelloDAOMySQLJDBCImpl implements CarrelloDAO {

    Connection conn;

    public CarrelloDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}


    @Override
    public Carrello create(User user) {

        PreparedStatement ps;
        Carrello carrello = null;




        try{

               String  sql ="INSERT INTO carrello (utente, deleted) VALUES (?,'N')";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, user.getEmail());

            try {
                ps.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }

            try {

                sql = "SELECT * FROM carrello WHERE utente = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, user.getEmail());

                ResultSet resultSet = ps.executeQuery();

                if(resultSet.next()){

                    carrello =read(resultSet);

                }else {

                    throw new RuntimeException();

                }
                resultSet.close();
                ps.close();

            }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

        }catch (SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);

        } catch (NullPointerException e) {
            throw new NullPointerException();

        } catch (Exception e) {
            throw new RuntimeException();
        }

        return carrello;

    }

    @Override
    public Carrello create2(Carrello carrello) throws DuplicatedObjectException {
        return null;
    }

    @Override
    public void update(Carrello carrello) {


    }

    @Override
    public void delete(Carrello carrello) {

    }

    public Carrello findByUserEmail(String email){

        Carrello carrello = new Carrello();
        PreparedStatement ps;

        try {

            String sql
                    = "SELECT * FROM carrello JOIN utente ON carrello.utente = utente.email WHERE carrello.utente = ? and carrello.deleted = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                carrello = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carrello;



    }

    public Carrello findByID(Long carrello_id){

        Carrello carrello = new Carrello();
        PreparedStatement ps;

        try {

            String sql
                    = "SELECT * FROM carrello  WHERE carrello.carrello_id = ? and deleted = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, carrello_id);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                carrello = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carrello;
    }

    @Override
    public Integer findCartItemsByCartID(Long carrello_id) {

        Integer items = 0;
        PreparedStatement ps;

        try {

            String sql = "SELECT COUNT(*) FROM ( SELECT carrello_id FROM carrello NATURAL JOIN biglietto WHERE biglietto.deleted = 'N' UNION ALL SELECT carrello_id FROM carrello NATURAL JOIN abbonamento WHERE abbonamento.deleted = 'N') AS combined Where carrello_id = ?";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, carrello_id);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                items = resultSet.getInt("count");
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;



    }

    @Override
    public Integer findCountOfTicketsByCartID(Long carrello_id) {

        Integer items = 0;
        PreparedStatement ps;

        try {

            String sql = "SELECT COUNT(*) FROM carrello NATURAL JOIN biglietto WHERE carrello.carrello_id = ? and biglietto.deleted = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, carrello_id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                items = resultSet.getInt("count");
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;



    }

    public Carrello findLoggedCartID(){return null;}

    @Override
    public void rimuoviBigliettoByID(String articoloID, String carrelloID) throws DuplicatedObjectException {

        PreparedStatement ps;


        try {

            String sql = "Select * from carrello natural join biglietto where carrello_id = ? and carrello.deleted = 'N' and biglietto.deleted = 'N' and biglietto.id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, Long.parseLong(carrelloID));
            ps.setLong(i, Long.parseLong(articoloID));

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (!exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di update di un articolo non presente nel carrello");

            } else {

                sql = "UPDATE biglietto SET deleted = 'Y', carrello_id = NULL WHERE biglietto.id = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, Long.parseLong(articoloID));

                ps.executeUpdate();
                ps.close();

            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);

        }
    }



    Carrello read(ResultSet rs) {

        Carrello carrello = new Carrello();
        User user =  new User();
        carrello.setUser(user);

        try {
            carrello.setCarrello_id(rs.getLong("carrello_id"));
        } catch (SQLException sqle) {
        }
        try {
            carrello.getUser().setEmail(rs.getString("utente"));
        } catch (SQLException sqle) {
        }
        try {
            carrello.setDeleted(rs.getString("deleted").equals("N"));
        } catch (SQLException sqle) {
        }

        return carrello;
    }
}

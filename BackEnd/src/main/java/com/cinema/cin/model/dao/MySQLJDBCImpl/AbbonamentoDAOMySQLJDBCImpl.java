package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.AbbonamentoDAO;
import com.cinema.cin.model.dao.CarrelloDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Abbonamento;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbbonamentoDAOMySQLJDBCImpl implements AbbonamentoDAO {


    Connection conn;

    public AbbonamentoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Abbonamento create(String numAccessi,
                              Float prezzo,
                              User user,
                              Carrello carrello) throws DuplicatedObjectException {

        PreparedStatement ps;
        Abbonamento abb = new Abbonamento();
        abb.setNumAccessi(numAccessi);
        abb.setPrezzo(prezzo);
        abb.setUser(user);
        abb.setCart(carrello);

        try{

            String sql ="SELECT abbonamento_id FROM abbonamento WHERE abbonamento.utente = ? and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setString(i,abb.getUser().getEmail());
            ResultSet resultSet = null;
            try {
                 resultSet = ps.executeQuery();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            boolean exist;
            exist = resultSet.next();
            resultSet.close();
            if(exist){
                throw new DuplicatedObjectException("AbbonamentoDAOImpl.create: Tentativo di creazione di un abbonamento già presente!");
            }else {

                sql
                        = "INSERT INTO abbonamento" +
                        "(n_accessi," +
                        "deleted," +
                        "utente," +
                        "carrello_id," +
                        "prezzo)" +
                        "VALUES (?,'N',?,?,?)";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, abb.getNumAccessi());
                ps.setString(i++, abb.getUser().getEmail());
                ps.setLong(i++, abb.getCart().getCarrello_id());
                ps.setFloat(i++, abb.getPrezzo());
                ps.executeUpdate();
            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return abb;
    }

    @Override
    public void update(Abbonamento abb) throws DuplicatedObjectException {

        PreparedStatement ps;

        try{

            String sql ="SELECT * FROM abbonamento WHERE abbonamento_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,abb.getAbbonamentoId());

            ResultSet resultSet = ps.executeQuery();
            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(!exist){
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.update: tentativo di update di un abbonamento non esistente");
            }else{

                sql = "UPDATE abbonamento SET n_accessi = ? WHERE abbonamento_id = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++,abb.getNumAccessi());
                ps.setLong(i,abb.getAbbonamentoId());
                ps.executeUpdate();
                ps.close();

            }


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void delete(Abbonamento abb) throws DuplicatedObjectException {

        PreparedStatement ps;

        try {


            String sql = "Select * from abbonamento WHERE deleted = 'N' and abbonamento_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i, abb.getAbbonamentoId());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (!exist) {

                throw new DuplicatedObjectException("ContactDAOJDBCImpl.delete: tentativo di update di un articolo non presente nel carrello");

            } else {


                sql = "UPDATE abbonamento SET deleted = 'Y', carrello_id = NULL WHERE abbonamento_id = ?";

                ps = conn.prepareStatement(sql);
                ps.setLong(1, abb.getAbbonamentoId());
                ps.executeUpdate();
                ps.close();

            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);

        }
    }

    @Override
    public Abbonamento findAbbByID(Long abbonamentoID) {

        PreparedStatement ps;
        Abbonamento abb = null;

        try{

            String sql ="SELECT * FROM abbonamento WHERE abbonamento_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,abbonamentoID);
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();

                if(resultSet.next()){
                    abb = read(resultSet);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return abb;
    }


    @Override
    public Abbonamento findAbbByUser(User user) {

        PreparedStatement ps;
        Abbonamento abb = null;

        try{

            String sql ="SELECT * FROM abbonamento WHERE utente = ? AND carrello_id IS NULL and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getEmail());
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();

            if(resultSet.next()){
                abb = read(resultSet);
            }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return abb;
    }

    public Abbonamento getAbbonamentoByCartID(Long carrello_id){

        PreparedStatement ps;
        Abbonamento abbonamento = null;

        try{

            String sql ="SELECT abbonamento.abbonamento_id, abbonamento.n_accessi, abbonamento.prezzo, abbonamento.utente, abbonamento.carrello_id FROM abbonamento NATURAL JOIN carrello  WHERE abbonamento.carrello_id = ? and abbonamento.deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,carrello_id);
            ResultSet resultSet = null;
            try{
                resultSet = ps.executeQuery();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            if (resultSet.next()) {
                    abbonamento = read(resultSet);
                }

            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return abbonamento;






    }

    public void acquistaAbbonamento(Long carrelloID, Long abbonamentoID) throws DuplicatedObjectException{

        PreparedStatement ps;


        try {

            String sql = "Select * from abbonamento natural join carrello where carrello_id = ? and carrello.deleted = 'N' and abbonamento.deleted = 'N' and abbonamento.abbonamento_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, carrelloID);
            ps.setLong(i, abbonamentoID);

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (!exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di acquisto di un abbonamento non presente nel carrello");

            } else {

                sql = "UPDATE abbonamento SET carrello_id = NULL WHERE abbonamento_id = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, abbonamentoID);
                try {
                    ps.executeUpdate();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                ps.close();

            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);

        }
    }

    Abbonamento read(ResultSet rs){

        Abbonamento abb = new Abbonamento();
        User usr = new User();
        Carrello carrello = new Carrello();
        abb.setCart(carrello);
        abb.setUser(usr);

        try{
            abb.setAbbonamentoId(rs.getLong("abbonamento_id"));
        }catch (SQLException ex){

        }
        try{
            abb.getCart().setCarrello_id(rs.getLong("carrello_id"));
        }catch (SQLException ex){

        }
        try{
            abb.setPrezzo(rs.getFloat("prezzo"));
        }catch (SQLException ex){

        }
        try{
            abb.setNumAccessi(rs.getString("n_accessi"));
        }catch (SQLException ex){

        }
        try{
            abb.getUser().setEmail(rs.getString("utente"));
        }catch (SQLException ex){

        }
        try {
            abb.setDeleted(rs.getString("deleted").equals("N"));
        } catch (SQLException ex) {

        }
        return abb;
    }

}

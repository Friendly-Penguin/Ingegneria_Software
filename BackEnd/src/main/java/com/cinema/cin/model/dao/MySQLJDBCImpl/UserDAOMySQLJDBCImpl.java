package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.exception.*;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.cinema.cin.model.dao.UserDAO;
import com.cinema.cin.model.mo.Abbonamento;
import com.cinema.cin.model.mo.User;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User create(String email, String nome, String cognome, String password, String matricola, String token_ID, String confermaEmail) throws DuplicatedObjectException{

        PreparedStatement ps;
        User usr = new User();
        usr.setEmail(email);
        usr.setNome(nome);
        usr.setCognome(cognome);
        usr.setPassword(password);
        usr.setMatricola(matricola);
        usr.setToken_ID(token_ID);


        try{

            String sql
                    = "SELECT email FROM utente WHERE cancellato = 'N' AND email = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, usr.getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(exist){
                throw new DuplicatedObjectException("UserDAOImpl.create: Tentativo di inserimento di un user già esistente.");

            }else if(matricola == "null"){


                sql ="INSERT INTO utente (nome,cognome,password,token_id,email) VALUES (?,?,?,?,?)";

                ps = conn.prepareStatement(sql);
                i = 1;

                ps.setString(i++, usr.getNome());
                ps.setString(i++, usr.getCognome());
                ps.setString(i++, usr.getPassword());
                ps.setString(i++, usr.getToken_ID());
                ps.setString(i, usr.getEmail());
                ps.executeUpdate();

            }else {

                sql ="INSERT INTO utente (nome,cognome,password,matricola,token_id,email) VALUES (?,?,?,?,?,?)";

                ps = conn.prepareStatement(sql);
                i = 1;

                ps.setString(i++, usr.getNome());
                ps.setString(i++, usr.getCognome());
                ps.setString(i++, usr.getPassword());
                ps.setString(i++, usr.getMatricola());
                ps.setString(i++, usr.getToken_ID());
                ps.setString(i++, usr.getEmail());
                ps.executeUpdate();

            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
            return usr;
    }

    @Override
    public void update(User usr) throws DuplicatedObjectException {

        PreparedStatement ps;

        try{

            String sql
                    = "SELECT email FROM utente" +
                    "WHERE cancellato = 'N'" +
                    "AND"+
                    "email <> ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i,usr.getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(!exist){
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di update di un utente non esistente");
            }else {
                sql
                        = "UPDATE utente SET"+
                        "nome = ?," +
                        "cognome = ?," +
                        "password = ?," +
                        "matricola = ?," +
                        "WHERE utente.email = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, usr.getNome());
                ps.setString(i++, usr.getCognome());
                ps.setString(i++, usr.getPassword());
                ps.setString(i++, usr.getMatricola());
                ps.setString(i,usr.getEmail());
                ps.executeUpdate();
                ps.close();


            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void delete(User usr) {

        PreparedStatement ps;

        try{

            String sql
                    = "UPDATE utente" +
                    "SET utente.cancellato = 'Y'" +
                    "WHERE" +
                    "utente.email = ?";

            ps= conn.prepareStatement(sql);
            ps.setString(1, usr.getEmail());
            ps.executeUpdate();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public User findLoggedUser() {
        return null;
    }

    @Override
    public User findByUserEmail(String email) {

        PreparedStatement ps;
        User user = null;

        try {

            String sql
                    = "SELECT *  FROM utente WHERE email = ? and cancellato = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;

    }

    @Override
    public boolean confermaAccount(User usr, String str) {

        boolean verificato = false;
        PreparedStatement ps;
        String token_DB = "";

        try {

            String sql = "SELECT token_id FROM utente WHERE email = ? and cancellato = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setString(1, usr.getEmail());

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                try {
                    token_DB = resultSet.getString("token_id");
                } catch (SQLException sqle) {
                    throw new RuntimeException(sqle);
                }
            }
            resultSet.close();
            ps.close();

            if (token_DB.equals(str)){

                sql = "UPDATE utente SET conferma_email = 'true' WHERE utente.email = ?";

                ps = conn.prepareStatement(sql);
                int i = 1;
                ps.setString(i++, usr.getEmail());
                try {
                    ps.executeUpdate();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                ps.close();
                verificato = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return verificato;
    }


    User read(ResultSet rs) {

        User user = new User();
        Abbonamento abbonamento = new Abbonamento();
        user.setAbb(abbonamento);

        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException sqle) {
        }
        try {
            user.setNome(rs.getString("nome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCognome(rs.getString("cognome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getString("cancellato").equals("N"));
        } catch (SQLException sqle) {
        }
        try {
            user.setMatricola(rs.getString("matricola"));
        } catch (SQLException sqle) {
        }
        try {
            user.setNumero_tessera(rs.getString("numero_tessera"));
        } catch (SQLException sqle) {
        }
        try {
            user.setConfermaEmail(rs.getString("conferma_email"));
        } catch (SQLException sqle) {
        }
        try {
            user.getAbb().setAbbonamentoId(rs.getLong("abbonamento_id"));
        } catch (SQLException sqle) {
        }
        try {
            user.getAbb().setNumAccessi(rs.getString("n_accessi"));
        } catch (SQLException sqle) {
        }
        return user;
    }

}


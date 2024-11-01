package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.ReviewDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Review;
import com.cinema.cin.model.mo.User;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOMySQLJDBCImpl implements ReviewDAO {

    Connection conn;

    public ReviewDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}


    @Override
    public Review create(User usr, Film film, String data, String voto, String titolo, String testo) {


        PreparedStatement ps;
        Review rew = new Review();
        rew.setUsr(usr);
        rew.setFilm(film);
        rew.setData(data);
        rew.setVoto(voto);
        rew.setTitolo(titolo);
        rew.setTesto(testo);


        try{

            String sql = "SELECT recensione_id FROM recensione WHERE deleted = 'N' AND utente = ? AND film = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, usr.getEmail());
            ps.setLong(i,film.getFilmID());

            ResultSet resultSet = ps.executeQuery();

            boolean exist = false;
            exist = resultSet.next();
            resultSet.close();

            if(exist){

                throw new NullPointerException();

            }else

                sql ="INSERT INTO recensione (data, voto, testo, film, deleted, titolo, utente) VALUES (?,?,?,?,'N',?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, rew.getData());
            ps.setString(i++, rew.getVoto());
            ps.setString(i++, rew.getTesto());
            ps.setLong(i++, film.getFilmID());
            ps.setString(i++, rew.getTitolo());
            ps.setString(i++, usr.getEmail());
            try {
                ps.executeUpdate();
                ps.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);

        } catch (NullPointerException e) {
            throw new NullPointerException();

        } catch (Exception e) {
            throw new RuntimeException();
        }

        return rew;

    }

    @Override
    public void update(Review rew) throws DuplicatedObjectException {

        PreparedStatement ps;

        try{

            String sql = "SELECT * FROM recensione WHERE deleted = 'N' AND recensione_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i,rew.getRew_id());
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();

            }catch (Exception e){
                e.printStackTrace();
            }
            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(!exist){
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.update: tentativo di update di una recensione non esistente");
            }else {
                sql = "UPDATE recensione SET data = ?, voto = ?,testo = ?,titolo = ? WHERE recensione_id = ?";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, rew.getData());
                ps.setString(i++, rew.getVoto());
                ps.setString(i++, rew.getTesto());
                ps.setString(i++, rew.getTitolo());
                ps.setLong(i++, rew.getRew_id());
                try {
                    ps.executeUpdate();
                }catch (Exception e){
                    e.printStackTrace();
                }
                ps.close();


            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void delete(Review rew) throws DuplicatedObjectException{

        PreparedStatement ps;

        try{

            String sql ="SELECT * FROM recensione WHERE deleted = 'N' AND recensione_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,rew.getRew_id());
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();


            if(!exist){

                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di update di una recensione non esistente");

            }else {

                sql = " UPDATE recensione SET deleted = 'Y' WHERE recensione_id = ? ";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, rew.getRew_id());
                ps.executeUpdate();
                ps.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Review> findByFilmID(Film film){


        PreparedStatement ps;
        Review rew;
        List<Review> rews = new ArrayList<Review>();

        try{

           String sql = "SELECT recensione_id, data, voto, testo, utente, film, recensione.titolo, utente.nome, utente.cognome " +
                        "FROM recensione " +
                        "JOIN film on recensione.film = film.film_id " +
                        "JOIN utente ON recensione.utente = utente.email " +
                        "WHERE recensione.film = ? and recensione.deleted = 'N'" +
                        "ORDER BY recensione.data ASC";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, film.getFilmID());
            ResultSet resultSet = null;
            try {
                //ResultSet resultSet = ps.executeQuery();
                resultSet = ps.executeQuery();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            while(resultSet.next()){
                rew = read(resultSet);
                rews.add(rew);

            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return rews;
    }


    public List<Review> findByUserEmail(String email){


        PreparedStatement ps;
        Review rew;
        List<Review> rews = new ArrayList<Review>();

        try{

            String sql ="SELECT recensione_id, data, voto, testo, utente, film, recensione.titolo FROM recensione  WHERE recensione.utente = ? and deleted = 'N' ORDER BY recensione.data ASC";


            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet resultSet = null;
            try {
                //ResultSet resultSet = ps.executeQuery();
                resultSet = ps.executeQuery();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            while(resultSet.next()){
                rew = read(resultSet);
                rews.add(rew);

            }

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return rews;
    }

    @Override
    public Review findByID(String rewID) {

        PreparedStatement ps;
        Review review = null;


        try{

            String sql ="SELECT * FROM recensione WHERE recensione_id = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,Long.parseLong(rewID));

            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                review = read(resultSet);
            }else throw new RuntimeException();
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  review;
    }

    public Float findMediaRecesioniByFilmID(String filmID){

        PreparedStatement ps;
        Float media = 0.00f;

        try{

            String sql = "SELECT AVG(CAST(recensione.voto AS FLOAT)) AS media_recensioni FROM recensione JOIN film ON recensione.film = film.film_id WHERE film_id = ? and recensione.deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,Long.parseLong(filmID));
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
            if(resultSet.next()){
                media = resultSet.getFloat("media_recensioni");
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

        return media;

    }

    Review read(ResultSet rs) {

        Review rew = new Review();
        User usr = new User();
        Film film = new Film();
        rew.setUsr(usr);
        rew.setFilm(film);


        try {
            rew.setRew_id(rs.getLong("recensione_id"));
        } catch (SQLException sqle) {
        }
        try {
            rew.setData(rs.getString("data"));
        } catch (SQLException sqle) {
        }
        try {
            rew.setVoto(rs.getString("voto"));
        } catch (SQLException sqle) {
        }
        try {
            rew.setTesto(rs.getString("testo"));
        } catch (SQLException sqle) {
        }
        try {
            rew.getUsr().setEmail(rs.getString("utente"));
        } catch (SQLException sqle) {

        }
        try {
            rew.getFilm().setFilm_id(rs.getLong("film"));
        } catch (SQLException sqle) {
        }
        try {
            rew.setDeleted(rs.getString("deleted").equals("N"));
        } catch (SQLException sqle) {
        }
        try {
            rew.setTitolo(rs.getString("titolo"));
        } catch (SQLException sqle) {
        }
        try{
            rew.getUsr().setNome(rs.getString("nome"));
        }catch (SQLException sqle) {
        }
        try{
            rew.getUsr().setCognome(rs.getString("cognome"));
        }catch (SQLException sqle) {
        }

        return rew;
    }


}

package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.FilmDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.User;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmDAOMySQLJDBCImpl implements FilmDAO {

    Connection conn;

    public FilmDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Film create(String durata, String regista, String titolo, String link_trailer, String locandina, String descrizione, String genere) throws DuplicatedObjectException {

        PreparedStatement ps;
        Film film = new Film();
        film.setDurata(durata);
        film.setRegista(regista);
        film.setTitolo(titolo);
        film.setLink_trailer(link_trailer);
        film.setLocandina(locandina);
        film.setTrama(descrizione);
        film.setGenere(genere);

        try{

            String sql = "SELECT * FROM film WHERE deleted = 'N' AND titolo = ? and regista = ? and durata = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, film.getTitolo());
            ps.setString(i++, film.getRegista());
            ps.setString(i++, film.getDurata());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(exist){
                throw new DuplicatedObjectException("FilmDAOImpl.create: Tentativo di inserimento di un user già esistente.");

            }else {

                sql ="INSERT INTO film (durata,regista,titolo,link_trailer,deleted,locandina,trama,genere) VALUES (?,?,?,?,'N',?,?,?)";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setString(i++, film.getDurata());
                ps.setString(i++, film.getRegista());
                ps.setString(i++, film.getTitolo());
                ps.setString(i++, film.getLink_trailer());
                ps.setString(i++, film.getLocandina());
                ps.setString(i++, film.getTrama());
                ps.setString(i++, film.getGenere());
                ps.executeUpdate();

            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return film;
    }


    @Override
    public void update(Film film) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Film film) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Film findByTitolo(String titolo) {


        PreparedStatement ps;
        Film film = null;

        try{

            String sql ="SELECT * FROM film WHERE LOWER(film.titolo) LIKE LOWER(?) and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1,titolo);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                film = read(resultSet);
            }else throw new RuntimeException();
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  film;
    }

    @Override
    public Film findById(String ID) {


        PreparedStatement ps;
        Film film = null;


        try{

            String sql ="SELECT * FROM film WHERE film_id = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,Long.parseLong(ID));

            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                film = read(resultSet);
            }else throw new RuntimeException();
            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  film;
    }

    @Override
    public List<Film> findByProiezione(){

        PreparedStatement ps;
        Film filmo;
        List<Film> films = new ArrayList<Film>();

        try{

            String sql
                    =   "SELECT DISTINCT film.film_id, durata,regista,titolo,link_trailer,film.deleted,locandina,trama,genere " +
                        "FROM FILM JOIN PROIEZIONE on film.film_id=proiezione.film_id " +
                        "Where film.deleted = 'N' " +
                        "AND proiezione.data::timestamp >= CURRENT_DATE " +
                        "AND proiezione.data::timestamp < date_trunc('week', current_date) + interval '1 week' " +
                        "order by titolo asc";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){


                filmo = read(resultSet);
                films.add(filmo);

            }


            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return films;

    }

    public List<Film> findByNotProiezione(){

        PreparedStatement ps;
        Film filmo;
        List<Film> films = new ArrayList<Film>();

        try{

            String sql =    "SELECT DISTINCT (f.*) " +
                            "FROM Film as f JOIN Proiezione as p ON f.film_id = p.film_id  " +
                            "WHERE f.deleted = 'N' " +
                            "AND p.data::timestamp >= date_trunc('week', current_date) + interval '1 week' " +
                            "AND p.data::timestamp < date_trunc('week', current_date) + interval '2 weeks'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){


                filmo = read(resultSet);
                films.add(filmo);

            }


            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return films;

    }

    public String findTitoloByID(Long ID){

        PreparedStatement ps;
        Film filmo = null;

        try{

            String sql ="SELECT film.titolo FROM FILM WHERE film_id = ? and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,ID);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                filmo = read(resultSet);
            }else {

                throw new RuntimeException();

            }


            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return filmo.getTitolo();

    }

    public Film findByProiezioneID(Long proiezione_id){

        PreparedStatement ps;
        Film filmo = null;

        try{

            String sql ="SELECT * FROM proiezione natural join film WHERE proiezione_id = ? and film.deleted = 'N' and proiezione.deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1,proiezione_id);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                filmo = read(resultSet);
            }else {

                throw new RuntimeException();

            }


            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return filmo;

    }

    @Override
    public List<Film> findAll() {

        PreparedStatement ps;
        Film filmo;
        List<Film> films = new ArrayList<Film>();

        try{

            String sql ="SELECT * FROM FILM WHERE deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){


                filmo = read(resultSet);
                films.add(filmo);

            }


            resultSet.close();
            ps.close();


        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return films;

    }



    Film read(ResultSet rs) {

        Film film = new Film();
        try {
            film.setFilm_id(rs.getLong("film_id"));
        } catch (SQLException sqle) {
        }
        try {
            film.setDurata(rs.getString("durata"));
        } catch (SQLException sqle) {
        }
        try {
            film.setRegista(rs.getString("regista"));
        } catch (SQLException sqle) {
        }
        try {
            film.setTitolo(rs.getString("titolo"));
        } catch (SQLException sqle) {
        }
        try {
            film.setLink_trailer(rs.getString("link_trailer"));
        } catch (SQLException sqle) {
        }
        try {
            film.setDeleted(rs.getString("deleted").equals('N'));
        } catch (SQLException sqle) {
        }
        try {
            film.setLocandina(rs.getString("locandina"));
        } catch (SQLException sqle) {
        }
        try {
            film.setGenere(rs.getString("genere"));
        } catch (SQLException sqle) {
        }
        try {
            film.setTrama(rs.getString("trama"));
        } catch (SQLException sqle) {
        }


        return film;
    }

}

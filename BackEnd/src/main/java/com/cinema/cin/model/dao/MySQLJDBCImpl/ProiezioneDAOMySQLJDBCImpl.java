package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.ProiezioneDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProiezioneDAOMySQLJDBCImpl implements ProiezioneDAO {

    Connection conn;

    public ProiezioneDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Proiezione create(String data, Film film, Sala sala) throws DuplicatedObjectException {

        PreparedStatement ps;
        Proiezione pro = new Proiezione();
        pro.setData(data);
        pro.setFilm(film);
        pro.setSala(sala);



        try{

            String sql = "SELECT * FROM proiezione WHERE data = ? AND film_id = ? and sala = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, pro.getData());
            ps.setLong(i++, pro.getFilm().getFilmID());
            ps.setLong(i++, pro.getSala().getSalaID());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(exist){
                throw new DuplicatedObjectException("UserDAOImpl.create: Tentativo di inserimento di un user già esistente.");

            }else {

                sql = "INSERT INTO proiezione (data, film_id, sala, deleted) VALUES (?,?,?,'N')";

                ps = conn.prepareStatement(sql);
                i = 1;

                ps.setString(i++, pro.getData());
                ps.setLong(i++, pro.getFilm().getFilmID());
                ps.setLong(i++, pro.getSala().getSalaID());

                ps.executeUpdate();
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
            return pro;
    }


    @Override
    public void update(Proiezione pro) {

    }

    @Override
    public void delete(Proiezione pro) {

    }

    public List<Proiezione> findByFilmID(Film film) {

        PreparedStatement ps;
        Proiezione pro;
        List<Proiezione> proieziones = new ArrayList<Proiezione>();

        try {

            String sql = "SELECT * FROM proiezione WHERE film_id = ? and deleted = 'N' ORDER BY data ASC";


            ps = conn.prepareStatement(sql);
            ps.setLong(1, film.getFilmID());
            ResultSet resultSet = null;
            try {
                //ResultSet resultSet = ps.executeQuery();
                resultSet = ps.executeQuery();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            while (resultSet.next()) {
                pro = read(resultSet);
                proieziones.add(pro);

            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return proieziones;
    }

    public Proiezione findByID(String ID) {

        PreparedStatement ps;
        Proiezione pro = null;


        try{

            String sql = "SELECT * FROM proiezione WHERE (proiezione_id = ?) and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,Long.parseLong(ID));



            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                pro = read(resultSet);

            }else
                throw new RuntimeException();


            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  pro;
    }

    @Override
    public Float findCostoByProiezioneID(Long ID) {

        PreparedStatement ps;
        Proiezione pro = null;


        try{

            String sql = "SELECT prezzo FROM proiezione WHERE (proiezione_id = ?) and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,ID);


            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                pro = read(resultSet);

            }else
                throw new RuntimeException();


            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  pro.getCosto();
    }

    @Override
    public String findDataByID(Long ID) {
        PreparedStatement ps;
        Proiezione pro = null;


        try{

            String sql = "SELECT data FROM proiezione WHERE (proiezione_id = ?) and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,ID);


            ResultSet resultSet = null;

            try{
                resultSet = ps.executeQuery();
            }catch(SQLException ex){
                ex.printStackTrace();
            }

            if(resultSet.next()){
                pro = read(resultSet);

            }else
                throw new RuntimeException();


            resultSet.close();
            ps.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return  pro.getData();
    }

    @Override
    public List<Proiezione> findByDataandSala(String data, Long salaID) {

        PreparedStatement ps;
        Proiezione pro;
        List<Proiezione> proieziones = new ArrayList<Proiezione>();

        try {

            String sql = "SELECT * FROM proiezione WHERE SUBSTRING(data FROM 1 FOR 10) = ? and sala = ? and deleted = 'N'";


            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++,data);
            ps.setLong(i, salaID);
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            while (resultSet.next()) {
                pro = read(resultSet);
                proieziones.add(pro);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return proieziones;
    }




    Proiezione read(ResultSet rs) {

            Proiezione pro = new Proiezione();
            Sala sala = new Sala();
            Film film = new Film();
            pro.setFilm(film);
            pro.setSala(sala);


            try {
                pro.setProie_id(rs.getLong("proiezione_id"));
            } catch (SQLException sqle) {
            }
            try {
                pro.setData(rs.getString("data"));
            } catch (SQLException sqle) {
            }
            try {
                pro.setCosto(rs.getFloat("prezzo"));
            } catch (SQLException sqle) {
            }
            try {
                pro.getFilm().setFilm_id(rs.getLong("film_id"));
            } catch (SQLException sqle) {
            }
            try {
                pro.getSala().setSalaID(rs.getLong("sala"));
            } catch (SQLException sqle) {
            }
            try {
                pro.setDeleted(rs.getString("deleted").equals("N"));
            } catch (SQLException sqle) {
            }
            return pro;
        }


}

package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.SalaDAO;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalaDAOMySQLJDBCImpl implements SalaDAO {

    Connection conn;

    public SalaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Sala create(Long salaID, String numeroSala, String numeroPosti) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Sala sala) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Sala sala) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Sala findByID(Long salaID) {


        PreparedStatement ps;
        Sala sala = null;


        try {

            String sql = "SELECT * FROM sala WHERE sala.id = ? and deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, salaID);

            ResultSet resultSet = null;

            try {
                resultSet = ps.executeQuery();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (resultSet.next()) {
                sala = read(resultSet);
            } else throw new RuntimeException();
            resultSet.close();
            ps.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return sala;
    }



    public ArrayList<Sala> findAll(){

        PreparedStatement ps;
        ArrayList<Sala> sale = new ArrayList<>();

        try {


            String sql = "SELECT * FROM sala WHERE deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = null;
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Sala sala = read(resultSet);
                sale.add(sala);
            }


        }catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return sale;
    }

    Sala read(ResultSet rs) {

        Sala sala = new Sala();

        try {
            sala.setSalaID(rs.getLong("id"));
        } catch (SQLException sqle) {
        }
        try {
            sala.setNumeroSala(rs.getString("numero_sala"));
        } catch (SQLException sqle) {
        }
        try {
            sala.setNumeroPosti(rs.getString("num_posti"));
        } catch (SQLException sqle) {
        }
        try {
            sala.setDeleted(rs.getString("deleted").equals('N'));
        } catch (SQLException sqle) {
        }

        return sala;
    }


}

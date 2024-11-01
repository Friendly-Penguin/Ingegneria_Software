package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.MetodoPagamentoDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.MetodoPagamento;
import com.cinema.cin.model.mo.User;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class MetodoPagamentoDAOMySQLJDBCImpl implements MetodoPagamentoDAO {

    Connection conn;

    public MetodoPagamentoDAOMySQLJDBCImpl(Connection conn){
        this.conn=conn;
    }

    @Override
    public MetodoPagamento create(String n_carta, String data_scad, String CVV, String titolare, User user) throws DuplicatedObjectException {
        PreparedStatement ps;
        MetodoPagamento metPag=new MetodoPagamento();

        metPag.setUser(user);
        metPag.setN_Carta(n_carta);
        metPag.setDataScad(data_scad);
        metPag.setCVV(CVV);
        metPag.setTitolare(titolare);

        try{
            String sql
                    = " SELECT utente, n_carta "
                    + " FROM metodopagamento "
                    + " WHERE "
                    + " deleted= 'N' AND "
                    + " utente=? AND "
                    + " n_carta=? AND "
                    + "titolare=?";

            ps=conn.prepareStatement(sql);
            int i=1;
            ps.setString(i++,metPag.getUser().getEmail());
            ps.setString(i++,metPag.getN_Carta());
            ps.setString(i++,metPag.getTitolare());

            ResultSet resultSet=ps.executeQuery();

            boolean exist;
            exist= resultSet.next();
            resultSet.close();

            if(exist){
                throw new DuplicatedObjectException("MetPagDAOImpl.create: Tentantivo di inserimento di una carta già esistente.");
            }


            sql
                    ="INSERT INTO metodopagamento"
                    +"("
                    +"n_carta,"
                    +"data_scadenza,"
                    +"CVV,"
                    +"deleted,"
                    +"utente,"
                    +"titolare)"
                    +"VALUES (?,?,?,'N',?,?)";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++,metPag.getN_Carta());
            ps.setString(i++, metPag.getDataScad());
            ps.setString(i++,metPag.getCVV());
            ps.setString(i++,metPag.getUser().getEmail());
            ps.setString(i++,metPag.getTitolare());

            try {
                ps.executeUpdate();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return metPag;
    }

    @Override
    public void update(MetodoPagamento metPag) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {

            String sql
                    = " SELECT utente, n_carta "
                    + " FROM metodo_pagamento "
                    + " WHERE "
                    + " deleted ='N' AND "
                    + " n_carta = ? AND"
                    + " data_scadenza = ? AND"
                    + " CVV = ? AND ";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, metPag.getN_Carta());
            ps.setString(i++, metPag.getDataScad());
            ps.setString(i++, metPag.getCVV());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            Long id = resultSet.getLong("metodoid");
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("MetPagDAOJDBCImpl.create: Tentativo di aggiornamento in un pagamento già esistente.");
            }

            String ID = Long.toString(id);

            sql
                    = " UPDATE metodo_pagamento "
                    + " SET "
                    + "   n_carta = ?, "
                    + "   data_scadenza = ?, "
                    + "   CVV = ? "
                    + " WHERE "
                    + "   metodoID = ? ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, metPag.getN_Carta());
            ps.setString(i++,metPag.getDataScad());
            ps.setString(i++, metPag.getCVV());
            ps.setString(i++, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(MetodoPagamento metPag) throws DuplicatedObjectException {
        PreparedStatement ps;

        try{

            String sql ="SELECT * FROM metodopagamento WHERE deleted = 'N' AND metodopagamento.metodo_id = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++,metPag.getMetodo_id());
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();


            if(!exist){

                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: tentativo di update di un utente non esistente");

            }else {

                sql = " UPDATE metodopagamento SET deleted='Y'  WHERE metodo_id = ? ";

                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, metPag.getMetodo_id());
                ps.executeUpdate();
                ps.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MetodoPagamento> findByUser(User user) {

        PreparedStatement ps;
        MetodoPagamento metPag;
        ArrayList<MetodoPagamento> metPags = new ArrayList<MetodoPagamento>();

        try {

            String sql
                    = "SELECT * FROM metodopagamento WHERE deleted = 'N' AND utente = ? ";

            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setString(i++,user.getEmail());
            ResultSet resultSet = null;
            try {
                resultSet = ps.executeQuery();
            }catch (Exception e) {
                e.printStackTrace();
            }

            while (resultSet.next()) {
                metPag = read(resultSet);
                metPags.add(metPag);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return metPags;
    }


    MetodoPagamento read(ResultSet rs){

        MetodoPagamento metPag = new MetodoPagamento();
        User user=new User();
        metPag.setUser(user);

        try {
            metPag.getUser().setEmail(rs.getString("utente"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setTitolare(rs.getString("titolare"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setMetodo_id(rs.getLong("metodo_id"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setN_Carta(rs.getString("n_carta"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setDataScad(rs.getString("data_scadenza"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setCVV(rs.getString("cvv"));
        } catch (SQLException sqle) {
        }
        try {
            metPag.setDeleted(rs.getString("deleted").equals("N"));
        } catch (SQLException sqle) {
        }
        return metPag;
}
}


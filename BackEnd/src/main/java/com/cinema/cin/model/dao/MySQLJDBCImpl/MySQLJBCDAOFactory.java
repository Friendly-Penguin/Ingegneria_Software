package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.model.dao.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


public class MySQLJBCDAOFactory extends DAOFactory {


    private Map factoryParameters;

    private Connection connection;

    public MySQLJBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOMySQLJDBCImpl(connection);
    }

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {
        return new AbbonamentoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public FilmDAO getFilmDAO() {
        return new FilmDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ProiezioneDAO getProiezioneDAO() {
        return new ProiezioneDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ReviewDAO getReviewDAO() {

        return new ReviewDAOMySQLJDBCImpl(connection);
    }

    @Override
    public SalaDAO getSalaDAO() {
        return new SalaDAOMySQLJDBCImpl(connection);
    }

    @Override
    public TicketDAO getTicketDAO() {
        return new TicketDAOMySQLJDBCImpl(connection);
    }

    @Override
    public MetodoPagamentoDAO getMetodoPagamentoDAO() {
        return new MetodoPagamentoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {return new CarrelloDAOMySQLJDBCImpl(connection);}

    @Override
    public AdminDAO getAdminDAO() {return new AdminDAOMySQLJDBCImpl(connection);}

}

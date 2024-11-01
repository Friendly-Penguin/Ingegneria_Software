package com.cinema.cin.model.dao;

import java.util.Map;
import com.cinema.cin.model.dao.CookieImpl.CookieDAOFactory;
import com.cinema.cin.model.dao.MySQLJDBCImpl.MySQLJBCDAOFactory;
/*


import com.isw.es_07_rubrica.model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

 */

public abstract class DAOFactory {
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL = "CookieImpl";

    public DAOFactory() {
    }

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

    public abstract void closeTransaction();

    public abstract UserDAO getUserDAO();

    public abstract AbbonamentoDAO getAbbonamentoDAO();

    public abstract FilmDAO getFilmDAO();

    public abstract ProiezioneDAO getProiezioneDAO();

    public abstract ReviewDAO getReviewDAO();

    public abstract SalaDAO getSalaDAO();

    public abstract TicketDAO getTicketDAO();

    public abstract MetodoPagamentoDAO getMetodoPagamentoDAO();

    public abstract CarrelloDAO getCarrelloDAO();

    public abstract AdminDAO getAdminDAO();

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters) {
        if (whichFactory.equals("MySQLJDBCImpl")) {
            return new MySQLJBCDAOFactory(factoryParameters);
        } else {
            return whichFactory.equals("CookieImpl") ? new CookieDAOFactory(factoryParameters) : null;
        }
    }
}

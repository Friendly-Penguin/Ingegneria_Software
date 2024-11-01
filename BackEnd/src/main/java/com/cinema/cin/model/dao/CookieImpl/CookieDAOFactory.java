package com.cinema.cin.model.dao.CookieImpl;

import com.cinema.cin.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;


public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters = factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request = (HttpServletRequest) factoryParameters.get("request");
            this.response = (HttpServletResponse) factoryParameters.get("response");
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void closeTransaction() {
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOCookieImpl(request,response);
    }

    public AdminDAO getAdminDAO() {return new AdminDAOCookieImpl(request,response);}

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public FilmDAO getFilmDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProiezioneDAO getProiezioneDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ReviewDAO getReviewDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SalaDAO getSalaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TicketDAO getTicketDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MetodoPagamentoDAO getMetodoPagamentoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {return new CarrelloDAOCookieImpl(request,response);}
}
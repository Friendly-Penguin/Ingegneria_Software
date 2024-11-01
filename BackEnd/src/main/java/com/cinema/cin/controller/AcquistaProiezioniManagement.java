package com.cinema.cin.controller;

import com.cinema.cin.model.dao.DAOFactory;
import com.cinema.cin.model.dao.FilmDAO;
import com.cinema.cin.model.dao.ProiezioneDAO;
import com.cinema.cin.model.dao.UserDAO;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Proiezione;
import com.cinema.cin.model.mo.User;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcquistaProiezioniManagement {

    private AcquistaProiezioniManagement() {

    }


    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;


        Logger logger = LogService.getApplicationLogger();
        String applicationMessage = null;
        List<Proiezione> proiezioni = new ArrayList<>();
        List<Film> films = new ArrayList<>();
        Boolean exist = true;


        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();


            try {


                films = filmDAO.findByProiezione();

                    for (int i = 0; i < films.size(); i++) {
                        proiezioni.addAll(proiezioneDAO.findByFilmID(films.get(i)));
                    }

            }catch (Exception ex){

                applicationMessage = "Errore recupero proiezioni = ricerca";
                logger.log(Level.SEVERE, "Errore recupero proiezioni = ricerca");
                exist = false;

            }

            if (exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();


                request.setAttribute("proiezioni", proiezioni);
                request.setAttribute("film", films);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "acquistaProiezione/view");

            } else {

                String destinazione = "HomeManagement.view";

                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "pagineLanding/rifiutaAggiunta");

            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}

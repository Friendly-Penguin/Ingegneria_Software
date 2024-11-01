package com.cinema.cin.controller;

import com.cinema.cin.model.dao.DAOFactory;
import com.cinema.cin.model.dao.FilmDAO;
import com.cinema.cin.model.dao.ReviewDAO;
import com.cinema.cin.model.dao.UserDAO;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Review;
import com.cinema.cin.model.mo.User;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewFilmManagement {

    private ViewFilmManagement() {

    }


    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();
        Film film = null;
        String filmID = request.getParameter("filmID");
        String filmTitolo = request.getParameter("titolo");
        String FilmID = (String) request.getAttribute("FilmID");
        String applicationMessage = (String) request.getAttribute("applicationMessage");
        String mediaUtente = null;
        List<Review> rews = null;
        boolean exist = true;


        try {


            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            ReviewDAO rewDAO = daoFactory.getReviewDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            if (filmID != null) {

                try {

                    film = filmDAO.findById(filmID);

                } catch (Exception e) {
                    applicationMessage = "Il film ricercato non è presente nel sistema!";
                    logger.log(Level.INFO, "Film non presente");
                    exist = false;
                }
            } else if (filmTitolo != null) {

                try {

                    film = filmDAO.findByTitolo(filmTitolo);

                } catch (Exception ex) {

                    applicationMessage = "Il film ricercato non è presente nel sistema!";
                    logger.log(Level.INFO, "Film non presente");
                    exist = false;
                }
            } else {

                filmID = FilmID;


                try {

                    film = filmDAO.findById(filmID);

                } catch (Exception e) {
                    applicationMessage = "Il film ricercato non è presente nel sistema!";
                    logger.log(Level.INFO, "Film non presente");
                    exist = false;
                }
            }

            if (exist) {
                try {

                    rews = rewDAO.findByFilmID(film);

                } catch (Exception ex) {
                    applicationMessage = "Errore nel recupero delle recensioni!";
                    logger.log(Level.INFO, "Errore recensioni");
                    exist = false;
                }
            }

            if (exist) {

                try {

                    mediaUtente = rewDAO.findMediaRecesioniByFilmID((film.getFilmID()).toString()).toString();


                } catch (Exception ex) {

                    applicationMessage = "Errore nel recupero della media recensioni!";
                    logger.log(Level.INFO, "Errore nel recupero della media recensioni");
                    exist = false;

                }
            }

            if (exist) {


                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("film", film);
                request.setAttribute("rew", rews);
                request.setAttribute("mediaUtenti", mediaUtente);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "viewFilm/view");

            } else {

                String destinazione = "HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
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
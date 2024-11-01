package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.mo.*;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreaElementiManagement {

    private CreaElementiManagement() {

    }

    public static void creaBiglietti(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        User loggedUser;

        String proiezioneID = request.getParameter("proiezioneID");
        String carrelloID = request.getParameter("carrelloID");
        String posti = request.getParameter("posti");
        String[] posti_singoli = posti.split("\\|");

        String destinazione = null;
        List<Ticket> ticket = new ArrayList<Ticket>();
        User user = new User();
        Carrello carrello = new Carrello();
        Proiezione proiezione = new Proiezione();

        Logger logger = LogService.getApplicationLogger();

        try {

            Boolean exist = true;
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            TicketDAO ticketDAO = daoFactory.getTicketDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();

            try{
                user = userDAO.findByUserEmail(loggedUser.getEmail());

            }catch (Exception e){
                applicationMessage = "Errore recupero utente - biglietto";
                logger.log(Level.INFO, "Errore recupero utente - biglietto");
                exist=false;
            }
            if(exist) {
                try {
                    carrello = carrelloDAO.findByID(Long.parseLong(carrelloID));

                } catch (Exception e) {
                    applicationMessage = "Errore recupero carrello - biglietto";
                    logger.log(Level.INFO, "Errore recupero carrello - biglietto");
                    exist=false;
                }
            }
            if(exist) {
                try {
                    proiezione = proiezioneDAO.findByID(proiezioneID);

                } catch (Exception e) {
                    applicationMessage = "Errore recupero proiezione - biglietto";
                    logger.log(Level.INFO, "Errore recupero proiezione - biglietto");
                    exist=false;
                }
            }


            if(exist) {
                for (int i = 0; i < posti_singoli.length; i++) {

                    String[] posti_singoli_separati = posti_singoli[i].split("_");

                    try {

                        ticketDAO.create(carrello, user, proiezione, posti_singoli_separati[0], posti_singoli_separati[1]);

                    } catch (Exception e) {

                        applicationMessage = "Errore creazione - biglietto: " + i;
                        logger.log(Level.INFO, "Errore creazione - biglietti");
                        exist=false;
                    }

                }
            }

            if(exist){

                daoFactory.commitTransaction();
                aggiornaCookie(daoFactory,sessionDAOFactory,request);
                sessionDAOFactory.commitTransaction();

                destinazione="CarrelloManagement.view";
                applicationMessage = "Articoli aggiunti correttamente!";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/confermaAggiunta");


            }else {

                destinazione="HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/rifiutaAggiunta");


            }





        }catch (Exception e) {
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

    public static void creaAbbonamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        User loggedUser;
        Carrello carrello = new Carrello();
        Abbonamento abbonamento = new Abbonamento();

        String costo = request.getParameter("costo");
        String carrelloID = request.getParameter("carrelloID");

        String destinazione = null;
        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();

            try{

                carrello = carrelloDAO.findByID(Long.parseLong(carrelloID));

            }catch (Exception ex){

                applicationMessage = "Errore recuperoCarrello - creaElementi";
                logger.log(Level.INFO, "Errore recuperoCarrello - creaElementi");
                exist = false;

            }

            if(exist){
                try{

                    abbonamento = abbonamentoDAO.findAbbByUser(loggedUser);

                }catch (Exception ex){

                    applicationMessage = "Errore recuperoAbbonamento utente - creaElementi";
                    logger.log(Level.INFO, "Errore recuperoAbbonamento utente - creaElementi");
                    exist = false;

                }
            }


            if(exist) {

                if (abbonamento == null) {
                    try {

                        abbonamentoDAO.create("10", Float.parseFloat(costo), loggedUser, carrello);

                    } catch (Exception ex) {

                        applicationMessage = "Errore creazioneAbbonamento - creaElementi";
                        logger.log(Level.INFO, "Errore creazioneAbbonamento - creaElementi");
                        exist = false;

                    }
                }else if(abbonamento.getNumAccessi().equals("0")){

                    try {

                        abbonamentoDAO.delete(abbonamento);
                        abbonamentoDAO.create("10", Float.parseFloat(costo), loggedUser, carrello);

                    } catch (Exception ex) {

                        applicationMessage = "Errore updateAbbonamento - creaElementi";
                        logger.log(Level.INFO, "Errore updateAbbonamento - creaElementi");
                        exist = false;

                    }
                }else{
                    exist = false;
                    applicationMessage = "Errore! Possiedi già un abbonamento con: " + abbonamento.getNumAccessi() + " accessi rimanenti";
                    logger.log(Level.INFO, "Errore abbonamento già esistente");
                }
            }
            if(exist){

                daoFactory.commitTransaction();
                aggiornaCookie(daoFactory,sessionDAOFactory,request);
                sessionDAOFactory.commitTransaction();

                destinazione="CarrelloManagement.view";
                applicationMessage = "Articoli aggiunti correttamente!";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/confermaAggiunta");


            }else {

                destinazione="HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/rifiutaAggiunta");


            }




        }catch (Exception e) {
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

    public static void modificaBiglietto(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        User loggedUser;

        String proiezioneID = request.getParameter("proiezioneID");
        String ticketID = request.getParameter("ticketID");
        String posto = request.getParameter("posti");


        String destinazione = null;
        User user = new User();
        Proiezione proiezione = new Proiezione();
        Ticket ticket = new Ticket();

        if (posto != null && posto.length() > 0) {
            // Rimuove l'ultimo carattere
            posto = posto.substring(0, posto.length() - 1);
        }

        Logger logger = LogService.getApplicationLogger();

        try {

            Boolean exist = true;
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            TicketDAO ticketDAO = daoFactory.getTicketDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();

            try{
                user = userDAO.findByUserEmail(loggedUser.getEmail());

            }catch (Exception e){
                applicationMessage = "Errore recupero utente - biglietto";
                logger.log(Level.INFO, "Errore recupero utente - biglietto");
                exist=false;
            }

            if(exist) {
                try {
                    proiezione = proiezioneDAO.findByID(proiezioneID);

                } catch (Exception e) {
                    applicationMessage = "Errore recupero proiezione - biglietto";
                    logger.log(Level.INFO, "Errore recupero proiezione - biglietto");
                    exist=false;
                }
            }


            if(exist) {

                    String[] posti_singoli_separati = posto.split("_");

                    try {

                        ticketDAO.createMod(user, proiezione, posti_singoli_separati[0], posti_singoli_separati[1]);

                    } catch (Exception e) {

                        applicationMessage = "Errore creazione - biglietto ";
                        logger.log(Level.INFO, "Errore creazione - biglietti");
                        exist=false;
                    }

                }

            if(exist){

                try {

                    ticket = ticketDAO.getTicketByTicketID(Long.parseLong(ticketID));
                    ticketDAO.delete(ticket);

                } catch (Exception e) {

                    applicationMessage = "Errore cancellazione - biglietto";
                    logger.log(Level.INFO, "Errore cancellazione - biglietto");
                    exist = false;
                }

            }

            if(exist){

                daoFactory.commitTransaction();
                aggiornaCookie(daoFactory,sessionDAOFactory,request);
                sessionDAOFactory.commitTransaction();

                destinazione="AccountManagement.viewTicket";
                applicationMessage = "Biglietto modificato correttamente!";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/confermaAggiunta");


            }else {

                destinazione="HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl","pagineLanding/rifiutaAggiunta");


            }





        }catch (Exception e) {
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

    public static void createRew(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;

        User loggedUser;
        Logger logger = LogService.getApplicationLogger();
        Review rew = null;
        String titolo = request.getParameter("campoTitolo");
        String testo = request.getParameter("campoTesto");
        String voto = request.getParameter("rate");
        String email = request.getParameter("campoEmail");
        String filmID = request.getParameter("campoFilmID");
        String data = request.getParameter("campoData");
        String applicationMessage = null;
        Film film = null;
        User user = null;

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
            UserDAO userDAO = daoFactory.getUserDAO();

            try {

                film = filmDAO.findById(filmID);

            } catch (Exception ex) {
                applicationMessage = "Errore recupero film";
                logger.log(Level.INFO, "Errore recupero film");
                exist = false;
            }

            if (exist) {
                try {
                    user = userDAO.findByUserEmail(email);

                } catch (Exception ex) {
                    applicationMessage = "Errore recupero utente";
                    logger.log(Level.INFO, "Errore recupero utente");
                    exist = false;
                }
            }

            if (exist) {

                try {

                    rewDAO.create(user, film, data, voto, titolo, testo);

                } catch (NullPointerException ex) {

                    applicationMessage = "Hai già inserito una recensione per questo film";
                    logger.log(Level.INFO, "Errore creazione recensione - gia' presente");
                    exist = false;

                } catch (RuntimeException ex) {

                    applicationMessage = "Errore creazione recensione";
                    logger.log(Level.INFO, "Errore creazione recensione");
                    exist = false;
                }
            }
            if(exist){

                applicationMessage = "Recensione inserita!";
                String destinazione = "ViewFilmManagement.view";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("FilmID", film.getFilmID().toString());
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("filmID", filmID);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");




            }else{

                String destinazione = "ViewFilmManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("filmID", filmID);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
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

    public static void eliminaRewUser(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        String reviewID = request.getParameter("reviewID");
        String filmID = request.getParameter("campoFilmID");


        Boolean exist = true;
        String applicationMessage = null;
        String destinazione = null;

        Logger logger = LogService.getApplicationLogger();

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
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();

            try{

                reviewDAO.delete(reviewDAO.findByID(reviewID));


            }catch (Exception ex){

                applicationMessage = "Errore cancellazione recensione";
                logger.log(Level.SEVERE, "Errore cancellazione recensione");
                exist = false;

            }

            if(exist){

                destinazione = "ViewFilmManagement.view";
                applicationMessage = "Recensione rimossa correttamente!";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedAdmin", loggedUser);
                request.setAttribute("filmID", filmID);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");


            }else{

                destinazione = "HomeManagement.view";
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedAdmin", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
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

    public static void modificaRew(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        User loggedUser;

        String campoData = request.getParameter("campoData");
        String campoVoto = request.getParameter("rate");
        String campoTesto = request.getParameter("campoTesto");
        String reviewID = request.getParameter("reviewID");
        String campoTitlo = request.getParameter("campoTitolo");


        String destinazione = null;
        Boolean exist = true;
        Logger logger = LogService.getApplicationLogger();
        Review rew = null;

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
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();

            try{

                rew = reviewDAO.findByID(reviewID);


            }catch (Exception ex){

                applicationMessage = "Errore recupero recensione";
                logger.log(Level.SEVERE, "Errore recupero recensione");
                exist = false;

            }

            if(exist){

                try{


                    rew.setTitolo(campoTitlo);
                    rew.setData(campoData);
                    rew.setVoto(campoVoto);
                    rew.setTesto(campoTesto);
                    reviewDAO.update(rew);


                }catch (Exception ex){

                    applicationMessage = "Errore modifica recensione";
                    logger.log(Level.SEVERE, "Errore modifica recensione");
                    exist = false;

                }
            }

            if(exist){

                destinazione = "ViewFilmManagement.view";
                applicationMessage = "Recensione modificata correttamente!";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("filmID", rew.getFilm().getFilmID().toString());
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");


            }else{

                destinazione = "HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
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

    public static void aggiornaCookie(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request){


        Carrello carrello = new Carrello();
        Integer items;


        CarrelloDAO cartSessionDAO = sessionDAOFactory.getCarrelloDAO();
        CarrelloDAO cartDAO = daoFactory.getCarrelloDAO();


        carrello = cartSessionDAO.findLoggedCartID();

        try{

            items = cartDAO.findCartItemsByCartID(carrello.getCarrello_id());
            carrello.setItems(items);
            items = cartDAO.findCountOfTicketsByCartID(carrello.getCarrello_id());
            carrello.setNumeroTickets(items);
            cartSessionDAO.update(carrello);

        }catch (Exception e){

            throw new RuntimeException();

        }



    }

}

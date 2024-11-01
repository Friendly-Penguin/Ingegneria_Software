package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.mo.*;
import com.cinema.cin.model.mo.Sala;
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

public class AccountManagement {

    private AccountManagement() {

    }

    //Sezione INFO

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        User usr = null;
        String applicationMessage = null;
        Abbonamento abbonamento = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            UserDAO userDAO = daoFactory.getUserDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();

            try {

                usr = userDAO.findByUserEmail(loggedUser.getEmail());

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                applicationMessage = "Errore recupero utente";
            }
            try {

                abbonamento = abbonamentoDAO.findAbbByUser(usr);

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                applicationMessage = "Errore recupero abbonamento";
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("utente", usr);
            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("abbonamento", abbonamento);
            request.setAttribute("viewUrl", "account/view");


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

    //Sezione Conferma Account

    public static void viewConferma(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        User usr = null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            UserDAO userDAO = daoFactory.getUserDAO();


            /*
            try{

                usr = userDAO.findByUserEmail(loggedUser.getEmail());

            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage());
                applicationMessage = "Errore recupero utente";
            }
            */


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            //request.setAttribute("utente",usr);
            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "account/confermaAccount");


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

    public static void confermaAccount(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        User user = null;
        String applicationMessage = null;
        boolean verificato = false;
        String token = request.getParameter("token");

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            UserDAO userDAO = daoFactory.getUserDAO();

            try {

                user = userDAO.findByUserEmail(loggedUser.getEmail());

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                applicationMessage = "Errore recupero utente";
            }

            try {

                verificato = userDAO.confermaAccount(user, token);

            } catch (Exception e) {

                logger.log(Level.SEVERE, e.getMessage());
                applicationMessage = "Errore: il token non corrisponde";

            }
            if (verificato) {

                user.setConfermaEmail("true");
                sessionUserDAO.delete((User) null);
                loggedUser = sessionUserDAO.create(user.getEmail(), user.getNome(), user.getCognome(), (String) null, (String) null, (String) null, user.getConfermaEmail());
                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                applicationMessage = "Account Verificato!";
                request.setAttribute("utente", user);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "account/view");

            } else {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                applicationMessage = "Errore: il token non corrisponde";
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "account/confermaAccount");

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

    //Sezione Recensioni

    public static void viewRew(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Film film = null;
        List<Review> reviewList = null;


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            ReviewDAO rewDAO = daoFactory.getReviewDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            try {

                reviewList = rewDAO.findByUserEmail(loggedUser.getEmail());

            } catch (Exception ex) {
                applicationMessage = "Errore nel recupero delle recensioni!";
                logger.log(Level.INFO, "Errore recensioni");
            }

            try {

                for (int i = 0; i < reviewList.size(); i++) {

                    reviewList.get(i).setFilm(filmDAO.findById(reviewList.get(i).getFilm().getFilmID().toString()));

                }

            } catch (Exception ex) {
                applicationMessage = "Errore nel recupero delle recensioni!";
                logger.log(Level.INFO, "Errore recensioni");
            }

            request.setAttribute("recensioni", reviewList);
            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "account/recensioni");

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

    public static void eliminaRew(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        String reviewID = request.getParameter("reviewID");

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

                destinazione = "AccountManagement.viewRew";
                applicationMessage = "Recensione rimossa correttamente!";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedAdmin", loggedUser);
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

    //Sezione Ticket

    public static void viewTicket(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        String applicationMessage = null;
        List<Ticket> ticketList = null;
        Film film = null;
        Sala sala = null;
        Proiezione proiezione = null;
        Long proiezioneID;
        Long filmID;
        Long salaID;
        Boolean exist = true;


        Logger logger = LogService.getApplicationLogger();

        try {

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
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            SalaDAO salaDAO = daoFactory.getSalaDAO();


            try {

                ticketList = ticketDAO.getTicketByUserEmail(loggedUser.getEmail());

            } catch (Exception ex) {
                applicationMessage = "Errore nel recupero dei biglietti!";
                logger.log(Level.INFO, "Errore biglietti");
                exist = false;
            }

            if(exist) {
                try {

                    for(int i = 0; i < ticketList.size(); i++) {

                        proiezioneID = ticketList.get(i).getProiezione().getProie_id();
                        proiezione = proiezioneDAO.findByID(proiezioneID.toString());

                        filmID = proiezione.getFilm().getFilmID();
                        film = filmDAO.findById(filmID.toString());

                        salaID = proiezione.getSala().getSalaID();
                        sala = salaDAO.findByID(salaID);

                        ticketList.get(i).setProiezione(proiezione);
                        ticketList.get(i).getProiezione().setFilm(film);
                        ticketList.get(i).getProiezione().setSala(sala);

                    }

                } catch (Exception ex) {
                    applicationMessage = "Errore nel recupero dei biglietti!";
                    logger.log(Level.INFO, "Errore biglietti");
                    exist = false;
                }
            }

            if(exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("ticket", ticketList);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "account/biglietti");

            }else {

                String destinazione = "AccountManagement.view";

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

    //Sezione Metodi di pagamento

    public static void viewMetodiPagamento(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;

        User loggedUser;
        String applicationMessage = null;

        List<MetodoPagamento> listaMetodi = new ArrayList<>();
        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

           MetodoPagamentoDAO metodoPagamentoDAO = daoFactory.getMetodoPagamentoDAO();

           try{

               listaMetodi.addAll(metodoPagamentoDAO.findByUser(loggedUser));

           }catch (Exception ex){

               applicationMessage = "Errore recupero metodi - utente";
               logger.log(Level.SEVERE, "Errore recupero metodi - utente");
               exist = false;
           }

           if(exist){

               daoFactory.commitTransaction();
               sessionDAOFactory.commitTransaction();


               request.setAttribute("loggedOn", loggedUser != null);
               request.setAttribute("loggedUser", loggedUser);
               request.setAttribute("listaMetodi", listaMetodi);
               request.setAttribute("applicationMessage", applicationMessage);
               request.setAttribute("viewUrl", "account/metodiPagamento");



           }else{

               request.setAttribute("loggedOn", loggedUser != null);
               request.setAttribute("loggedUser", loggedUser);
               request.setAttribute("applicationMessage", applicationMessage);
               request.setAttribute("viewUrl", "account/view");

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

    public static void rimuoviMetodo(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;

        User loggedUser;
        String applicationMessage = null;

        String cartaID = request.getParameter("cartaID");


        MetodoPagamento carta = new MetodoPagamento();
        String destinazione = null;
        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            MetodoPagamentoDAO metodoPagamentoDAO = daoFactory.getMetodoPagamentoDAO();

            try{
                carta.setMetodo_id(Long.parseLong(cartaID));
                metodoPagamentoDAO.delete(carta);

            }catch (Exception ex){

                applicationMessage = "Errore rimozione metodo - utente";
                logger.log(Level.SEVERE, "Errore rimozione metodo - utente");
                exist = false;

            }


            if(exist){

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            destinazione = "AccountManagement.viewMetodiPagamento";
            applicationMessage = "Metodo rimosso correttamente!";


            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("destinazione", destinazione);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");

            }else{

                destinazione = "AccountManagement.viewMetodiPagamento";

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



}


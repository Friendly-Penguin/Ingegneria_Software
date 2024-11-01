package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.mo.*;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModificaTicketManagement {

    private ModificaTicketManagement(){

    }

    public static void modificaTicket(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        User loggedUser;
        Ticket ticket = new Ticket();

        String ticketID = request.getParameter("ticketID");

        String destinazione = null;
        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;
        List<Proiezione> proiezioni = new ArrayList<>();
        List<Film> films = new ArrayList<>();

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
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            SalaDAO salaDAO = daoFactory.getSalaDAO();


            try {

                ticket = ticketDAO.getTicketByTicketID(Long.parseLong(ticketID));
                ticket.setProiezione(proiezioneDAO.findByID(ticket.getProiezione().getProie_id().toString()));
                ticket.getProiezione().setSala(salaDAO.findByID(ticket.getProiezione().getSala().getSalaID()));
                ticket.getProiezione().setFilm(filmDAO.findByProiezioneID(ticket.getProiezione().getProie_id()));


            } catch (Exception ex) {

                applicationMessage = "Errore recupero ticket - creaElementi";
                logger.log(Level.SEVERE, "Errore recupero ticket - creaElementi");
                exist = false;

            }


            try {


                films = filmDAO.findByProiezione();

                for (int i = 0; i < films.size(); i++) {

                    proiezioni.addAll(proiezioneDAO.findByFilmID(films.get(i)));
                }

            } catch (Exception ex) {

                applicationMessage = "Errore recupero proiezioni - modifica";
                logger.log(Level.SEVERE, "Errore recupero proiezioni - modifica");
                exist = false;

            }

            if (exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();


                request.setAttribute("proiezioni", proiezioni);
                request.setAttribute("film", films);
                request.setAttribute("ticket", ticket);
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "modificaTicket/ricercaModificaTicket");

            } else {

                destinazione = "HomeManagement.view";

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

    public static void viewSala(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String proID = request.getParameter("proiezioneID");
        String titoloFilm = request.getParameter("filmTitolo");
        String applicationMessage = null;
        Boolean exist = true;


        String[] proiezioniSeparate = proID.split("-");
        List<Proiezione> proieziones = new ArrayList<>();
        LinkedHashMap<Long, List<Ticket>> proiezione_biglietti = new LinkedHashMap<>();
        String data = null;
        LinkedHashMap<Long, String> proiezione_sala = new LinkedHashMap<>();


        String ticketID = request.getParameter("ticketID");
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


            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            TicketDAO ticketDAO = daoFactory.getTicketDAO();
            SalaDAO salaDAO = daoFactory.getSalaDAO();

            try{

                for(int i = 0; i < proiezioniSeparate.length; i++) {

                    proieziones.add(proiezioneDAO.findByID(proiezioniSeparate[i]));
                    proiezione_biglietti.put(proieziones.get(i).getProie_id(), null);
                    proiezione_sala.put(proieziones.get(i).getProie_id(), null);
                }
                data = proieziones.get(0).getData();

            }catch (Exception ex){

                applicationMessage = "La proiezione non è presente nel sistema!";
                logger.log(Level.INFO, "Proiezione non presente");
                exist = false;
            }

            if(exist) {

                try {

                    for (Long item : proiezione_biglietti.keySet()) {

                        List<Ticket> tickets = new ArrayList<>();
                        Proiezione pro = proiezioneDAO.findByID(item.toString());
                        tickets.addAll(ticketDAO.getTicketByProiezione(pro));
                        proiezione_biglietti.replace(item, tickets);

                        Sala sala = salaDAO.findByID(pro.getSala().getSalaID());
                        proiezione_sala.replace(item, sala.getNumeroSala());
                    }




                } catch (Exception ex) {

                    applicationMessage = "Errore recupero ticket!";
                    logger.log(Level.INFO, "Errore recupero ticket per la proiezione selezionata");
                    exist = false;
                }
            }


            if (exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("proiezione_biglietti", proiezione_biglietti);
                request.setAttribute("proiezione_sala", proiezione_sala);
                request.setAttribute("data", data);
                request.setAttribute("ticketID", ticketID);
                request.setAttribute("titoloFilm", titoloFilm);
                request.setAttribute("viewUrl", "modificaTicket/salaModificaTicket");

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

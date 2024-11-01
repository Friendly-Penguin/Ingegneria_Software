package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.mo.Proiezione;
import com.cinema.cin.model.mo.Sala;
import com.cinema.cin.model.mo.Ticket;
import com.cinema.cin.model.mo.User;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalaManagement {

    private SalaManagement(){

    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

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
                    request.setAttribute("titoloFilm", titoloFilm);
                    request.setAttribute("viewUrl", "sala/view");

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

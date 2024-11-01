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

public class CarrelloManagement {


    private CarrelloManagement(){

    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        Carrello loggedCart;

        Boolean exist=true;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        List<MetodoPagamento> metodoPagamentos = new ArrayList<MetodoPagamento>();
        List<Ticket> tickets = new ArrayList<Ticket>();
        Abbonamento abbonamento = new Abbonamento();
        Abbonamento abbUser = new Abbonamento();
        Integer numeroOggetti = null;
        Integer numeroBiglietti = null;


        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
            loggedCart = sessionCarrelloDAO.findLoggedCartID();


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            MetodoPagamentoDAO metodoPagamentoDAO = daoFactory.getMetodoPagamentoDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();

            try{

                tickets = recuperaTicketsEFilmTitoloECostoProiezioneEData(daoFactory,sessionDAOFactory,request,loggedCart.getCarrello_id().toString());


            }catch (Exception e){

                applicationMessage = "Errore recupero tickets - carrello";
                logger.log(Level.INFO, "Errore recupero tickets - carrello");
                exist=false;

            }

            if(exist){

                try{

                    abbonamento = recuperaAbbonamento(daoFactory,sessionDAOFactory,request,loggedCart.getCarrello_id().toString());

                }catch (Exception e){

                    applicationMessage = "Errore recupero abbonamento - carrello";
                    logger.log(Level.INFO, "Errore recupero abbonamento - carrello");
                    exist=false;

                }

            }
            if(exist) {
                try {

                    metodoPagamentos.addAll(metodoPagamentoDAO.findByUser(loggedUser));

                } catch (Exception e) {

                    applicationMessage = "Errore recupero metodi pagamento - carrello";
                    logger.log(Level.INFO, "Errore recupero metodi pagamento - carrello");
                    exist = false;

                }
            }

            if(exist) {
                try {

                    abbUser = abbonamentoDAO.findAbbByUser(loggedUser);

                } catch (Exception e) {

                    applicationMessage = "Errore recupero abbonamento utente - carrello";
                    logger.log(Level.INFO, "Errore recupero abbonamento utente - carrello");
                    exist = false;

                }
            }

            if(exist){

                numeroOggetti = carrelloDAO.findCartItemsByCartID(loggedCart.getCarrello_id());
                numeroBiglietti = carrelloDAO.findCountOfTicketsByCartID(loggedCart.getCarrello_id());
                aggiornaCookie(daoFactory,sessionDAOFactory,request);
            }





            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("abbonamentoCarrello", abbonamento);
                request.setAttribute("abbonamentoUser", abbUser);
                request.setAttribute("numeroBiglietti", numeroBiglietti);
                request.setAttribute("biglietti", tickets);
                request.setAttribute("metodoPagamento", metodoPagamentos);
                request.setAttribute("numeroOggetti", numeroOggetti);
                request.setAttribute("viewUrl", "carrello/view");



            }else{

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                HomeManagement.view(request,response);



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

    public static void acquista(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Boolean exist = true;
        String applicationMessage = null;
        String carrelloID = request.getParameter("carrelloID");
        String abbonamentiUtilizzati = request.getParameter("AbbUtilizzati");

        List<Ticket> tickets = new ArrayList<Ticket>();
        Abbonamento abbonamento = new Abbonamento();
        Carrello carrello = new Carrello();

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



            TicketDAO ticketDAO = daoFactory.getTicketDAO();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();

            try {

                tickets = recuperaTicketsEFilmTitoloECostoProiezioneEData(daoFactory,sessionDAOFactory,request,carrelloID);


            } catch (Exception e) {

                applicationMessage = "Errore recupero tickets - carrello";
                logger.log(Level.INFO, "Errore recupero tickets - carrello");
                exist = false;

            }

            if (exist) {

                try {

                    abbonamento = recuperaAbbonamento(daoFactory,sessionDAOFactory,request,carrelloID);

                } catch (Exception e) {

                    applicationMessage = "Errore recupero abbonamento - carrello";
                    logger.log(Level.INFO, "Errore recupero abbonamento - carrello");
                    exist = false;

                }

            }

            if(exist && tickets.size() > 0){

                try {

                    for (int i = 0; i < tickets.size(); i++) {
                        ticketDAO.acquistaTickets(carrelloID, tickets.get(i).getTicket_id().toString());
                    }
                }catch (Exception e) {

                    applicationMessage = "Errore acquisto ticket - carrello";
                    logger.log(Level.INFO, "Errore acquisto ticket - carrello");
                    exist = false;

                }

            }

            if(exist && abbonamento != null){

                try {

                    abbonamentoDAO.acquistaAbbonamento(Long.parseLong(carrelloID), abbonamento.getAbbonamentoId());

                } catch (Exception e) {

                    applicationMessage = "Errore acquisto abbonamento - carrello";
                    logger.log(Level.INFO, "Errore acquisto abbonamento - carrello");
                    exist = false;

                }

            }

            if(exist){

                carrello.setCarrello_id(Long.parseLong(carrelloID));
                carrello.setItems(carrelloDAO.findCartItemsByCartID(carrello.getCarrello_id()));
                carrello.setNumeroTickets(carrelloDAO.findCountOfTicketsByCartID(carrello.getCarrello_id()));
                sessionCarrelloDAO.update(carrello);
            }

            if(!abbonamentiUtilizzati.equals("0")){
                Abbonamento abb = new Abbonamento();
                abb = abbonamentoDAO.findAbbByUser(loggedUser);
                Integer numero_rimasti = Integer.parseInt(abb.getNumAccessi()) - Integer.parseInt(abbonamentiUtilizzati);
                abb.setNumAccessi(numero_rimasti.toString());
                abbonamentoDAO.update(abb);
            }


            if (exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                applicationMessage = "Acquisto effettuato correttamente!";
                String destinazione = "HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione",destinazione);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");

            } else {

                String destinazione = "CarrelloManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("carrelloID", carrelloID);
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

    public static void cancella(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        int i;

        Boolean exist = true;
        String applicationMessage = null;
        String carrelloID = request.getParameter("carrelloID");
        String abbonamentoID = request.getParameter("abbonamentoID");

        String destinazione = null;
        List<Ticket> tickets = new ArrayList<Ticket>();
        List<String> bigliettiID = new ArrayList<>();
        List<MetodoPagamento> metodoPagamentos = new ArrayList<MetodoPagamento>();
        Abbonamento abbonamento = new Abbonamento();
        Carrello carrello = new Carrello();
        String valore;

        for(i = 1; i < 6; i++){
            valore = request.getParameter("biglietto"+i);
            if(!(valore.equals(""))){
                bigliettiID.add(valore);
            }
        }

        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();



            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();

            try{

                for(i = 0 ; i < bigliettiID.size(); i++){
                    carrelloDAO.rimuoviBigliettoByID(bigliettiID.get(i), carrelloID);
                }

            }catch (Exception ex){

                applicationMessage = "Errore rimozione elemento: " + i + " - carrello";
                logger.log(Level.INFO, "Errore rimozione elemento: " + i + " - carrello");
                exist = false;

            }

            if((!(abbonamentoID.equals(""))) && exist) {

                try {

                    abbonamento = abbonamentoDAO.findAbbByID(Long.parseLong(abbonamentoID));
                    abbonamentoDAO.delete(abbonamento);

                } catch (Exception ex) {

                    applicationMessage = "Errore rimozione abbonamento - carrello";
                    logger.log(Level.INFO, "Errore rimozione abbonamento - carrello");
                    exist = false;

                }

            }
            if(exist){

                carrello.setCarrello_id(Long.parseLong(carrelloID));
                carrello.setItems(carrelloDAO.findCartItemsByCartID(carrello.getCarrello_id()));
                carrello.setNumeroTickets(carrelloDAO.findCountOfTicketsByCartID(carrello.getCarrello_id()));
                sessionCarrelloDAO.update(carrello);
            }


            if(exist){

                applicationMessage="Elementi rimossi correttamente!";

                destinazione="CarrelloManagement.view";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("carrelloID", carrelloID);
                request.setAttribute("destinazione",destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");

            }else{

                destinazione="HomeManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("carrelloID", carrelloID);
                request.setAttribute("destinazione",destinazione);
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

    public static void viewAggiungiMetodo(HttpServletRequest request, HttpServletResponse response) {

       DAOFactory sessionDAOFactory = null;
       DAOFactory daoFactory = null;
       User loggedUser;

       String carrelloID = request.getParameter("carrelloID");
       Logger logger = LogService.getApplicationLogger();
       String destinazione = request.getParameter("destinazione");



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


           daoFactory.commitTransaction();
           sessionDAOFactory.commitTransaction();
           request.setAttribute("loggedOn", loggedUser != null);
           request.setAttribute("loggedUser", loggedUser);
           request.setAttribute("destinazione", destinazione);
           request.setAttribute("viewUrl", "carrello/metodoPagamento");



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

    public static void nuovoMetodo(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();
        String applicationMessage = null;

        String destinazione = request.getParameter("destinazione");
        String carrelloID = request.getParameter("carrelloID");
        String campoNumeroCarta = request.getParameter("campoNumeroCarta");
        String campoTitolare = request.getParameter("campoTitolare");
        String campoScadenza = request.getParameter("campoScadenza");
        String campoCVV = request.getParameter("campoCVV");
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

            MetodoPagamentoDAO metodoPagamentoDAO = daoFactory.getMetodoPagamentoDAO();


            try{

                metodoPagamentoDAO.create(campoNumeroCarta,campoScadenza,campoCVV,campoTitolare,loggedUser);

            }catch (Exception ex){
                applicationMessage = "Errore aggiunta metodo - carrello";
                logger.log(Level.INFO, "Errore aggiunta metodo - carrello");
                exist = false;
            }


            if(exist){

                applicationMessage="Metodo aggiunto correttamente!";

                if(destinazione.equals("null")){
                    destinazione = "CarrelloManagement.view";
                }

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("carrelloID", carrelloID);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");

            }else{

                applicationMessage="Metodo aggiunto correttamente!";
                destinazione = "CarrelloManagement.view";

                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("carrelloID", carrelloID);
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

    public static List<Ticket> recuperaTicketsEFilmTitoloECostoProiezioneEData(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request, String carrelloID) throws Exception {

        TicketDAO ticketDAO = daoFactory.getTicketDAO();
        FilmDAO filmDAO = daoFactory.getFilmDAO();
        ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
        List<Ticket> tickets = new ArrayList<Ticket>();

        try {
             tickets = ticketDAO.getTicketByCarrelloID(Long.parseLong(carrelloID));

            if(tickets !=null){

                for (int i=0; i < tickets.size(); i++){

                    tickets.get(i).getProiezione().setFilm(filmDAO.findByProiezioneID(tickets.get(i).getProiezione().getProie_id()));
                    tickets.get(i).getProiezione().setCosto(proiezioneDAO.findCostoByProiezioneID(tickets.get(i).getProiezione().getProie_id()));
                    tickets.get(i).getProiezione().setData(proiezioneDAO.findDataByID(tickets.get(i).getProiezione().getProie_id()));
                }
            }

        }catch (Exception ex){
            throw new RuntimeException();
        }
        return tickets;
    }

    public static Abbonamento recuperaAbbonamento(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request, String carrelloID) throws Exception{

        Abbonamento abbonamento = null;
        AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
        try{

            abbonamento = abbonamentoDAO.getAbbonamentoByCartID(Long.parseLong(carrelloID));

        }catch (Exception ex){
            throw new RuntimeException();
        }

        return abbonamento;

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

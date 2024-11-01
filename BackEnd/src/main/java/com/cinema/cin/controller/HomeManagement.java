package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.*;
import com.cinema.cin.services.logservice.LogService;
import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.cinema.cin.services.config.Configuration;


public class HomeManagement {

    private HomeManagement() {
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        List<Film> films;
        List<Proiezione> proieziones = new ArrayList<Proiezione>();

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
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();

            if(loggedUser != null){
                aggiornaCookie(daoFactory,sessionDAOFactory,request);
            }

            try {


                films = filmDAO.findByProiezione();

                try {
                    for (int i = 0; i < films.size(); i++) {
                        proieziones.addAll(proiezioneDAO.findByFilmID(films.get(i)));
                    }
                }catch (Exception ex){
                    throw new EmptyStackException();
                }
                request.setAttribute("proiezioni", proieziones);
                request.setAttribute("films", films);

            }catch (Exception ex){
                throw new RuntimeException();
            }

                    /*
                        ID_film: {
                            Giorno : [
                                {
                                    Ora: Proiezione
                                }
                            ]
                        }

                        ID_film : {
                            Giorno : {
                                Ora : [
                                    Proiezione
                                ]
                            }
                        }
                    */
            LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<String, ArrayList<Long>>>> filmMappa = new LinkedHashMap<>();

            for (int j = 0; j < proieziones.size(); j++) {
                Long FilmID = proieziones.get(j).getFilm().getFilmID();
                if (!(filmMappa.containsKey(FilmID))) {
                    LinkedHashMap<String, LinkedHashMap<String, ArrayList<Long>>> proiezioniMappa = new LinkedHashMap<>();
                    filmMappa.put(FilmID, proiezioniMappa);
                    for (int a = j; a < proieziones.size(); a++) {
                        if (FilmID == proieziones.get(a).getFilm().getFilmID()) {
                            String[] Data_Ora = proieziones.get(a).getData().split(" ");
                            if (filmMappa.get(FilmID).containsKey(Data_Ora[0])) {
                                if (!filmMappa.get(FilmID).get(Data_Ora[0]).containsKey(Data_Ora[1])) {
                                    ArrayList<Long> lista_proiezioni = new ArrayList<>();
                                    filmMappa.get(FilmID).get(Data_Ora[0]).put(Data_Ora[1], lista_proiezioni);
                                }
                                filmMappa.get(FilmID).get(Data_Ora[0]).get(Data_Ora[1]).add(proieziones.get(a).getProie_id());
                            } else {
                                LinkedHashMap<String, ArrayList<Long>> Ore = new LinkedHashMap<>();
                                ArrayList<Long> lista_proiezioni = new ArrayList<>();
                                lista_proiezioni.add(proieziones.get(a).getProie_id());
                                Ore.put(Data_Ora[1], lista_proiezioni);
                                filmMappa.get(FilmID).put(Data_Ora[0], Ore);
                            }
                        }
                    }
                }
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("mappa", filmMappa);
            request.setAttribute("films", films);
            request.setAttribute("viewUrl", "homeManagement/view");


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

    public static void viewAbbonamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

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

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "homeManagement/abbonamenti");



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

    public static void viewProssimamente(HttpServletRequest request, HttpServletResponse response){


        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();
        List<Film> films = new ArrayList<>();
        String applicationMessage = null;
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

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            try{

                films.addAll(filmDAO.findByNotProiezione());


            }catch (Exception ex){

                applicationMessage = "Errore recupero film no proieizone";
                logger.log(Level.SEVERE,"Errore recupero film no proieizone");
                exist = false;

            }

            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("listaFilm", films);
                request.setAttribute("viewUrl", "homeManagement/prossimamente");



            }else{

                String destinazione = "HomeManagement.view";

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

    public static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request) {

        List<Film> films;
        List<Proiezione> proieziones = new ArrayList<Proiezione>();


        try {
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();

            films = filmDAO.findByProiezione();

            try {
                for (int i = 0; i < films.size(); i++) {
                    proieziones.addAll(proiezioneDAO.findByFilmID(films.get(i)));
                }
            }catch (Exception ex){
                throw new EmptyStackException();
            }
            request.setAttribute("proiezioni", proieziones);
            request.setAttribute("films", films);

        }catch (Exception ex){
            throw new RuntimeException();
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


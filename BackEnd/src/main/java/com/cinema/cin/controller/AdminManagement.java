package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;
import com.cinema.cin.model.mo.*;
import com.cinema.cin.services.config.Configuration;
import com.cinema.cin.services.logservice.LogService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminManagement extends HttpServlet {

    private AdminManagement() {
    }

    //Gestione Accesso

    public static void logon(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();
        String email = request.getParameter("campoEmail");
        String password = request.getParameter("campoPassword");

        Boolean exist = true;

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();


            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            Admin loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, (Map) null);
            daoFactory.beginTransaction();


            AdminDAO adminDAO = daoFactory.getAdminDAO();
            Admin admin = adminDAO.findByAdminEmail(email);


            try {
                if ((admin != null && admin.getAdmin_password().equals(password) && admin.getDeleted() == false)) {
                    Boolean bol = null;
                    loggedAdmin = sessionAdminDAO.create(admin.getAdmin_name(),admin.getAdmin_email(), (String) null, admin.getAdmin_root());
                }else{
                    exist = false;
                    applicationMessage = "Password errata!";
                }
            }catch (Exception ex) {

                applicationMessage = "Errore creazione cookie - utente";
                logger.log(Level.SEVERE,"Errore creazione cookie - utente");
                exist = false;

            }

            if(exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "sezioneAdmin/view");

            }else{

                sessionAdminDAO.delete((Admin) null);
                loggedAdmin = null;
                String destinazione = "HomeManagement.view";
                applicationMessage = "Username e password errati! - ADMIN";

                request.setAttribute("loggedOn",loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");




            }


        } catch (Exception var22) {
            logger.log(Level.SEVERE, "Controller Error", var22);

            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }

                if (sessionDAOFactory != null) {
                    sessionDAOFactory.rollbackTransaction();
                }
            } catch (Throwable var21) {
            }

            throw new RuntimeException(var22);
        } finally {
            try {
                if (daoFactory != null) {
                    daoFactory.closeTransaction();
                }

                if (sessionDAOFactory != null) {
                    sessionDAOFactory.closeTransaction();
                }
            } catch (Throwable var20) {
            }

        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            sessionAdminDAO.delete((Admin) null);
            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            String destinazione = "HomeManagement.view";
            String applicationMessage = "A presto!";

            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", (Object) null);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("destinazione", destinazione);
            request.setAttribute("viewUrl", "pagineLanding/admin/confermaAggiunta");


        } catch (Exception var15) {
            logger.log(Level.SEVERE, "Controller Error", var15);

            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.rollbackTransaction();
                }
            } catch (Throwable var14) {
            }

            throw new RuntimeException(var15);
        } finally {
            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.closeTransaction();
                }
            } catch (Throwable var13) {
            }

        }

    }


    //Pagina principale

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedAdmin != null);
            request.setAttribute("loggedAdmin", loggedAdmin);
            request.setAttribute("viewUrl", "sezioneAdmin/view");


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


    //Seszione crea-Proiezione

    public static void nuovaProiezione(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();
        ArrayList<Film> listaFilm = new ArrayList<Film>();
        Boolean exist = true;
        String applicationMessage = null;
        Admin loggedAdmin;

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = (Admin) sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            try{

                listaFilm.addAll(filmDAO.findAll());

            }catch (Exception ex){

                applicationMessage = "Errore recupero film - nuovaPro";
                logger.log(Level.SEVERE,"Errore recupero film - nuovaPro");
                exist = false;

            }

            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("listaFilm", listaFilm);
                request.setAttribute("viewUrl", "sezioneAdmin/nuovaProiezione/view");

            }else{

                String destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");


            }








        } catch (Exception var15) {
            logger.log(Level.SEVERE, "Controller Error", var15);

            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.rollbackTransaction();
                }
            } catch (Throwable var14) {
            }

            throw new RuntimeException(var15);
        } finally {
            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.closeTransaction();
                }
            } catch (Throwable var13) {
            }

        }

    }

    public static void selezionaOrario(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();

        String data = request.getParameter("data");
        String dataReplace = data.replace("-","/");
        String[] parts = dataReplace.split("/");
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];
        String dataCorretta = day +"/" +month+ "/"+ year;
        String filmID = request.getParameter("filmSelezionato");
        String salaSelezionata = request.getParameter("salaSelezionata");

        Boolean exist = true;
        String applicationMessage = null;
        Admin loggedAdmin;

        Film film = new Film();
        String[] disponibili = new String[5];
        LinkedHashMap<String,Boolean> orari = new LinkedHashMap<>();
        orari.put("14:00", false);
        orari.put("16:00", false);
        orari.put("18:00", false);
        orari.put("20:00", false);
        orari.put("22:00", false);
        List<Proiezione> proiezioneList = new ArrayList<Proiezione>();

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = (Admin) sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();

            try{

            film = filmDAO.findById(filmID);

            }catch (Exception ex){

                applicationMessage = "Errore recupero film - orarioPro";
                logger.log(Level.SEVERE,"Errore recupero film - orarioPro");
                exist = false;

            }

            if(exist){
                try{

                    proiezioneList.addAll(proiezioneDAO.findByDataandSala(dataCorretta, Long.parseLong(salaSelezionata)));
                    for(int i = 0; i < proiezioneList.size(); i++){
                        String[] splitted = proiezioneList.get(i).getData().split(" ");

                        if(orari.containsKey(splitted[1])){
                            orari.put(splitted[1], true);
                        }

                    }

                }catch (Exception ex){

                    applicationMessage = "Errore recupero proiezioni - orarioPro";
                    logger.log(Level.SEVERE,"Errore recupero proiezioni - orarioPro");
                    exist = false;
                }
            }

            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("data",dataCorretta);
                request.setAttribute("sala", salaSelezionata);
                request.setAttribute("film", film);
                request.setAttribute("mappaOrari", orari);
                request.setAttribute("viewUrl", "sezioneAdmin/nuovaProiezione/selezionaOrario");

            }else{

                String destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");


            }








        } catch (Exception var15) {
            logger.log(Level.SEVERE, "Controller Error", var15);

            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.rollbackTransaction();
                }
            } catch (Throwable var14) {
            }

            throw new RuntimeException(var15);
        } finally {
            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.closeTransaction();
                }
            } catch (Throwable var13) {
            }

        }

    }

    public static void creaProiezione(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Logger logger = LogService.getApplicationLogger();

        String data = request.getParameter("data");
        String filmID = request.getParameter("filmID");
        String salaSelezionata = request.getParameter("sala");
        String orari = request.getParameter("orari");

        Boolean exist = true;
        String applicationMessage = null;
        Admin loggedAdmin;

        Film film = new Film();
        String[] orariSplitted = orari.split("-");



        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = (Admin) sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            SalaDAO salaDAO = daoFactory.getSalaDAO();

            try{

                for(int i= 0; i < orariSplitted.length; i++){


                    String dataCompleta = data + " " + orariSplitted[i];
                    proiezioneDAO.create(dataCompleta,filmDAO.findById(filmID),salaDAO.findByID(Long.parseLong(salaSelezionata)));
                }


            }catch (Exception ex){

                applicationMessage = "Errore creazione proiezioni - creaPro";
                logger.log(Level.SEVERE,"Errore creazione proiezioni - creaPro");
                exist = false;
            }


            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                String destinazione = "AdminManagement.view";
                applicationMessage = "Proiezioni create correttamente!";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/confermaAggiunta");

            }else{

                String destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");


            }








        } catch (Exception var15) {
            logger.log(Level.SEVERE, "Controller Error", var15);

            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.rollbackTransaction();
                }
            } catch (Throwable var14) {
            }

            throw new RuntimeException(var15);
        } finally {
            try {
                if (sessionDAOFactory != null) {
                    sessionDAOFactory.closeTransaction();
                }
            } catch (Throwable var13) {
            }

        }

    }


    //Sezione crea-Film

    public static void creaFilm(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedAdmin != null);
            request.setAttribute("loggedAdmin", loggedAdmin);
            request.setAttribute("viewUrl", "sezioneAdmin/aggiuntaFilm/view");


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

    public static void aggiungiFilm(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        String titolo = request.getParameter("titolo");
        String durata = request.getParameter("durata");
        String regista = request.getParameter("regista");
        String link_trailer = request.getParameter("link_trailer");
        String trama = request.getParameter("trama");
        String locandina = request.getParameter("nomeFile");
        String genere = request.getParameter("genere");


        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;
        String applicationMessage = null;
        String destinazione = null;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            try{

                filmDAO.create(durata,regista,titolo,link_trailer,locandina,trama,genere);

            }catch (Exception ex){

                applicationMessage = "Errore creazione film!";
                logger.log(Level.SEVERE,"Errore creazione film!");
                exist = false;

            }

            if(exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                destinazione = "AdminManagement.view";
                applicationMessage = "Film aggiunto correttamente!";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/confermaAggiunta");


            }else{

                destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");

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


    //Sezione gestisci-Recensioni

    public static void viewRew(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;
        String applicationMessage = null;
        List<Film> films = new ArrayList<>();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            FilmDAO filmDAO = daoFactory.getFilmDAO();

            try{

                films.addAll(filmDAO.findAll());

            }catch (Exception ex){

                applicationMessage = "Errore recupero film - gestioneRew";
                logger.log(Level.SEVERE,"Errore recupero film - gestioneRew!");
                exist = false;

            }

            if(exist) {


                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("viewUrl", "sezioneAdmin/gestioneRew/view");
                request.setAttribute("listaFilm", films);

            }else{

                String destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");
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

    public static void gestisciRew(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();
        String applicationMessage = null;

        String filmID = request.getParameter("filmSelezionato");
        List<Review> reviews = new ArrayList<>();
        Boolean exist = true;
        Film film = new Film();


        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();


                try {


                    film = filmDAO.findById(filmID);
                    reviews.addAll(reviewDAO.findByFilmID(film));

                } catch (Exception ex) {

                    applicationMessage = "Errore recupero recesioni - gestioneRew";
                    logger.log(Level.SEVERE, "Errore recupero recensioni - gestioneRew!");
                    exist = false;

                }

                if(exist){

                    try{

                        for (int i = 0; i < reviews.size(); i++) {

                            reviews.get(i).setFilm(filmDAO.findById(reviews.get(i).getFilm().getFilmID().toString()));

                        }


                    }catch (Exception ex){

                        applicationMessage = "Errore recupero recesioni2 - gestioneRew";
                        logger.log(Level.SEVERE, "Errore recupero recensioni2 - gestioneRew!");
                        exist = false;

                    }
                }

            if(exist) {


                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("listaRew", reviews);
                request.setAttribute("titoloFilm", film.getTitolo());
                request.setAttribute("viewUrl", "sezioneAdmin/gestioneRew/viewRew");


            }else{

                String destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");
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

    public static void cancellaRew(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        String listaID = request.getParameter("stringaID");


        Boolean exist = true;
        int i = 0;
        String applicationMessage = null;
        String destinazione = null;
        String[] rewID = listaID.split("-");

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            ReviewDAO reviewDAO = daoFactory.getReviewDAO();

            try{

                for(i = 0; i < rewID.length ; i++){

                    reviewDAO.delete(reviewDAO.findByID(rewID[i]));

                }


            }catch (Exception ex){

                applicationMessage = "Errore cancellazione recensione: " + i;
                logger.log(Level.SEVERE, "Errore cancellazione recensione: " + i);
                exist = false;

            }

            if(exist){

                destinazione = "AdminManagement.view";
                applicationMessage = "Recensioni rimosse correttamente!";

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/confermaAggiunta");


            }else{

                destinazione = "AdminManagement.view";
                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");

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

    //Sezione gestisci-Sala

    public static void viewStatoSala(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();
        Boolean exist = true;
        String applicationMessage = null;
        ArrayList<Ticket> biglietti = new ArrayList<>();
        ArrayList<String> orari = new ArrayList<>();
        orari.add("14:00");
        orari.add("16:00");
        orari.add("18:00");
        orari.add("20:00");
        orari.add("22:00");
        String campoData = request.getParameter("campoData");

        ArrayList<Sala> sale = new ArrayList<>();
        /*
            Giorno : {Sala: {Ora: Proiezione}}}
        */
        LinkedHashMap<String, LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>>>> giorno_sala_ora_pro = new LinkedHashMap<>();

        if (campoData != null) {

                // Data specifica (es. 2023-07-01)
                String[] anno_mese_giorno = campoData.split("-");

                LocalDate specificDate = LocalDate.of(Integer.parseInt(anno_mese_giorno[0]),Integer.parseInt(anno_mese_giorno[1]), Integer.parseInt(anno_mese_giorno[2]));

                List<String> giorniDellaSettimana = new ArrayList<>();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Ottieni il giorno della settimana corrispondente alla data
                DayOfWeek dayOfWeek = specificDate.getDayOfWeek();

                // Calcola la data del lunedì della settimana corrente
                LocalDate startOfWeek = specificDate.minusDays(dayOfWeek.getValue() - 1);

                // Aggiungi i giorni della settimana alla lista
                for (int i = 0; i < DayOfWeek.values().length; i++) {
                    LocalDate date = startOfWeek.plusDays(i);
                    String formattedDate = date.format(formatter);
                    LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>>> sala_ora_pro = new LinkedHashMap<>();
                    giorno_sala_ora_pro.put(formattedDate, sala_ora_pro);
                }

        } else {

            LocalDate today = LocalDate.now();
            // Ottiene il giorno della settimana corrente (Es. Lunedi)
            DayOfWeek dayOfWeek = today.getDayOfWeek();

            // Calcola la data del lunedi della settimana corrente
            LocalDate startOfWeek = today.minusDays(dayOfWeek.getValue() - 1);

            // Formatta la data con un formato specifico (opzionale)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Stampa i giorni della settimana
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                LocalDate date = startOfWeek.plusDays(i);
                String formattedDate = date.format(formatter);
                LinkedHashMap<Long, LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>>> sala_ora_pro = new LinkedHashMap<>();
                giorno_sala_ora_pro.put(formattedDate, sala_ora_pro);
            }
        }

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            ProiezioneDAO proiezioneDAO = daoFactory.getProiezioneDAO();
            SalaDAO salaDAO = daoFactory.getSalaDAO();
            FilmDAO filmDAO = daoFactory.getFilmDAO();
            TicketDAO ticketDAO = daoFactory.getTicketDAO();


                try {

                    sale.addAll(salaDAO.findAll());

                } catch (Exception ex) {

                    applicationMessage = "Errore recupero sale";
                    logger.log(Level.SEVERE, "Errore recupero sale");
                    exist = false;
                }

            // Ciclo per le sale
                for (String giorno: giorno_sala_ora_pro.keySet()) {
                    for (int i = 0; i < sale.size(); i++) {
                        giorno_sala_ora_pro.get(giorno).put(sale.get(i).getSalaID(), null);
                    }
                }
                for (String giorno: giorno_sala_ora_pro.keySet()) {
                    for (Long sala_id: giorno_sala_ora_pro.get(giorno).keySet()) {
                        LinkedHashMap<String, LinkedHashMap<Proiezione, ArrayList<Ticket>>> ora_pro = new LinkedHashMap<>();
                        giorno_sala_ora_pro.get(giorno).put(sala_id, ora_pro);
                        for (int i = 0; i < orari.size(); i++) {
                            giorno_sala_ora_pro.get(giorno).get(sala_id).put(orari.get(i), null);
                        }
                    }
                }
                if(exist) {
                    try {
                        for (String giorno : giorno_sala_ora_pro.keySet()) {
                            for (Long sala_id : giorno_sala_ora_pro.get(giorno).keySet()) {
                                //recupero per ogni Giorno - Sala tutte le proiezioni
                                List<Proiezione> pro = proiezioneDAO.findByDataandSala(giorno, sala_id);
                                for (int k = 0; k < pro.size(); k++) {
                                    //setto tutti i titoli alle proiezioni
                                    pro.get(k).getFilm().setTitolo(filmDAO.findTitoloByID(pro.get(k).getFilm().getFilmID()));
                                }
                                for (int i = 0; i < pro.size(); i++) {
                                    LinkedHashMap<Proiezione, ArrayList<Ticket>> pro_ticket = new LinkedHashMap<>();
                                    Proiezione proiezione = pro.get(i);
                                    String[] data_ora = proiezione.getData().split(" ");
                                    ArrayList<Ticket> tickets_pro = new ArrayList<>(ticketDAO.getTicketByProiezione(pro.get(i)));
                                    pro_ticket.put(proiezione, tickets_pro);
                                    giorno_sala_ora_pro.get(data_ora[0]).get(sala_id).put(data_ora[1], pro_ticket);
                                }
                            }
                        }
                    } catch (Exception ex) {

                        applicationMessage = "Errore recupero proieizioni";
                        logger.log(Level.SEVERE, "Errore recupero proieizioni");
                        exist = false;

                    }
                }



                if (exist) {

                    daoFactory.commitTransaction();
                    sessionDAOFactory.commitTransaction();

                    request.setAttribute("loggedOn", loggedAdmin != null);
                    request.setAttribute("loggedAdmin", loggedAdmin);
                    request.setAttribute("mappa", giorno_sala_ora_pro);
                    request.setAttribute("biglietti", biglietti);
                    request.setAttribute("sale", sale);
                    request.setAttribute("viewUrl", "sezioneAdmin/statoSala/view");

                } else {

                    String destinazione = "AdminManagement.view";

                    request.setAttribute("loggedOn", loggedAdmin != null);
                    request.setAttribute("loggedAdmin", loggedAdmin);
                    request.setAttribute("destinazione", destinazione);
                    request.setAttribute("applicationMessage", applicationMessage);
                    request.setAttribute("viewUrl", "sezioneAdmin/pagineLanding/admin/rifiutaAggiunta");


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

    //Sezione aggiungi utenti

    public static void  viewAggiungiAdmin(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedAdmin != null);
            request.setAttribute("loggedAdmin", loggedAdmin);
            request.setAttribute("viewUrl", "sezioneAdmin/aggiuntaAdmin/view");


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

    public static void nuovoAdmin(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Admin loggedAdmin;

        String email = request.getParameter("campoEmail");
        String nome = request.getParameter("campoNome");
        String passowrd = request.getParameter("campoPassword");
        String rootOption = request.getParameter("rootOption");

        Logger logger = LogService.getApplicationLogger();
        String applicationMessage = null;
        String destinazione = null;
        Boolean exist = true;
        Boolean opzioneRoot = false;

        if(rootOption != null) {
            if (rootOption.equals("true")) {
                opzioneRoot = true;
            }else{
                opzioneRoot = false;
            }
        }

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AdminDAO sessionAdminDAO = sessionDAOFactory.getAdminDAO();
            loggedAdmin = sessionAdminDAO.findLoggedAdmin();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();
            AdminDAO adminDAO = daoFactory.getAdminDAO();

            try{

                adminDAO.create(nome,email,passowrd,opzioneRoot);

            }catch (Exception ex){

                applicationMessage = "Errore creazione nuovo admin";
                logger.log(Level.SEVERE,"Errore creazione nuovo admin");
                exist = false;

            }

            if(exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                destinazione = "AdminManagement.view";
                applicationMessage = "ADMIN aggiunto correttamente!";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/confermaAggiunta");

            }else{

                destinazione = "AdminManagement.view";

                request.setAttribute("loggedOn", loggedAdmin != null);
                request.setAttribute("loggedAdmin", loggedAdmin);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/admin/rifiutaAggiunta");


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

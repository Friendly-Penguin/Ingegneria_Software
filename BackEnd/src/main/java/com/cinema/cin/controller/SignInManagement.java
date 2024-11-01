package com.cinema.cin.controller;

import com.cinema.cin.model.dao.*;

import java.security.*;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.services.logservice.LogService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cinema.cin.model.mo.User;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.cinema.cin.services.config.Configuration;

public class SignInManagement {

    private SignInManagement() {

    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

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
            request.setAttribute("viewUrl", "signIn/view");


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

    public static void viewAdmin(HttpServletRequest request, HttpServletResponse response) {

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
            request.setAttribute("viewUrl", "signIn/viewAdmin");


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

    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        Carrello carrello = null;
        Logger logger = LogService.getApplicationLogger();
        String email = request.getParameter("campoEmail");
        String password = request.getParameter("campoPassword");
        Integer items = 0;
        Boolean exist = true;

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();


            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            CarrelloDAO sessionCarrelloDAO = sessionDAOFactory.getCarrelloDAO();
            User loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, (Map) null);
            daoFactory.beginTransaction();

            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserEmail(email);

            try {
                if ((user != null && user.getPassword().equals(password) && user.getDeleted() == false)) {
                    Boolean bol = null;
                    loggedUser = sessionUserDAO.create(user.getEmail(), user.getNome(), user.getCognome(), (String) null, user.getMatricola(), (String) null, user.getConfermaEmail());
                    carrello = carrelloDAO.findByUserEmail(loggedUser.getEmail());
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

                try {
                    if (carrello.getCarrello_id() != null) {

                        items = carrelloDAO.findCartItemsByCartID(carrello.getCarrello_id());
                        carrello.setItems(items);
                        carrello.setNumeroTickets(carrelloDAO.findCountOfTicketsByCartID(carrello.getCarrello_id()));
                        sessionCarrelloDAO.create2(carrello);

                    } else {

                        carrello = carrelloDAO.create(loggedUser);
                        items = carrelloDAO.findCartItemsByCartID(carrello.getCarrello_id());
                        carrello.setItems(items);
                        carrello.setNumeroTickets(carrelloDAO.findCountOfTicketsByCartID(carrello.getCarrello_id()));
                        sessionCarrelloDAO.create2(carrello);

                    }

                } catch (Exception ex) {

                    applicationMessage = "Errore creazione cookie - carrello";
                    logger.log(Level.SEVERE, "Errore creazione cookie - carrello");
                    exist = false;


                }
            }

            if(exist) {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                String destinazione ="HomeManagement.view";
                applicationMessage = "Benvenuto " + loggedUser.getNome() + "!";
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");

            }else{

                String destinazione = "HomeManagement.view";

                sessionUserDAO.delete((User) null);
                loggedUser = null;
                applicationMessage = "Username e password errati!";
                request.setAttribute("loggedOn", loggedUser != null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("viewUrl", "pagineLanding/rifiutaAggiunta");




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

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            sessionUserDAO.delete((User) null);
            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            String destinazione = "HomeManagement.view";
            String applicationMessage = "A presto!";

            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", (Object) null);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("destinazione", destinazione);
            request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");


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

    public static void create(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        String email = request.getParameter("campoEmail");
        String password = request.getParameter("campoPassword");
        String nome = request.getParameter("campoNome");
        String cognome = request.getParameter("campoCognome");
        String matricola = request.getParameter("campoMatricola");
        String destinazione = null;

        String confermaEmail = null;
        Boolean exist = true;

        try {
            Map sessionFactoryParameters = new HashMap();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory("CookieImpl", sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();


            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            User loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory("MySQLJDBCImpl", (Map) null);
            daoFactory.beginTransaction();

            if(matricola.isEmpty()){
                matricola = "null";
            }

            UserDAO userDAO = daoFactory.getUserDAO();
            String token_ID = creaToken();

            try {

                userDAO.create(email, nome, cognome, password, matricola, token_ID, confermaEmail);

            } catch (Exception e) {

                applicationMessage = "Utente già registrato!";
                logger.log(Level.INFO, "Tentativo di inserimento di un utente già esistente");
                exist = false;
            }

            if (exist) {

                try {

                    sendConfirmationEmail(email, token_ID);


                } catch (Exception e) {
                    exist = false;
                    applicationMessage = "Errore invio email!";
                    logger.log(Level.INFO, "Tentativo di invio email fallito!");
                }

            }

            if (exist){

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();
                applicationMessage = "Utente registrato!<br>Controlla la mail per confermare il tuo account!";
                destinazione = "HomeManagement.view";

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/confermaAggiunta");


            }else{

                destinazione = "HomeManagement.view";

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("destinazione", destinazione);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "pagineLanding/rifiutaAggiunta");


            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
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

    private static String creaToken(){

        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(randomIndex));
        }

        return token.toString();
    }

    private static void sendConfirmationEmail(String email_to, String token) throws Exception {


        Properties props = new java.util.Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cinema20002@gmail.com", "wygm hkfb omps uedt");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("cinema20002@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
        message.setSubject("Conferma il tuo account");
        String htmlContent = "<html>"
                + "<body>"
                + "<div style='border: 1px solid black; padding: 20px; max-width: 600px; margin: auto;'>"
                + "<h1 style='text-align: center; color:red; text-shadow: 2px 2px 5px black;'>Cineplex</h1>"
                + "<h2 style='text-align: center;'>Conferma Account</h2>"
                + "<p style='text-align: center;'>Grazie per esserti registrato!<br> </p>"
                + "<p style='text-align: center;'>Per confermare il tuo account inserisci il seguente token nell'apposita sezione:<br></p>"
                + "<p style='text-align: center; color: black; font-size:20px;'>\"Account\\Attivazione\"</p>"
                + "<div style='background-color: #f0f0f0; border: 1px dashed #ccc; padding: 10px; text-align: center;'>"
                + "<code style='font-size: 20px;'>" + token + "</code>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        // Impostazione del contenuto del messaggio
        message.setContent(htmlContent, "text/html; charset=utf-8");

        // Invio del messaggio
        Transport.send(message);
    }
}
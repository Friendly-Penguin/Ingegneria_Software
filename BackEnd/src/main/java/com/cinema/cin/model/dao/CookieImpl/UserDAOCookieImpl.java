package com.cinema.cin.model.dao.CookieImpl;

import com.cinema.cin.model.dao.UserDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public User create(String email,
                       String nome,
                       String cognome,
                       String password,
                       String matricola,
                       String token_ID,
                       String confermaEmail) throws DuplicatedObjectException {

        User loggedUser = new User();
        loggedUser.setNome(nome);
        loggedUser.setCognome(cognome);
        loggedUser.setEmail(email);
        loggedUser.setConfermaEmail(confermaEmail);
        if(matricola != null) {
            loggedUser.setMatricola("true");
        }else{
            loggedUser.setMatricola("false");
        }


        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public void update(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(User loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public User findLoggedUser() {

        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    @Override
    public User findByUserEmail(String str) {
        return null;
    }

    @Override
    public boolean confermaAccount(User usr, String str) {return false;}


    private String encode(User loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getNome() + "#" + loggedUser.getCognome() + "#" + loggedUser.getEmail() + "#" + loggedUser.getConfermaEmail() + "#" + loggedUser.getMatricola();
        return encodedLoggedUser;

    }

    private User decode(String encodedLoggedUser) {

        User loggedUser = new User();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setNome(values[0]);
        loggedUser.setCognome(values[1]);
        loggedUser.setEmail(values[2]);
        loggedUser.setConfermaEmail(values[3]);
        loggedUser.setMatricola(values[4]);

        return loggedUser;

    }
}

package com.cinema.cin.model.dao.CookieImpl;

import com.cinema.cin.model.dao.AdminDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Admin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminDAOCookieImpl implements AdminDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public AdminDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public Admin create(String nome, String email, String password, Boolean root) throws DuplicatedObjectException {

        Admin loggedAdmin = new Admin();
        loggedAdmin.setAdmin_name(nome);
        loggedAdmin.setAdmin_email(email);
        loggedAdmin.setAdmin_root(root);


        Cookie cookie;
        cookie = new Cookie("loggedAdmin", encode(loggedAdmin));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedAdmin;

    }

    @Override
    public void update(Admin loggedAdmin) throws DuplicatedObjectException {

        Cookie cookie;
        cookie = new Cookie("loggedAdmin", encode(loggedAdmin));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(Admin loggedAdmin) {

        Cookie cookie;
        cookie = new Cookie("loggedAdmin", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public Admin findLoggedAdmin() {

        Cookie[] cookies = request.getCookies();
        Admin loggedAdmin = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedAdmin == null; i++) {
                if (cookies[i].getName().equals("loggedAdmin")) {
                    loggedAdmin = decode(cookies[i].getValue());
                }
            }
        }

        return loggedAdmin;

    }

    @Override
    public Admin findByAdminEmail(String email) {
        return null;
    }

    private String encode(Admin loggedAdmin) {

        String encodedLoggedAdmin;
        encodedLoggedAdmin = loggedAdmin.getAdmin_name() + "#" + loggedAdmin.getAdmin_email() + "#" + loggedAdmin.getAdmin_root();
        return encodedLoggedAdmin;

    }

    private Admin decode(String endodedLoggedAdmin) {

        Admin loggedAdmin = new Admin();

        String[] values = endodedLoggedAdmin.split("#");

        loggedAdmin.setAdmin_name(values[0]);
        loggedAdmin.setAdmin_email(values[1]);
        loggedAdmin.setAdmin_root(Boolean.parseBoolean(values[2]));


        return loggedAdmin;

    }
}




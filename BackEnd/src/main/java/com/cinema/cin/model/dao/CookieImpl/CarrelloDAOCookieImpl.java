package com.cinema.cin.model.dao.CookieImpl;

import com.cinema.cin.model.dao.CarrelloDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Carrello;
import com.cinema.cin.model.mo.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CarrelloDAOCookieImpl implements CarrelloDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public CarrelloDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public Carrello create(User user) throws DuplicatedObjectException {

        return null;

    }


    public Carrello create2(Carrello carrello){

        Carrello cart = new Carrello();
        cart.setCarrello_id(carrello.getCarrello_id());
        cart.setItems(carrello.getItems());
        cart.setNumeroTickets(carrello.getNumeroTickets());


        Cookie cookie;
        cookie = new Cookie("cart", encode(cart));
        cookie.setPath("/");
        response.addCookie(cookie);

        return cart;
    }


    @Override
    public void update(Carrello carrello) {

        Cookie cookie;
        cookie = new Cookie("cart", encode(carrello));
        cookie.setPath("/");
        response.addCookie(cookie);

    }


    @Override
    public void delete(Carrello carrello) {

        Cookie cookie;
        cookie = new Cookie("cart", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }


    private String encode(Carrello carrello) {

        String encodedCarrello;
        encodedCarrello = carrello.getCarrello_id() + "#" + carrello.getItems() + "#" + carrello.getNumeroTickets();
        return encodedCarrello;

    }

    private Carrello decode(String encodedCarrello) {

        Carrello cart = new Carrello();

        String[] values = encodedCarrello.split("#");

        cart.setCarrello_id(Long.parseLong(values[0]));
        cart.setItems(Integer.parseInt(values[1]));
        cart.setNumeroTickets(Integer.parseInt(values[2]));

        return cart;

    }

    @Override
    public Carrello findByUserEmail(String email) {
        return null;
    }

    @Override
    public Integer findCartItemsByCartID(Long carrello_id) {
        return 0;
    }

    @Override
    public Carrello findByID(Long carrello_id) {
        return null;
    }

    public Carrello findLoggedCartID(){

        Cookie[] cookies = request.getCookies();
        Carrello carrello = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && carrello == null; i++) {
                if (cookies[i].getName().equals("cart")) {
                    carrello = decode(cookies[i].getValue());
                }
            }
        }

        return carrello;

    }

    @Override
    public void rimuoviBigliettoByID(String articoloID, String carrelloID) throws DuplicatedObjectException {

    }


    @Override
    public Integer findCountOfTicketsByCartID(Long carrello_id) {
        return 0;
    }
}



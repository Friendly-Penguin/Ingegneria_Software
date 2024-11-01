package com.cinema.cin.model.mo;

public class User {

    private String email;
    private String nome;
    private String cognome;
    private String password;
    private String matricola;
    private String numero_tessera;
    private String token_ID;
    private boolean deleted;
    private String confermaEmail;

    /* 1:N */
    private Ticket[] ticket;
    private Review[] review;
    private MetodoPagamento[] metodo;

    /* 1:1 */
    private Abbonamento abb;
    private Carrello cart;


    /*Getter and Setter*/

    public void setCart(Carrello cart) {
        this.cart = cart;
    }

    public void setToken_ID(String token_ID) {
        this.token_ID = token_ID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setConfermaEmail(String confermaEmail) {
        this.confermaEmail = confermaEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public void setNumero_tessera(String numero_tessera) {
        this.numero_tessera = numero_tessera;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setTicket(Ticket[] ticket) {
        this.ticket = ticket;
    }

    public void setReview(Review[] review) {
        this.review = review;
    }

    public void setAbb(Abbonamento abb) {
        this.abb = abb;
    }


    public void setMetodo(MetodoPagamento[] metodo) {
        this.metodo = metodo;
    }

    public String getConfermaEmail() {
        return confermaEmail;
    }

    public String getToken_ID() {
        return token_ID;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }


    public String getPassword() {
        return password;
    }

    public String getMatricola() {
        return matricola;
    }

    public String getNumero_tessera() {
        return numero_tessera;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public Ticket getTicket(int index) {
        return ticket[index];
    }

    public Review getReview(int index) {
        return review[index];
    }

    public Abbonamento getAbb() {
        return abb;
    }


    public MetodoPagamento getMetodo(int index) {
        return metodo[index];
    }

    public Carrello getCart() {
        return cart;
    }
}


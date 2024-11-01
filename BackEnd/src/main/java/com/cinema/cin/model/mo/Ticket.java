package com.cinema.cin.model.mo;

public class Ticket {


    private Long ticket_id;
    private String n_colonna;
    private String n_riga;
    private boolean comprato;
    private boolean deleted;


    /* 1:1 */
    private Proiezione proiezione;

    /* N:1 */
    private User usr;
    private Carrello cart;

    public void setCart(Carrello cart) {
        this.cart = cart;
    }

    public void setTicket_id(Long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public void setN_colonna(String n_colonna) {
        this.n_colonna = n_colonna;
    }

    public void setN_riga(String n_riga) {
        this.n_riga = n_riga;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setProiezione(Proiezione proiezione) {
        this.proiezione = proiezione;
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }

    public void setComprato(boolean comprato) {
        this.comprato = comprato;
    }

    public Long getTicket_id() {
        return ticket_id;
    }

    public boolean isComprato() {
        return comprato;
    }

    public String getN_colonna() {
        return n_colonna;
    }

    public String getN_riga() {
        return n_riga;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public User getUsr() {
        return usr;
    }

    public Carrello getCart() {
        return cart;
    }
}

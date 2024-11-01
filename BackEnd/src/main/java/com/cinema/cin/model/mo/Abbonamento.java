package com.cinema.cin.model.mo;

public class Abbonamento {
    private Long abbonamentoId;
    private String numAccessi;
    private Float prezzo;
    private boolean deleted;

    /* 1:1 */
    private User user;
    private Carrello cart;

    public Long getAbbonamentoId() {
        return abbonamentoId;
    }

    public void setAbbonamentoId(Long abbonamentoId) {
        this.abbonamentoId = abbonamentoId;
    }

    public void setCart(Carrello cart) {
        this.cart = cart;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public void setNumAccessi(String numAccessi) {
        this.numAccessi = numAccessi;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNumAccessi() {
        return numAccessi;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getUser() {
        return user;
    }

    public Carrello getCart() {
        return cart;
    }
}

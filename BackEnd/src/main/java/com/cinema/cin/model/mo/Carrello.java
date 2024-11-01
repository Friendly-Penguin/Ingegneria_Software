package com.cinema.cin.model.mo;

public class Carrello {

    private Long carrello_id;
    private Integer items;
    private Integer numeroTickets;
    private boolean deleted;

    /* 1:N */
    private Ticket[] tickets;

    /* 1:1 */
    private User user;
    private Abbonamento abbonamento;

    public void setNumeroTickets(Integer numeroTickets) {
        this.numeroTickets = numeroTickets;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCarrello_id(Long carrello_id) {
        this.carrello_id = carrello_id;
    }

    public void setTickets(Ticket[] tickets) {
        this.tickets = tickets;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAbbonamento(Abbonamento abbonamento) {
        this.abbonamento = abbonamento;
    }

    public Integer getItems() {
        return items;
    }

    public Integer getNumeroTickets() {
        return numeroTickets;
    }

    public Abbonamento getAbbonamento() {
        return abbonamento;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getUser() {
        return user;
    }

    public Ticket getTickets(int index) {
        return tickets[index];
    }

    public Long getCarrello_id() {
        return carrello_id;
    }
}

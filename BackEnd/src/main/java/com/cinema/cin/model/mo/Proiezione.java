package com.cinema.cin.model.mo;

public class Proiezione {

    private Long proie_id;
    private String data;
    private boolean deleted;
    private Float costo;


    /* N:1 */
    private Film film;

    /* 1:1 */
    private Sala sala;
    private Ticket ticket;

    public void setProie_id(Long proie_id) {
        this.proie_id = proie_id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Long getProie_id() {
        return proie_id;
    }

    public String getData() {
        return data;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public Float getCosto() {
        return costo;
    }

    public Film getFilm() {
        return film;
    }

    public Sala getSala() {
        return sala;
    }

    public Ticket getTicket() {
        return ticket;
    }
}

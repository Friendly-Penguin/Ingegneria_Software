package com.cinema.cin.model.mo;

public class Review {

    private Long rew_id;
    private String data;
    private String voto;
    private String testo;
    private String titolo;
    private boolean deleted;


    /* N:1 */
    private User usr;
    private Film film;

    public void setTitolo(String titolo) {this.titolo = titolo;}

    public void setRew_id(Long rew_id) {
        this.rew_id = rew_id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }

    public void setFilm(Film film) {
        this.film = film;
    }


    public String getTitolo() {return titolo;}

    public Long getRew_id() {
        return rew_id;
    }

    public String getData() {
        return data;
    }

    public String getVoto() {
        return voto;
    }

    public String getTesto() {
        return testo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getUsr() {
        return usr;
    }

    public Film getFilm() {
        return film;
    }
}

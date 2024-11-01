package com.cinema.cin.model.mo;

public class Film {

    private Long filmID;
    private String durata;
    private String regista;
    private String titolo;
    private String link_trailer;
    private String locandina;
    private String genere;
    private String trama;
    private boolean deleted;

    /* 1:N */
    private Proiezione[] proiezione;
    private Review[] reviews;

    /* 1:1 */

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public void setFilm_id(Long filmID) {
        this.filmID = filmID;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setLink_trailer(String link_trailer) {
        this.link_trailer = link_trailer;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setProiezione(Proiezione[] proiezione) {
        this.proiezione = proiezione;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public void setLocandina(String img) {
        this.locandina = img;
    }

    public Long getFilmID() {
        return filmID;
    }

    public String getDurata() {
        return durata;
    }

    public String getRegista() {
        return regista;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getLink_trailer() {
        return link_trailer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Proiezione getProiezione(int index) {
        return proiezione[index];
    }

    public Review getReviews(int index) {
        return reviews[index];
    }

    public String getLocandina() {
        return this.locandina;
    }
}

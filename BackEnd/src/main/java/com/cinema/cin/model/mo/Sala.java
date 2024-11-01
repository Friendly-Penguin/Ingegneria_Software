package com.cinema.cin.model.mo;

public class Sala {

    private Long salaID;
    private String numeroSala;
    private String numeroPosti;
    private boolean deleted;

    /* N:1 */

    public void setSalaID(Long salaID) {
        this.salaID = salaID;
    }

    public void setNumeroSala(String numeroSala) {
        this.numeroSala = numeroSala;
    }


    public void setNumeroPosti(String numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public Long getSalaID() {
        return salaID;
    }

    public String getNumeroSala() {
        return numeroSala;
    }


    public String getNumeroPosti() {
        return numeroPosti;
    }

    public boolean isDeleted() {
        return deleted;
    }

}

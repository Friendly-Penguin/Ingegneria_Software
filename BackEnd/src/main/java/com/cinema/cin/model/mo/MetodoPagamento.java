package com.cinema.cin.model.mo;

public class MetodoPagamento {
    private Long metodo_id;
    private String N_Carta;
    private String DataScad;
    private String CVV;
    private String titolare;
    private boolean deleted;

    /* N:1 */
    private User user;

    public void setTitolare(String titolare) {
        this.titolare = titolare;
    }

    public void setMetodo_id(Long metodo_id) {
        this.metodo_id = metodo_id;
    }

    public void setN_Carta(String numCarta) {
        N_Carta = numCarta;
    }

    public void setDataScad(String dataScad) {
        DataScad = dataScad;
    }

    public void setCVV(String ccv) {
        this.CVV = ccv;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getMetodo_id() {
        return metodo_id;
    }

    public String getTitolare() {
        return titolare;
    }

    public String getN_Carta() {
        return N_Carta;
    }

    public String getDataScad() {
        return DataScad;
    }

    public String getCVV() {
        return CVV;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getUser() {
        return user;
    }
}

package com.cinema.cin.model.dao;

import com.cinema.cin.model.mo.Sala;

import java.util.ArrayList;

public interface SalaDAO {
    public Sala create(
            Long salaID,
            String numeroSala,
            String numeroPosti
    );

    public void update(Sala sala);

    public void delete(Sala sala);

    public Sala findByID(Long salaID);

    public ArrayList<Sala> findAll();

}

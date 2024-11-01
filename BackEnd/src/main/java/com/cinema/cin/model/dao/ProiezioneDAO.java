package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Proiezione;
import com.cinema.cin.model.mo.Sala;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProiezioneDAO {

    public Proiezione create(
            String data,
            Film film,
            Sala sala
    ) throws DuplicatedObjectException;

    public void update(Proiezione pro);

    public void delete(Proiezione pro);

    public List<Proiezione> findByFilmID(Film film);

    public Proiezione findByID(String ID);

    public Float findCostoByProiezioneID(Long ID);

    public String findDataByID(Long ID);

    public List<Proiezione> findByDataandSala(String data, Long salaID);

}

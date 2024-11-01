package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Film;

import java.util.ArrayList;
import java.util.List;

public interface FilmDAO {
    public Film create(
            String durata,
            String regista,
            String titolo,
            String link_trailer,
            String locandina,
            String descrizione,
            String genere
            ) throws DuplicatedObjectException;

    public void update(Film film);

    public void delete(Film film);


    public Film findByTitolo(String titolo);

    public Film findById(String ID);

    public List<Film> findByProiezione();

    public String findTitoloByID(Long ID);

    public Film findByProiezioneID(Long proiezione_id);

    public List<Film> findAll();

    public List<Film> findByNotProiezione();


}

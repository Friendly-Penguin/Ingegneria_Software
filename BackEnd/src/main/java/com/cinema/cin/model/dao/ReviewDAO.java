package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Film;
import com.cinema.cin.model.mo.Review;
import com.cinema.cin.model.mo.User;

import java.util.List;

public interface ReviewDAO {
    public Review create(
            User user,
            Film film,
            String data,
            String voto,
            String titolo,
            String testo
    );

    public void update(Review rew) throws DuplicatedObjectException;

    public void delete(Review rew) throws DuplicatedObjectException;

    public List<Review> findByFilmID(Film film);

    public List<Review> findByUserEmail(String string);

    public Review findByID(String rewID);

    public Float findMediaRecesioniByFilmID(String filmID);
}

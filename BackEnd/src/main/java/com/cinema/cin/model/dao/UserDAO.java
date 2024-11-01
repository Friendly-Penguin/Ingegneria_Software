package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.User;
public interface UserDAO {

    public User create(
            String email,
            String nome,
            String cognome,
            String password,
            String matricola,
            String token_ID,
            String confermaEmail
    ) throws DuplicatedObjectException;

    public void update(User usr) throws DuplicatedObjectException;

    public void delete(User usr);

    public User findLoggedUser();

    public User findByUserEmail(String str);

    public boolean confermaAccount(User usr, String str);

}

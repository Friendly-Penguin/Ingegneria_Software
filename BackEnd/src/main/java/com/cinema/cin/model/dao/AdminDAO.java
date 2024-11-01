package com.cinema.cin.model.dao;

import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Admin;

public interface AdminDAO {


    public Admin create(
        String nome,
        String email,
        String password,
        Boolean root
        )throws DuplicatedObjectException;

    public void update(Admin admin) throws DuplicatedObjectException;

    public void delete(Admin admin);

    public Admin findLoggedAdmin();

    public Admin findByAdminEmail(String email);

}

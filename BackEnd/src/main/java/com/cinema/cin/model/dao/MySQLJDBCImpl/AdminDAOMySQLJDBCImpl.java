package com.cinema.cin.model.dao.MySQLJDBCImpl;

import com.cinema.cin.model.dao.AdminDAO;
import com.cinema.cin.model.dao.exception.DuplicatedObjectException;
import com.cinema.cin.model.mo.Abbonamento;
import com.cinema.cin.model.mo.Admin;
import com.cinema.cin.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOMySQLJDBCImpl implements AdminDAO {

    Connection conn;

    public AdminDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Admin create(String nome, String email, String password, Boolean root) throws DuplicatedObjectException {

        PreparedStatement ps;
        Admin admin = new Admin();
        admin.setAdmin_name(nome);
        admin.setAdmin_email(email);
        admin.setAdmin_password(password);
        admin.setAdmin_root(root);

        try{

            String sql = "SELECT * FROM admin WHERE deleted = 'N' AND email = ? and root = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, admin.getAdmin_email());
            ps.setBoolean(i++, admin.getAdmin_root());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if(exist){
                throw new DuplicatedObjectException("UserDAOImpl.create: Tentativo di inserimento di un admin già esistente.");

            }else {


                sql = "INSERT INTO admin (nome,email,password,root,deleted) VALUES (?,?,?,?,'N')";

                ps = conn.prepareStatement(sql);
                i = 1;

                ps.setString(i++, admin.getAdmin_name());
                ps.setString(i++, admin.getAdmin_email());
                ps.setString(i++, admin.getAdmin_password());
                ps.setBoolean(i++, admin.getAdmin_root());
                ps.executeUpdate();
            }
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
        return admin;
    }


    @Override
    public void update(Admin admin) throws DuplicatedObjectException {

    }

    @Override
    public void delete(Admin admin) {

    }

    @Override
    public Admin findLoggedAdmin() {
        return null;
    }

    @Override
    public Admin findByAdminEmail(String email) {

        PreparedStatement ps;
        Admin admin = null;

        try {

            String sql = "SELECT *  FROM admin WHERE email = ? and deleted = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                admin = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return admin;

    }

    Admin read(ResultSet rs) {

        Admin admin = new Admin();

        try {
            admin.setAdmin_email(rs.getString("email"));
        } catch (SQLException sqle) {
        }
        try {
            admin.setAdmin_name(rs.getString("nome"));
        } catch (SQLException sqle) {
        }
        try {
            admin.setAdmin_id(rs.getLong("admin_id"));
        } catch (SQLException sqle) {
        }
        try {
            admin.setAdmin_password(rs.getString("password"));
        } catch (SQLException sqle) {
        }
        try {
            admin.setDeleted(rs.getString("deleted").equals("N"));
        } catch (SQLException sqle) {
        }
        try {
            admin.setAdmin_root(rs.getBoolean("root"));
        } catch (SQLException sqle) {
        }

        return admin;
    }

}


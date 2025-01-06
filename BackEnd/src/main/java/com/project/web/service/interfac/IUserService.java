package com.project.web.service.interfac;

import com.project.web.dto.LoginRequest;
import com.project.web.dto.Response;
import com.project.web.model.User;

import java.time.LocalDateTime;


public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUser();

    Response deleteUser(String userID);

    Response getUserByID(String userID);

    Response getTicketCount(Long userID);

    void updateUserLastLogin(Long userID, LocalDateTime lastLogin);

}

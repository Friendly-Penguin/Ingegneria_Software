package com.project.web.service.impl;

import com.project.web.dto.LoginRequest;
import com.project.web.dto.Response;
import com.project.web.dto.UserDTO;
import com.project.web.exception.CustomExcept;
import com.project.web.model.User;
import com.project.web.repo.UserRep;
import com.project.web.service.interfac.IUserService;
import com.project.web.utils.JWTUtils;
import com.project.web.utils.Utils;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRep userRep;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {

        Response response = new Response();

        try{

            if(user.getRole() == null || user.getRole().isBlank()){
                user.setRole("USER");
            }

            if(userRep.existsByEmail(user.getEmail())){
                throw new CustomExcept(user.getEmail() + "Already Exist");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRep.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);


        }catch (CustomExcept ex){
            response.setStatusCode(400);
            response.setMessage(ex.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error in User registration: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginUser) {

        Response response = new Response();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
            var user = userRep.findByEmail(loginUser.getEmail()).orElseThrow(() -> new CustomExcept("User not Found"));
            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setUserID(user.getId());
            response.setExpirationTime("1 Hour");
            response.setMessage("Success");

        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in User login: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUser() {

        Response response = new Response();

        try{
            List<User> users = userRep.findAll();
            List<UserDTO> userDTOList = Utils.mapUserEntityToUserDTOList(users);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUserDTOList(userDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userID) {

        Response response = new Response();

        try{

            userRep.findById(Long.valueOf(userID)).orElseThrow(() -> new CustomExcept("User not Found"));
            userRep.deleteById(Long.valueOf(userID));
            response.setStatusCode(200);
            response.setMessage("Success");



        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in User deleting: " + e.getMessage());
        }


        return response;
    }

    @Override
    public Response getUserByID(String userID) {

        Response response = new Response();
        try{

            User user = userRep.findById(Long.valueOf(userID)).orElseThrow(() -> new CustomExcept("User not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setUser(userDTO);

        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());

        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in User retriving: " + e.getMessage());
        }


        return response;
    }

    @Override
    public Response getTicketCount(Long userID){

        Response response = new Response();

        try{

            Long count = userRep.countUnansweredTickets(userID);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicketCount(count);

        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in User ticketCount retriving: " + e.getMessage());
        }


        return response;
    }

    @Override
    @Transactional
    public void updateUserLastLogin(Long userID, LocalDateTime lastLogin) {
        User user = userRep.findById(Long.valueOf(userID))
                .orElseThrow(() -> new CustomExcept("User not Found"));
        user.setLastLogin(lastLogin);
        userRep.save(user); // Salva il cambiamento, gestito automaticamente
        System.out.println("User last login updated.");
    }
}

package com.project.web.controller;

import com.project.web.dto.Response;
import com.project.web.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /* PER VEDERE TUTTI GLI UTENTI  */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        Response response = userService.getAllUser();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per recuperare le informazioni di un UTENTE tramite il suo ID  */
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
        Response response = userService.getUserByID(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per cancellare un determinato UTENTE dato il suo ID  */
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per recuperare il numero di ticket APERTI di un determinato UTENTE */
    @GetMapping("/ticketCount-{userID}")
    public ResponseEntity<Response> getTicketCount(@PathVariable("userID")Long userID) {
        Response response = userService.getTicketCount(userID);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }



}


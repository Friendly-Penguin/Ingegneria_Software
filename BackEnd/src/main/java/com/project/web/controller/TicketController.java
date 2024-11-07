package com.project.web.controller;

import com.project.web.dto.Response;
import com.project.web.exception.CustomExcept;
import com.project.web.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private ITicketService ticketService;


    /* Usato per creare un nuovo TICKET*/
    @PostMapping("/open")
    public ResponseEntity<Response> openTicket(@RequestParam(value = "title", required = false)String title,
                                               @RequestParam(value = "userID", required = false) Long userID,
                                               @RequestParam(value = "category", required = false)Long category) {

        if (title == null || title.isEmpty()) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a title for the ticket!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } else if (category < 0 || category == null) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a category for the ticket!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } else if (userID < 0 || userID == null) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a userID for the ticket!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        }
        Response response = new Response();
        try {
             response = ticketService.openTicket(title, category, userID);

        }catch(CustomExcept ex){

            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }


        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /* Usato per aggiornare un TICKET*/
    @PutMapping("/update/{ticketID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateTicket(@PathVariable Long ticketID,
                                                 @RequestParam(value = "category", required = false)Long category,
                                                 @RequestParam(value = "answer", required = false)String answer) {

        if (answer == null || answer.isEmpty()) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide an answer for the ticket!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } else if (category < 0 || category == null) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a category for the ticket!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        }
        Response response = new Response();

        try {
             response = ticketService.updateTicket(ticketID, answer, category);



        }catch(CustomExcept ex){

            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per recuperare tutti i TICKET con Risposta */
    @GetMapping("/getAllAnswered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllAnsweredTicket(){
        Response response = ticketService.getAllAnsweredTicket();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per recuperare tutti i TICKET senza Risposta */
    @PostMapping("/getAllNotAnswered")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllNotAnsweredTicket(){
        Response response = ticketService.getAllNotAnsweredTicket();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per recuperare tutti i TICKET di uno specifico utente */
    @GetMapping("/getUser/{userID}")
    public ResponseEntity<Response> getUserTicket(@PathVariable Long userID){
        Response response = ticketService.getUserTicket(userID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /* Usato per recuperare un TICKET specifico tramite il suo ID*/
    @GetMapping("/get/{ticketID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getTicketByID(@PathVariable Long ticketID){
        Response response =  ticketService.getTicketByID(ticketID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* Usato per cancellare un TICKET tramite il suo ID */
    @DeleteMapping("/delete-{ticketID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteTicket(@PathVariable Long ticketID){
        Response response = ticketService.deleteTicket(ticketID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}

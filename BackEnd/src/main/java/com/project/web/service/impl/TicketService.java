package com.project.web.service.impl;


import com.project.web.dto.Response;
import com.project.web.dto.TicketDTO;
import com.project.web.exception.CustomExcept;
import com.project.web.model.Categoria;
import com.project.web.model.Ticket;
import com.project.web.model.User;
import com.project.web.repo.TicketRepo;
import com.project.web.service.interfac.ITicketService;
import com.project.web.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepo ticketRepo;


    @Override
    public Response openTicket(String titolo, Long categoria, Long userID){

        Response response = new Response();

        try{
            Ticket ticket = new Ticket();
            User user = new User();
            Categoria category = new Categoria();

            user.setId(userID);
            category.setId(categoria);

            ticket.setUser(user);
            ticket.setCategory(category);
            ticket.setTitle(titolo);

            Ticket savedTicket = ticketRepo.save(ticket);
            TicketDTO ticketDTO = Utils.mapTicketEntityToTicketDTO(savedTicket);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicket(ticketDTO);

        }catch (Exception ex){
            System.out.println(ex);
            throw new CustomExcept("Error adding new Ticket: " + ex.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllAnsweredTicket() {

        Response response = new Response();

        try{
            List<Ticket> tickets = ticketRepo.findByAnswered();
            List<TicketDTO> ticketDTOList = Utils.mapTicketEntityToTicketDTOList(tickets);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicketDTOList(ticketDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllNotAnsweredTicket() {

        Response response = new Response();

        try{
            List<Ticket> tickets = ticketRepo.findByNotAnswered();
            List<TicketDTO> ticketDTOList = Utils.mapTicketEntityToTicketDTOList(tickets);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicketDTOList(ticketDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserTicket(Long userID) {
        Response response = new Response();

        try{
            List<Ticket> tickets = ticketRepo.findUserTicket(userID);
            List<TicketDTO> ticketDTOList = Utils.mapTicketEntityToTicketDTOList(tickets);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicketDTOList(ticketDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateTicket(Long ticketID, String answer, Long category) {

        Response response = new Response();

        try {

            Ticket ticket = ticketRepo.findById(ticketID).orElseThrow(() -> new CustomExcept("Ticket not found"));


            Categoria categoria = new Categoria();
            categoria.setId(category);
            ticket.setCategory(categoria);
            ticket.setAnswer(answer);
            ticket.setAnswered(true);

            Ticket updatedTicket = ticketRepo.save(ticket);
            TicketDTO ticketDTO = Utils.mapTicketEntityToTicketDTO(updatedTicket);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicket(ticketDTO);

        }catch (CustomExcept ex){
            response.setStatusCode(500);
            response.setMessage("Error deleting Question " + ex.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error adding new Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getTicketByID(Long ticketID){
        Response response = new Response();

        try{
            Ticket ticket = ticketRepo.findTicketByID(ticketID);
            TicketDTO ticketDTO = Utils.mapTicketEntityToTicketDTO(ticket);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setTicket(ticketDTO);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteTicket(Long ticketID){

        Response response = new Response();

        try{

            ticketRepo.findById(Long.valueOf(ticketID)).orElseThrow(() -> new CustomExcept("Ticket not Found"));
            ticketRepo.deleteById(Long.valueOf(ticketID));
            response.setStatusCode(200);
            response.setMessage("Success");

        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());

        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in Ticket deleting: " + e.getMessage());
        }


        return response;
    }

}

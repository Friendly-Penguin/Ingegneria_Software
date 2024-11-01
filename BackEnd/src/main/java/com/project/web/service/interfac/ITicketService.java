package com.project.web.service.interfac;

import com.project.web.dto.Response;

public interface ITicketService {

    Response openTicket(String titolo, Long categoria, Long userID);

    Response getAllNotAnsweredTicket();

    Response getAllAnsweredTicket();

    Response getUserTicket(Long userID);

    Response updateTicket(Long ticketID, String answer, Long Category);

    Response getTicketByID(Long ticketID);
}

package com.project.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private Long userID;
    private String expirationTime;

    private UserDTO user;
    private List<UserDTO> userDTOList;

    private QuestionDTO question;
    private List<QuestionDTO> questionDTOList;

    private CategoryDTO category;
    private List<CategoryDTO> categoryDTOList;

    private TicketDTO ticket;
    private List<TicketDTO> ticketDTOList;


}

package com.project.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {

    private Long id;
    private String title;
    private String answer;
    private Boolean answered;
    private Long category;
    private Long userID;
}
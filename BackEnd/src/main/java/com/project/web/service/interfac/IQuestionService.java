package com.project.web.service.interfac;

import com.project.web.dto.Response;


public interface IQuestionService {

    Response addQuestion(String titolo, String content, Long categoria, Long userID);

    Response getAllQuestions();

    Response deleteQuestion(Long id);

    Response updateQuestion(Long id, String title, String content, Long Category);

    Response getQuestion(Long questionID);
}

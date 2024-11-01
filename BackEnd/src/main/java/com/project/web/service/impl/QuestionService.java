package com.project.web.service.impl;

import com.project.web.dto.QuestionDTO;
import com.project.web.dto.Response;
import com.project.web.exception.CustomExcept;
import com.project.web.model.Categoria;
import com.project.web.model.Question;
import com.project.web.model.User;
import com.project.web.repo.QuestionRepo;
import com.project.web.service.interfac.IQuestionService;
import com.project.web.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionRepo questionRepo;


    @Override
    public Response addQuestion(String titolo, String content, Long categoria, Long userID) {

        Response response = new Response();

        try{
            Question question = new Question();
            User user = new User();
            Categoria category = new Categoria();
            user.setId(userID);
            category.setId(categoria);

            question.setUser(user);
            question.setCategory(category);
            question.setTitle(titolo);
            question.setContent(content);
            Question savedQuestion = questionRepo.save(question);
            QuestionDTO questionDTO = Utils.mapQuestionEntityToQuestionDTO(savedQuestion);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setQuestion(questionDTO);


        }catch (Exception e){

            System.out.println(e);
            throw new CustomExcept("Error adding new Question: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllQuestions() {

        Response response = new Response();

        try{
            List<Question> questions = questionRepo.findAll();
            List<QuestionDTO> questionDTOList = Utils.mapQuestionEntityToQuestionDTOList(questions);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setQuestionDTOList(questionDTOList);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error retriving answered Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteQuestion(Long questionId) {

        Response response = new Response();

        try {
            questionRepo.findById(questionId).orElseThrow(() -> new CustomExcept("Question not found"));
            questionRepo.deleteById(questionId);

            response.setStatusCode(200);
            response.setMessage("Success");

        }catch (CustomExcept ex){
                response.setStatusCode(400);
                response.setMessage("Error finding Question " + ex.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting Question " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateQuestion(Long questionId, String title, String content, Long category) {

            Response response = new Response();

            try {

                Question question = questionRepo.findById(questionId).orElseThrow(() -> new CustomExcept("Question not found"));


                Categoria categoria = new Categoria();
                categoria.setId(category);
                question.setCategory(categoria);
                question.setContent(content);
                question.setTitle(title);
                Question updatedQuestion = questionRepo.save(question);
                QuestionDTO questionDTO = Utils.mapQuestionEntityToQuestionDTO(updatedQuestion);

                response.setStatusCode(200);
                response.setMessage("Success");
                response.setQuestion(questionDTO);

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
    public Response getQuestion(Long questionID){

        Response response = new Response();

        try{
            Question question = questionRepo.findQuestionByID(questionID);
            QuestionDTO questionDTO = Utils.mapQuestionEntityToQuestionDTO(question);

            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setQuestion(questionDTO);

        }catch (CustomExcept ex){

            response.setStatusCode(404);
            response.setMessage(ex.getMessage());
        }catch (Exception e){

            response.setStatusCode(500);
            response.setMessage("Error in User question retriving: " + e.getMessage());
        }


        return response;
    }

}

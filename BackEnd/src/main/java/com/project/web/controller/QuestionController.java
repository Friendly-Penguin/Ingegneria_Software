package com.project.web.controller;


import com.project.web.dto.Response;
import com.project.web.exception.CustomExcept;
import com.project.web.service.interfac.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;


    // Aggiorna una domanda con una risposta
    @PutMapping("/update/{questionID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateQuestion(@PathVariable Long questionID,
                                                   @RequestParam(value = "title", required = false)String title,
                                                   @RequestParam(value = "answer", required = false) String answer,
                                                   @RequestParam(value = "category", required = false)Long category) {
        if (answer == null || answer.isEmpty()) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide an answer for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } else if (category < 0 || category == null) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a category for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        } else if (title == null || title.isEmpty()) {

            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a title for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        }

        Response response = questionService.updateQuestion(questionID, title, answer, category);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* PER AGGIUNGERE UNA NUOVA DOMANDA */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addQuestion(@RequestParam(value = "title", required = false)String title,
                                                @RequestParam(value = "category", required = false) Long category,
                                                @RequestParam(value = "userID", required = false)Long userID,
                                                @RequestParam(value = "content", required = false)String content) {

        if(title == null || title.isEmpty()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a title for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }else if(category == null || category < 0){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a category for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } else if (userID == null || userID < 0) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide an userID for the question!");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = new Response();
        try {
                response = questionService.addQuestion(title, content, category, userID);



        }catch(CustomExcept ex){

            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // Recupera tutte le domande
    @GetMapping("/all")
    public ResponseEntity<Response> getAllAnsweredQuestions(){
        Response response = questionService.getAllQuestions();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Cancella una domanda dato il suo ID
    @DeleteMapping("/delete/{questionId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteQuestion(@PathVariable Long questionId){
        Response response = questionService.deleteQuestion(questionId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //Recupera una domanda in particolare tramite il suo ID
    @GetMapping("/retrive-{questionID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getQuestion(@PathVariable Long questionID) {
        Response response = questionService.getQuestion(questionID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

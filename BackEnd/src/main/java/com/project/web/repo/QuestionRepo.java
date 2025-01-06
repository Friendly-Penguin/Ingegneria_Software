package com.project.web.repo;

import com.project.web.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/*
 * A repository is a mechanism for encapsulating the interaction with the database.
 * It allows you to perform CRUD (Create, Read, Update, Delete) operations and custom queries in a clean and easy way.
 * Spring Data JPA provides the JpaRepository interface, which simplifies database operations.
 */


@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {


    //Retrives all the question sorted by id from the DB
    @Query("select q from Question as q order by q.id ASC")
    List<Question> findAll();

    //Retrives a specific question by its ID
    @Query("select q from Question as q where q.id = :questionID")
    Question findQuestionByID(long questionID);

}

package com.project.web.repo;

import com.project.web.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {



    @Query("select q from Question as q order by q.id ASC")
    List<Question> findAll();

    @Query("select q from Question as q where q.id = :questionID")
    Question findQuestionByID(long questionID);

}

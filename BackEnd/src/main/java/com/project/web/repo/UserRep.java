package com.project.web.repo;
import com.project.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
* A repository is a mechanism for encapsulating the interaction with the database.
* It allows you to perform CRUD (Create, Read, Update, Delete) operations and custom queries in a clean and easy way.
* Spring Data JPA provides the JpaRepository interface, which simplifies database operations.
* */

import java.util.Optional;
@Repository
public interface UserRep extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // Retrive the number of not answered tickets written by a specific user
    @Query("select count(t) from User u join u.tickets t where u.id = :userID and t.answered = false")
    Long countUnansweredTickets(@Param("userID") Long userID);
}

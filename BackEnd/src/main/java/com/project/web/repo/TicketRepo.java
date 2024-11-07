package com.project.web.repo;

import com.project.web.model.Ticket;
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
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    // Retrives a list of Ticket not answered from the DB
    @Query("select t from Ticket as t where t.answered = false order by t.id ASC ")
    List<Ticket> findByNotAnswered();

    // Retrives a list of Ticket answered from the DB
    @Query("select t from Ticket as t where t.answered = true order by t.id ASC ")
    List<Ticket> findByAnswered();

    // Retrieves a list of tickets for a specific user identified by its ID
    @Query("Select t from Ticket as t where t.user.id = :id_user")
    List<Ticket> findUserTicket(Long id_user);

    // Retrives a specific Ticket identified by its id
    @Query("Select t from Ticket as t where t.id = :id_ticket")
    Ticket findTicketByID(Long id_ticket);


}

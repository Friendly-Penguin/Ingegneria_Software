package com.project.web.repo;

import com.project.web.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    @Query("select t from Ticket as t where t.answered = false order by t.id ASC ")
    List<Ticket> findByNotAnswered();

    @Query("select t from Ticket as t where t.answered = true order by t.id ASC ")
    List<Ticket> findByAnswered();

    @Query("Select t from Ticket as t where t.user.id = :id_user")
    List<Ticket> findUserTicket(Long id_user);

    @Query("Select t from Ticket as t where t.id = :id_ticket")
    Ticket findTicketByID(Long id_ticket);


}

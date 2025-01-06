package com.project.web.repo;

import com.project.web.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/*
 * A repository is a mechanism for encapsulating the interaction with the database.
 * It allows you to perform CRUD (Create, Read, Update, Delete) operations and custom queries in a clean and easy way.
 * Spring Data JPA provides the JpaRepository interface, which simplifies database operations.
 */

import java.util.List;
@Repository
public interface CategoryRepo extends JpaRepository<Categoria, Long> {

    //Retrives all the category from the DB
    @Query("select c from Categoria as c")
    List<Categoria> getAll();

}

package com.project.web.repo;

import com.project.web.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepo extends JpaRepository<Categoria, Long> {

    @Query("select c from Categoria as c")
    List<Categoria> getAll();

}

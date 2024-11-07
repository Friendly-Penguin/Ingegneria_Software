package com.project.web.controller;

import com.project.web.dto.Response;
import com.project.web.exception.CustomExcept;
import com.project.web.repo.CategoryRepo;
import com.project.web.service.interfac.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    /* PER AGGIUNGERE UNA NUOVA CATEGORIA */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addCategory(@RequestBody Map<String, String> request)
    {
        String type = request.get("categoryType");
        if(type == null || type.isEmpty()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide a type for the category");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = new Response();
        try{

            response = categoryService.addCategory(type);

        }catch (CustomExcept ex){
            response.setStatusCode(500);
            response.setMessage(ex.getMessage());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /* PER CANCELLARE UNA CATEGORIA */
    @DeleteMapping("/delete/{categoryID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long categoryID){
        Response response =  categoryService.deleteCategory(categoryID);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    /* PER RECUPERARE TUTTE LE CATEGORIE */
    @GetMapping("/all")
    public ResponseEntity<Response> getAll(){

        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

}

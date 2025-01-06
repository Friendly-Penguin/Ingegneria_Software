package com.project.web.service.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.project.web.dto.Response;
import com.project.web.dto.CategoryDTO;
import com.project.web.exception.CustomExcept;
import com.project.web.model.Categoria;
import com.project.web.repo.CategoryRepo;
import com.project.web.service.interfac.ICategoryService;
import com.project.web.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addCategory(String type) {

        Response response = new Response();

        try{
            Categoria category = new Categoria();

            category.setType(type);
            Categoria savedCategory = categoryRepo.save(category);
            CategoryDTO categoryDTO = Utils.mapCategoryEntityToCategoryDTO(savedCategory);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setCategory(categoryDTO);

        }catch (Exception e){
            System.out.println(e);
            throw  new CustomExcept("Error adding new Category: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteCategory(Long categoryID) {

        Response response = new Response();

        try{
            categoryRepo.findById(categoryID).orElseThrow(()->new CustomExcept("Category not found"));
            categoryRepo.deleteById(categoryID);

            response.setStatusCode(200);
            response.setMessage("Success");


        }catch (CustomExcept ex){
            response.setStatusCode(400);
            response.setMessage("Error finding category: " + ex.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting category: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllCategories() {

        Response response = new Response();

        try {

            List<Categoria> categories = categoryRepo.getAll();
            List<CategoryDTO> categoriesDTO = Utils.mapCategoryEntityToCategoryDTOList(categories);


            response.setStatusCode(200);
            response.setMessage("Success");
            response.setCategoryDTOList(categoriesDTO);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all categories: " + e.getMessage());
        }

        return response;
    }
}

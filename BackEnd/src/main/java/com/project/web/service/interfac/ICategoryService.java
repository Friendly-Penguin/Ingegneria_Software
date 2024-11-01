package com.project.web.service.interfac;

import com.project.web.dto.Response;


public interface ICategoryService {

    Response addCategory(String type);

    Response deleteCategory(Long id);

    Response getAllCategories();
}

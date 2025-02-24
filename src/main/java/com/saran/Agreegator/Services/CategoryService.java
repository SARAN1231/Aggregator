package com.saran.Agreegator.Services;

import com.saran.Agreegator.Dtos.CategoryDto;
import com.saran.Agreegator.Dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDTO);
    Response getAllCategories();
    Response getCategoryById(Long id);
    Response updateCategory(Long id, CategoryDto categoryDTO);
    Response deleteCategory(Long id);
}

package com.saran.Agreegator.Services;

import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.SubCategoryDto;

public interface SubCategoryService {
    Response createSubCategory(SubCategoryDto subCategoryDto);
    Response getAllSubCategories();
    Response getSubCategoryById(Long id);
    Response updateSubCategory(Long id, SubCategoryDto subCategoryDto);
    Response deleteSubCategory(Long id);
}


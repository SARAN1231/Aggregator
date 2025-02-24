package com.saran.Agreegator.Services.Impl;

import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.SubCategoryDto;
import com.saran.Agreegator.Models.Category;
import com.saran.Agreegator.Models.SubCategory;
import com.saran.Agreegator.Repository.CategoryRepository;
import com.saran.Agreegator.Repository.SubCategoryRepository;
import com.saran.Agreegator.Services.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createSubCategory(SubCategoryDto subCategoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findByName(subCategoryDto.getCategoryName());
        if (categoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found with name: " + subCategoryDto.getCategoryName())
                    .build();
        }

        SubCategory subCategory = SubCategory.builder()
                .name(subCategoryDto.getName())
                .category(categoryOptional.get()) // Set category by name
                .build();

        subCategoryRepository.save(subCategory);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("SubCategory created successfully")
                .subCategory(modelMapper.map(subCategory, SubCategoryDto.class))
                .build();
    }

    @Override
    public Response getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("SubCategories retrieved successfully")
                .subCategories(subCategories.stream().map(sub -> modelMapper.map(sub, SubCategoryDto.class)).toList())
                .build();
    }

    @Override
    public Response getSubCategoryById(Long id) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(id);
        if (subCategoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("SubCategory not found")
                    .build();
        }

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("SubCategory retrieved successfully")
                .subCategory(modelMapper.map(subCategoryOptional.get(), SubCategoryDto.class))
                .build();
    }

    @Override
    public Response updateSubCategory(Long id, SubCategoryDto subCategoryDto) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(id);
        if (subCategoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("SubCategory not found")
                    .build();
        }

        Optional<Category> categoryOptional = categoryRepository.findByName(subCategoryDto.getCategoryName());
        if (categoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found with name: " + subCategoryDto.getCategoryName())
                    .build();
        }

        SubCategory subCategory = subCategoryOptional.get();
        subCategory.setName(subCategoryDto.getName());
        subCategory.setCategory(categoryOptional.get()); // Set category by name

        subCategoryRepository.save(subCategory);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("SubCategory updated successfully")
                .subCategory(modelMapper.map(subCategory, SubCategoryDto.class))
                .build();
    }

    @Override
    public Response deleteSubCategory(Long id) {
        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(id);
        if (subCategoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("SubCategory not found")
                    .build();
        }

        subCategoryRepository.deleteById(id);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("SubCategory deleted successfully")
                .build();
    }
}



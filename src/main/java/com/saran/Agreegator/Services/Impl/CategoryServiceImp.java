package com.saran.Agreegator.Services.Impl;

import com.saran.Agreegator.Dtos.CategoryDto;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.SubCategoryDto;
import com.saran.Agreegator.Models.Category;
import com.saran.Agreegator.Repository.CategoryRepository;
import com.saran.Agreegator.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public Response createCategory(CategoryDto categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Category already exists: " + categoryDTO.getName())
                    .build();
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(category);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Category created successfully")
                .category(modelMapper.map(category, CategoryDto.class))
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Categories fetched successfully")
                .categories(categories)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found")
                    .build();
        }

        Category category = categoryOptional.get();

        // Convert to DTO
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        // Manually map subcategories
        List<SubCategoryDto> subCategoryDtos = category.getSubCategories().stream()
                .map(subCategory -> modelMapper.map(subCategory, SubCategoryDto.class))
                .toList();

        categoryDto.setSubCategories(subCategoryDtos);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category retrieved successfully")
                .category(categoryDto)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDto categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found")
                    .build();
        }

        Category category = categoryOptional.get();
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category updated successfully")
                .category(modelMapper.map(category, CategoryDto.class))
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found")
                    .build();
        }

        categoryRepository.deleteById(id);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category deleted successfully")
                .build();
    }
}

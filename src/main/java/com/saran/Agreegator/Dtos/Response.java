package com.saran.Agreegator.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.saran.Agreegator.Enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Response {
    // generic
    private int status;
    private String message;

    // for login
    private String token;
    private UserRole role;
    private String expiryDate;

    // for outputs
    private UserDto user;
    private List<UserDto> users;

    private CategoryDto category;
    private List<CategoryDto> categories;

    private SubCategoryDto subCategory;
    private List<SubCategoryDto> subCategories;

    private ProductDto product;
    private List<ProductDto> products;

    private final LocalDateTime timestamp = LocalDateTime.now();
}

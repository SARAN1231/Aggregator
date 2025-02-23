package com.saran.Agreegator.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.saran.Agreegator.Enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status;
    private String message;
    private String token;
    private UserRole role;
    private String expiryDate;

    private UserDto user;
    private List<UserDto> users;
    private CategoryDto category;
    private List<CategoryDto> categories;
    private SubCategoryDto subCategory;
    private List<SubCategoryDto> subCategories;
    private ProductDto product;
    private List<ProductDto> products;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
}

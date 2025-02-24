package com.saran.Agreegator.Services.Impl;

import com.saran.Agreegator.Dtos.ProductDto;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Exceptions.ResourceNotFoundException;
import com.saran.Agreegator.Models.Product;
import com.saran.Agreegator.Models.SubCategory;
import com.saran.Agreegator.Repository.ProductRepository;
import com.saran.Agreegator.Repository.SubCategoryRepository;
import com.saran.Agreegator.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createProduct(ProductDto productDto) {
        // Fetch SubCategory by name instead of ID
        SubCategory subCategory =  subCategoryRepository.findByName(productDto.getSubCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found: " + productDto.getSubCategoryName()));

        Product product = Product.builder()
                .name(productDto.getName())
                .barcode(productDto.getBarcode())
                .description(productDto.getDescription())
                .mrpPrice(productDto.getMrpPrice())
                .sellingPrice(productDto.getSellingPrice())
                .quantity(productDto.getQuantity())
                .expiryDate(productDto.getExpiryDate())
                .imageUrl(productDto.getImageUrl())
                .inventoryCount(productDto.getInventoryCount())
                .subCategory(subCategory)  // Set the fetched SubCategory
                .build();

        productRepository.save(product);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Product added successfully")
                .product(modelMapper.map(product, ProductDto.class))
                .build();
    }


    @Override
    public Response getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT.value())
                    .message("No products found")
                    .build();
        }

        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Products fetched successfully")
                .products(productDtos)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Product not found")
                    .build();
        }

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product fetched successfully")
                .product(modelMapper.map(productOptional.get(), ProductDto.class))
                .build();
    }

    @Override
    public Response updateProduct(Long id, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Product not found")
                    .build();
        }

        Product product = productOptional.get();
        product.setName(productDto.getName());
        product.setBarcode(productDto.getBarcode());
        product.setDescription(productDto.getDescription());
        product.setMrpPrice(productDto.getMrpPrice());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setQuantity(productDto.getQuantity());
        product.setExpiryDate(productDto.getExpiryDate());
        product.setImageUrl(productDto.getImageUrl());
        product.setInventoryCount(productDto.getInventoryCount());

        Optional<SubCategory> subCategoryOptional = subCategoryRepository.findByName(productDto.getSubCategoryName());
        if (subCategoryOptional.isPresent()) {
            product.setSubCategory(subCategoryOptional.get());
        } else {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("SubCategory not found")
                    .build();
        }

        productRepository.save(product);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product updated successfully")
                .product(modelMapper.map(product, ProductDto.class))
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Product not found")
                    .build();
        }

        productRepository.deleteById(id);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product deleted successfully")
                .build();
    }
}


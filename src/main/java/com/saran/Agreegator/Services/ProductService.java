package com.saran.Agreegator.Services;

import com.saran.Agreegator.Dtos.ProductDto;
import com.saran.Agreegator.Dtos.Response;

public interface ProductService {
    Response createProduct(ProductDto productDto);
    Response getAllProducts();
    Response getProductById(Long id);
    Response updateProduct(Long id, ProductDto productDto);
    Response deleteProduct(Long id);
}

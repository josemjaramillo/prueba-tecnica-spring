package com.example.inventory.service;

import com.example.inventory.dto.ProductRequestDTO;
import com.example.inventory.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO product);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);
    void deleteProduct(Long id);
}

package com.example.inventory.service.impl;

import com.example.inventory.entity.Product;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.repository.ProductRepository;

import com.example.inventory.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository repo){
        this.productRepository = repo;
    }

    @Override
    @Transactional(readOnly=true)
    public List<Product> getAllProducts() { return productRepository.findAll(); }

    @Override
    @Transactional(readOnly=true)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    }

    @Override
    @Transactional
    public Product createProduct(Product p) { return productRepository.save(p); }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product changes) {
        Product p = getProductById(id);
        p.setName(changes.getName());
        p.setDescription(changes.getDescription());
        p.setPrice(changes.getPrice());
        return productRepository.save(p);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) { productRepository.delete(getProductById(id)); }
}

package com.example.inventory.service.impl;

import com.example.inventory.dto.ProductRequestDTO;
import com.example.inventory.dto.ProductResponseDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.ProductRepository;

import com.example.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository repo, ProductMapper mapper){
        this.productRepository = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly=true)
    public List<ProductResponseDTO> getAllProducts() { return mapper.toDto(productRepository.findAll()) ; }

    @Override
    @Transactional(readOnly=true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Get: producto con ID {} no encontrado", id);
                    return new NotFoundException("Producto no encontrado");
                });
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        if (dto.price().signum() <= 0) {
            log.warn("Intento de crear producto con precio inválido: {}", dto.price());
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        Product entity = mapper.toEntity(dto);
        return mapper.toDto(productRepository.save(entity));
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update: producto con ID {} no encontrado", id);
                    return new NotFoundException("Producto no encontrado");
                });

        if (dto.price().signum() <= 0) {
            log.warn("Intento de actualizar producto con precio inválido: {}", dto.price());
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(productRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product entity = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Delete: producto con ID {} no encontrado", id);
            return new NotFoundException("Producto no encontrado");
        });

        productRepository.delete(entity);
    }
}

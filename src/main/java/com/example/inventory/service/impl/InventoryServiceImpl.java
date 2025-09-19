package com.example.inventory.service.impl;

import com.example.inventory.entity.InventoryMovement;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.repository.InventoryMovementRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(
            InventoryMovementRepository movementRepository,
            ProductRepository productRepository)
    {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public int getAvailableStock(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Producto no encontrado");
        }
        return movementRepository.computeStock(productId);
    }

    @Override
    @Transactional
    public InventoryMovement registerStockIn(Long productId, int quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        InventoryMovement movement = new InventoryMovement(product, InventoryMovement.MovementType.IN, quantity);
        return movementRepository.save(movement);
    }

    @Override
    @Transactional
    public InventoryMovement registerStockOut(Long productId, int quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        if (quantity > movementRepository.computeStock(productId)){
            throw new IllegalArgumentException("Stock insuficiente");
        }
        InventoryMovement movement = new InventoryMovement(product, InventoryMovement.MovementType.OUT, quantity);
        return movementRepository.save(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> getStockHistory(Long productId){

        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Producto no encontrado");
        }
        return movementRepository.findByProduct_IdOrderByMovementDateDesc(productId);
    }

}

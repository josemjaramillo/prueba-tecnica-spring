package com.example.inventory.service.impl;

import com.example.inventory.dto.MovementRequestDTO;
import com.example.inventory.dto.MovementResponseDTO;
import com.example.inventory.dto.StockResponseDTO;
import com.example.inventory.entity.InventoryMovement;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.mapper.MovementMapper;
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
    private final MovementMapper mapper;

    public InventoryServiceImpl(
            InventoryMovementRepository movementRepository,
            ProductRepository productRepository,
            MovementMapper mapper)
    {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponseDTO getProductStock(Long productId) {
        Product entity = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        int stock = movementRepository.computeStock(productId);
        return new StockResponseDTO(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(), stock);
    }

    @Override
    @Transactional
    public MovementResponseDTO registerStock(Long productId, MovementRequestDTO dto){
        if (dto.quantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        if (dto.movementType() == InventoryMovement.MovementType.OUT) {
            if (dto.quantity() > movementRepository.computeStock(productId)) {
                throw new IllegalArgumentException("Stock insuficiente");
            }
        }
        InventoryMovement entity = new InventoryMovement(product, dto.movementType(), dto.quantity());
        movementRepository.save(entity);
        return mapper.toDto(entity);
    }



    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDTO> getStockHistory(Long productId){

        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Producto no encontrado");
        }
        return mapper.toDto(movementRepository.findByProduct_IdOrderByMovementDateDesc(productId));
    }

}

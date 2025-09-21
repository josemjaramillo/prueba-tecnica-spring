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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock InventoryMovementRepository movementRepository;
    @Mock ProductRepository productRepository;
    @Mock MovementMapper mapper;

    @InjectMocks InventoryServiceImpl service;

    private Product product;
    

    @BeforeEach
    void setUp() {
        product = new Product("Teclado", "Mecánico", new BigDecimal("150.00"));
        product.setId(1L);
    }

    @Test
    void getProductStock_ok() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(movementRepository.computeStock(1L)).thenReturn(15);

        StockResponseDTO result = service.getProductStock(1L);

        assertEquals(1L, result.id());
        assertEquals("Teclado", result.name());
        assertEquals(15, result.stock());
        Mockito.verify(movementRepository).computeStock(1L);
    }

    @Test
    void getProductStock_notFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getProductStock(1L));
    }

    @Test
    void registerStock_in_ok() {
        MovementRequestDTO dto = new MovementRequestDTO(InventoryMovement.MovementType.IN, 10);
        InventoryMovement savedMovement = new InventoryMovement(product, InventoryMovement.MovementType.IN, 10);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(movementRepository.save(Mockito.any())).thenReturn(savedMovement);
        Mockito.when(mapper.toDto(Mockito.any(InventoryMovement.class))).thenReturn(new MovementResponseDTO(1L, 1L, InventoryMovement.MovementType.IN, 10, null));

        MovementResponseDTO result = service.registerStock(1L, dto);

        assertEquals(1L, result.productId());
        assertEquals(10, result.quantity());
        Mockito.verify(movementRepository).save(Mockito.any());
        Mockito.verify(movementRepository, Mockito.never()).computeStock(Mockito.anyLong());
    }

    @Test
    void registerStock_out_ok() {
        MovementRequestDTO dto = new MovementRequestDTO(InventoryMovement.MovementType.OUT, 5);
        InventoryMovement savedMovement = new InventoryMovement(product, InventoryMovement.MovementType.OUT, 5);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(movementRepository.computeStock(1L)).thenReturn(10);
        Mockito.when(movementRepository.save(Mockito.any())).thenReturn(savedMovement);
        Mockito.when(mapper.toDto(Mockito.any(InventoryMovement.class))).thenReturn(new MovementResponseDTO(1L, 1L, InventoryMovement.MovementType.OUT, 5, null));

        MovementResponseDTO result = service.registerStock(1L, dto);

        assertEquals(InventoryMovement.MovementType.OUT, result.movementType());
        assertEquals(5, result.quantity());
        Mockito.verify(movementRepository).save(Mockito.any());
    }

    @Test
    void registerStock_out_insufficientStock() {
        MovementRequestDTO dto = new MovementRequestDTO(InventoryMovement.MovementType.OUT, 10);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(movementRepository.computeStock(1L)).thenReturn(5);

        assertThrows(IllegalArgumentException.class, () -> service.registerStock(1L, dto));
    }

    @Test
    void registerStock_invalidQuantity() {
        MovementRequestDTO dto = new MovementRequestDTO(InventoryMovement.MovementType.IN, -1);

        assertThrows(IllegalArgumentException.class, () -> service.registerStock(1L, dto));
    }

    @Test
    void registerStock_productNotFound() {
        MovementRequestDTO dto = new MovementRequestDTO(InventoryMovement.MovementType.IN, 5);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.registerStock(1L, dto));
    }

    @Test
    void getStockHistory_ok() {
        InventoryMovement movement = new InventoryMovement(product, InventoryMovement.MovementType.IN, 10);
        List<InventoryMovement> movements = List.of(movement);
        MovementResponseDTO dto = new MovementResponseDTO(1L, 1L, InventoryMovement.MovementType.IN, 10, null);

        Mockito.when(productRepository.existsById(1L)).thenReturn(true);
        Mockito.when(movementRepository.findByProduct_IdOrderByMovementDateDesc(1L)).thenReturn(movements);
        Mockito.when(mapper.toDto(movements)).thenReturn(List.of(dto));

        List<MovementResponseDTO> result = service.getStockHistory(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().productId());
    }

    @Test
    void getStockHistory_notFound() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.getStockHistory(1L));
    }

}
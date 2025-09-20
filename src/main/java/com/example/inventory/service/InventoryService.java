package com.example.inventory.service;

import com.example.inventory.dto.MovementRequestDTO;
import com.example.inventory.dto.MovementResponseDTO;
import com.example.inventory.dto.StockResponseDTO;

import java.util.List;

public interface InventoryService {

    StockResponseDTO getProductStock(Long productId);
    MovementResponseDTO registerStock(Long productId, MovementRequestDTO dto);
    List<MovementResponseDTO> getStockHistory(Long productId);
}
package com.example.inventory.service;

import com.example.inventory.entity.InventoryMovement;
import java.util.List;

public interface InventoryService {

    int getAvailableStock(Long productId);
    InventoryMovement registerStockIn(Long productId, int quantity);
    InventoryMovement registerStockOut(Long productId, int quantity);
    List<InventoryMovement> getStockHistory(Long productId);
}
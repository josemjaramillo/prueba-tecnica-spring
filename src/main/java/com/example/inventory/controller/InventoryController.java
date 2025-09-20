package com.example.inventory.controller;


import com.example.inventory.dto.MovementRequestDTO;
import com.example.inventory.dto.MovementResponseDTO;
import com.example.inventory.dto.StockResponseDTO;
import com.example.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/stock")
    public ResponseEntity<StockResponseDTO> getStock (@PathVariable("productId") Long id){
        return ResponseEntity.ok(inventoryService.getProductStock(id));
    }

    @PostMapping("/movements")
    public ResponseEntity<MovementResponseDTO> registerStockIn (
            @PathVariable("productId") Long id,
            @Valid @RequestBody MovementRequestDTO body){
        return ResponseEntity.ok(inventoryService.registerStock(id,body));
    }

    @GetMapping("/movements")
    public List<MovementResponseDTO> getAllMovements(@PathVariable("productId") Long id){
        return inventoryService.getStockHistory(id);
    }
}

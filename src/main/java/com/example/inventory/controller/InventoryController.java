package com.example.inventory.controller;


import com.example.inventory.dto.MovementRequestDTO;
import com.example.inventory.dto.MovementResponseDTO;
import com.example.inventory.dto.StockResponseDTO;
import com.example.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name= "Inventario", description = "Operaciones de consulta inventario")
@RestController
@RequestMapping("/api/products/{productId}")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Obtener stock de un producto")
    @GetMapping("/stock")
    public ResponseEntity<StockResponseDTO> getStock (@PathVariable("productId") Long id){
        return ResponseEntity.ok(inventoryService.getProductStock(id));
    }

    @Operation(summary = "Registrar un stock de entrada/salida")
    @PostMapping("/movements")
    public ResponseEntity<MovementResponseDTO> registerStockIn (
            @PathVariable("productId") Long id,
            @Valid @RequestBody MovementRequestDTO body){
        return ResponseEntity.ok(inventoryService.registerStock(id,body));
    }

    @Operation(summary = "Obtener el historial de movimientos")
    @GetMapping("/movements")
    public List<MovementResponseDTO> getAllMovements(@PathVariable("productId") Long id){
        return inventoryService.getStockHistory(id);
    }
}

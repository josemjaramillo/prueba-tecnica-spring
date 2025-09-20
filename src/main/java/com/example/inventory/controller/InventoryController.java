package com.example.inventory.controller;


import com.example.inventory.entity.InventoryMovement;
import com.example.inventory.entity.Product;
import com.example.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

//- `GET /api/products/{id}/stock` → consultar stock disponible
//- `POST /api/products/{id}/stock/in` → registrar entrada
//- `POST /api/products/{id}/stock/out` → registrar salida
//- `GET /api/products/{id}/movements` → ver historial (opcional)

    @GetMapping("/stock")
    public ResponseEntity<Integer> getStock (@PathVariable("productId") Long id){
        return ResponseEntity.ok(inventoryService.getAvailableStock(id));
    }

//    @PostMapping("/movements")
//    public ResponseEntity<InventoryMovement> registerStockIn (
//            @PathVariable("productId") Long id,
//            @RequestBody MovementRequest body){
//        inventoryService.registerStockIn()
//    }
}

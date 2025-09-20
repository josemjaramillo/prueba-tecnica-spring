package com.example.inventory.dto;

import com.example.inventory.entity.InventoryMovement;

public record MovementResponseDTO (
        Long id,
        Long productId,
        InventoryMovement.MovementType movementType,
        Integer quantity
) { }

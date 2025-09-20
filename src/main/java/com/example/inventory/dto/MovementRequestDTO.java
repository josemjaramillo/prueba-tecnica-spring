package com.example.inventory.dto;

import com.example.inventory.entity.InventoryMovement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovementRequestDTO(
        @NotNull InventoryMovement.MovementType movementType,
        @Positive Integer quantity
) { }

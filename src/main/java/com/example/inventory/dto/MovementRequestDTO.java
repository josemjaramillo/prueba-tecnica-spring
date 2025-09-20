package com.example.inventory.dto;

import com.example.inventory.entity.InventoryMovement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovementRequestDTO(
        @Schema(description = "Tipo de movimiento (IN o OUT)", example = "IN") @NotNull InventoryMovement.MovementType movementType,
        @Schema(description = "Cantidad del movimiento", example = "10") @Positive Integer quantity
) { }

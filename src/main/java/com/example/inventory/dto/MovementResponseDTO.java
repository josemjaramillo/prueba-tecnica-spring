package com.example.inventory.dto;

import com.example.inventory.entity.InventoryMovement;
import io.swagger.v3.oas.annotations.media.Schema;

public record MovementResponseDTO (
        @Schema(description = "ID del movimiento", example = "1001") Long id,
        @Schema(description = "ID del producto", example = "1") Long productId,
        @Schema(description = "Tipo de movimiento", example = "OUT") InventoryMovement.MovementType movementType,
        @Schema(description = "Cantidad", example = "5") Integer quantity,
        @Schema(description = "Fecha del movimiento") java.sql.Timestamp movementDate
) { }
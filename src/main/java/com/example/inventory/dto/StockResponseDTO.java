package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record StockResponseDTO(
        @Schema(description = "ID del producto", example = "1") Long id,
        @Schema(description = "Nombre del producto", example = "Teclado mecánico") String name,
        @Schema(description = "Descripción del producto") String description,
        @Schema(description = "Precio del producto", example = "49.99") BigDecimal price,
        @Schema(description = "Cantidad disponible en inventario", example = "30") Integer stock
) {}

package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponseDTO(
        @Schema(description = "ID del producto", example = "1") Long id,
        @Schema(description = "Nombre del producto", example = "Teclado mecánico") String name,
        @Schema(description = "Descripción del producto", example = "Teclado retroiluminado con switches rojos") String description,
        @Schema(description = "Precio del producto", example = "49.99") BigDecimal price
) {}

package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequestDTO(
        @Schema(description = "Nombre del producto", example = "Teclado mecánico") @NotBlank String name,
        @Schema(description = "Descripción del producto", example = "Teclado retroiluminado con switches rojos") String description,
        @Schema(description = "Precio del producto", example = "49.99") @NotNull @Positive BigDecimal price
) {}
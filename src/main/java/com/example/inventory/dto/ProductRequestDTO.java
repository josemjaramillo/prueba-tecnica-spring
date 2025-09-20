package com.example.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal price
) {}
package com.example.inventory.dto;

import java.math.BigDecimal;

public record StockResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {}

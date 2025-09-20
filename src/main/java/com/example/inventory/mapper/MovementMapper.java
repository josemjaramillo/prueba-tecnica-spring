package com.example.inventory.mapper;

import com.example.inventory.dto.MovementRequestDTO;
import com.example.inventory.dto.MovementResponseDTO;
import com.example.inventory.entity.InventoryMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementMapper {
    InventoryMovement toEntity(MovementRequestDTO dto);

    @Mapping(target = "productId", source = "product.id")
    MovementResponseDTO toDto(InventoryMovement entity);

    List<MovementResponseDTO> toDto(List<InventoryMovement> entities);
}

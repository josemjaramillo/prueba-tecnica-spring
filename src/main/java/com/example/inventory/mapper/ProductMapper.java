package com.example.inventory.mapper;

import com.example.inventory.dto.ProductRequestDTO;
import com.example.inventory.dto.ProductResponseDTO;
import com.example.inventory.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toDto(Product entity);

    List<ProductResponseDTO> toDto(List<Product> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget Product entity);
}

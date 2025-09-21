package com.example.inventory.service.impl;

import com.example.inventory.dto.ProductRequestDTO;
import com.example.inventory.dto.ProductResponseDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock ProductRepository productRepository;
    @Mock ProductMapper mapper;

    @InjectMocks ProductServiceImpl service;

    private ProductRequestDTO request;
    private Product entity;
    private ProductResponseDTO response;

    @BeforeEach
    void setUp() {
        request = new ProductRequestDTO("Mouse", "Gamer", new BigDecimal("99.90"));
        entity = new Product( "Mouse", "Gamer", new BigDecimal("99.90"));
        response = new ProductResponseDTO(1L, "Mouse", "Gamer", new BigDecimal("99.90"));
    }

    @Test
    void createProduct_ok() {
        Mockito.when(mapper.toEntity(request)).thenReturn(entity);
        Mockito.when(productRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(response);

        ProductResponseDTO out = service.createProduct(request);

        assertEquals(1L, out.id());
        assertEquals("Mouse", out.name());
        Mockito.verify(productRepository).save(entity);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    void createProduct_precioInvalido_lanzaIllegalArgument() {
        ProductRequestDTO bad = new ProductRequestDTO("X", "Y", new BigDecimal("0"));

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> service.createProduct(bad));

        assertTrue(ex.getMessage().contains("precio") || ex.getMessage().toLowerCase().contains("precio"));
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    void getProductById_ok() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDto(entity)).thenReturn(response);

        ProductResponseDTO out = service.getProductById(1L);

        assertEquals(1L, out.id());
        Mockito.verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_noExiste_lanzaNotFound() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getProductById(99L));
    }

    @Test
    void updateProduct_ok() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        // mapper.updateEntityFromDto(...) no devuelve nada
        Mockito.doAnswer(inv -> { /* side-effects si quieres */ return null; })
                .when(mapper).updateEntityFromDto(Mockito.any(ProductRequestDTO.class), Mockito.eq(entity));
        Mockito.when(productRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity)).thenReturn(response);

        ProductResponseDTO out = service.updateProduct(1L, request);

        assertEquals("Mouse", out.name());
        Mockito.verify(mapper).updateEntityFromDto(request, entity);
        Mockito.verify(productRepository).save(entity);
    }

    @Test
    void updateProduct_precioInvalido_lanzaIllegalArgument() {
        ProductRequestDTO bad = new ProductRequestDTO("X","Y", new BigDecimal("-1"));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> service.updateProduct(1L, bad));
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

}
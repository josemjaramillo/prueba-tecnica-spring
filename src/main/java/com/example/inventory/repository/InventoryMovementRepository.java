package com.example.inventory.repository;

import com.example.inventory.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    @Query("""
        SELECT COALESCE(SUM(
            CASE
                WHEN m.movementType = com.example.inventory.entity.InventoryMovement.MovementType.IN THEN m.quantity
                ELSE -m.quantity
            END
        ), 0)
        FROM InventoryMovement m
        WHERE m.product.id = :productId
    """)
    int computeStock(@Param("productId") Long productId);

    // Consulta personalizada: buscar movimientos por producto ordenados por fecha descendente
    List<InventoryMovement> findByProduct_IdOrderByMovementDateDesc(Long productId);
}
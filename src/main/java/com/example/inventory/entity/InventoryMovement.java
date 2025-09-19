package com.example.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    public enum MovementType { IN, OUT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 10)
    private MovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    // insertable=false para que respete DEFAULT CURRENT_TIMESTAMP de Postgres
    @Column(name = "movement_date", insertable = false, updatable = false)
    private java.sql.Timestamp movementDate;

    public InventoryMovement() {}
    public InventoryMovement(Product product, MovementType type, Integer quantity) {
        this.product = product;
        this.movementType = type;
        this.quantity = quantity;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public java.sql.Timestamp getMovementDate() { return movementDate; }
}

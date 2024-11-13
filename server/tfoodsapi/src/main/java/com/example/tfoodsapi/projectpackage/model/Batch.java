package com.example.tfoodsapi.projectpackage.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Duration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Column(name = "manufactured_date", nullable = false)
    private Timestamp manufacturedDate;

    @Column(name = "expiration_date", nullable = false)
    private Timestamp expirationDate;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());

        if (manufacturedDate != null && product != null && product.getShelfLifeDays() != null) {
            Instant expirationInstant = manufacturedDate.toInstant().plus(Duration.ofDays(product.getShelfLifeDays()));
            expirationDate = Timestamp.from(expirationInstant);
        } else {

            expirationDate = null;
        }
    }

    // Getters and Setters
}

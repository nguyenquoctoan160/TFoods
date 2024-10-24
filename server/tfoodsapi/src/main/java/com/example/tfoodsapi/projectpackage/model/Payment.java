package com.example.tfoodsapi.projectpackage.model;

import java.sql.Timestamp;

import com.example.tfoodsapi.projectpackage.projectenum.PaymentMethod;
import com.example.tfoodsapi.projectpackage.projectenum.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    // Getters and Setters@PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis()); // Initialize createdAt
    }
}

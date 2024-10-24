package com.example.tfoodsapi.projectpackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tfoodsapi.projectpackage.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(Integer categoryId, String name, Pageable pageable);

    Page<Product> findBySeller_IdAndNameContainingIgnoreCase(Integer sellerId, String name, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable); // Add this line
}

package com.example.tfoodsapi.projectpackage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.model.User;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Page<Branch> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address,
            Pageable pageable);

    Page<Branch> findBySeller(User seller, Pageable pageable);
}

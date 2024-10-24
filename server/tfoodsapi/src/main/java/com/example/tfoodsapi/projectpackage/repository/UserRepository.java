package com.example.tfoodsapi.projectpackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tfoodsapi.projectpackage.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Thêm các phương thức truy vấn nếu cần
    User findByUsername(String username);
}

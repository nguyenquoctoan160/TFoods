package com.example.tfoodsapi.projectpackage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tfoodsapi.projectpackage.model.Category;

import com.example.tfoodsapi.projectpackage.service.CategoryService;
import com.example.tfoodsapi.projectpackage.service.UserService;
import com.example.tfoodsapi.security.authentication.CustomAuthenticationToken;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> createCategory(@RequestBody @Valid String categoryname) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        Integer userId = getAuthenticatedUserId(authentication);
        if (!userService.isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not Administrator");
        }

        categoryService.createCategory(categoryname);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer id,
            @RequestBody @Valid String categoryname) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        // Assuming the UserDetails contains the user ID or a method to retrieve it.
        Integer userId = getAuthenticatedUserId(authentication); // Implement this method accordingly
        if (!userService.isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not Administrator");
        }

        categoryService.updateCategory(id, categoryname);
        return ResponseEntity.ok("Successfully");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        Integer userId = getAuthenticatedUserId(authentication);
        if (!userService.isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not Administrator");
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Successfully");
    }

    // Lấy danh sách Category có phân trang
    @GetMapping("/get")
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "id") String sortBy) {
        Page<Category> categories = categoryService.getCategories(page, 10, sortBy);
        return ResponseEntity.ok(categories);
    }

    // Tìm Category theo tên có phân trang
    @GetMapping("/search")
    public ResponseEntity<Page<Category>> searchCategoriesByName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {
        Page<Category> categories = categoryService.searchCategoriesByName(name, page, 10);
        return ResponseEntity.ok(categories);
    }

    private Integer getAuthenticatedUserId(Authentication authentication) {
        try {

            if (authentication instanceof CustomAuthenticationToken) {
                CustomAuthenticationToken customAuth = (CustomAuthenticationToken) authentication;

                return customAuth.getUserId();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
}

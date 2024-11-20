package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.model.Category;
import com.example.tfoodsapi.projectpackage.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisService redisService;

    public Category getCategoryById(Integer id) {
        // Kiểm tra trong Redis trước
        Category category = redisService.getCategoryFromID(id);
        if (category != null) {
            return category;
        }

        // Nếu không có trong Redis, lấy từ DB
        category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Lưu vào Redis
        redisService.setCategoryFromID(category);
        return category;
    }

    public Category createCategory(String categoryName) {
        Category category = new Category(categoryName);
        category = categoryRepository.save(category);

        // Lưu vào Redis
        redisService.setCategoryFromID(category);
        return category;
    }

    public Category updateCategory(Integer id, String categoryName) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existingCategory.setName(categoryName);
        existingCategory = categoryRepository.save(existingCategory);

        // Cập nhật vào Redis
        redisService.setCategoryFromID(existingCategory);
        return existingCategory;
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);

        // Xóa khỏi Redis
        redisService.deleteCategoryById(id);
    }

    public Page<Category> getCategories(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return categoryRepository.findAll(pageable);
    }

    public Page<Category> searchCategoriesByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}

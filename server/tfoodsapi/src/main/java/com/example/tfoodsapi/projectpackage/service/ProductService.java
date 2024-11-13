package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.model.Product;
import com.example.tfoodsapi.projectpackage.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(Integer categoryId, Integer sellerId, String name, Pageable pageable) {
        if (categoryId != null) {
            return productRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageable);
        } else if (sellerId != null) {
            return productRepository.findBySeller_IdAndNameContainingIgnoreCase(sellerId, name, pageable);
        } else {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        }
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        Product product = getProductById(id);
        if (updatedProduct.getName() != null)
            product.setName(updatedProduct.getName());
        if (updatedProduct.getDescription() != null)
            product.setDescription(updatedProduct.getDescription());
        if (updatedProduct.getPrice() != null)
            product.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getCategory() != null)
            product.setCategory(updatedProduct.getCategory());
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}

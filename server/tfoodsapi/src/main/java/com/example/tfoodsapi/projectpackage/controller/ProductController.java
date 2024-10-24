package com.example.tfoodsapi.projectpackage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tfoodsapi.projectpackage.DTOModel.ProductDTO;
import com.example.tfoodsapi.projectpackage.model.Category;
import com.example.tfoodsapi.projectpackage.model.Product;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.projectenum.Role;
import com.example.tfoodsapi.projectpackage.service.CategoryService;
import com.example.tfoodsapi.projectpackage.service.ProductService;
import com.example.tfoodsapi.projectpackage.service.UserService;
import com.example.tfoodsapi.security.JwtUtil;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(value = "category_id", required = false) Integer categoryId,
            @RequestParam(value = "seller_id", required = false) Integer sellerId,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getProducts(categoryId, sellerId, search, pageable);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody ProductDTO productDTO) {

        // Lấy token từ header, bỏ đi "Bearer " ở đầu
        String token = tokenHeader.replace("Bearer ", "");

        // Lấy tên người dùng từ token
        Integer userid = jwtUtil.extractToID(token);

        // Lấy thông tin người dùng dựa trên username (giả sử có phương thức này trong
        // userService)
        User seller = userService.getUserById(userid);
        if (seller.getRole() != Role.SELLER) {
            return new ResponseEntity<>("Only SELLERs can create products", HttpStatus.FORBIDDEN);
        }

        // Chuyển đổi ProductDTO thành Product và set seller
        Product product = mapToProduct(productDTO);
        product.setSeller(seller);

        // Tạo sản phẩm
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        Product product = mapToProduct(productDTO);
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    // Chuyển đổi từ ProductDTO sang Product
    private Product mapToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        // Lấy Category từ service, ném ngoại lệ nếu không tìm thấy
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        product.setCategory(category);

        return product;
    }
}

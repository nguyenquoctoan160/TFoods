package com.example.tfoodsapi.projectpackage.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tfoodsapi.projectpackage.DTOModel.UserLoginRequest;
import com.example.tfoodsapi.projectpackage.DTOModel.UserRegistrationRequest;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.projectenum.Role;
import com.example.tfoodsapi.projectpackage.service.MinioService;
import com.example.tfoodsapi.projectpackage.service.UserService;
import com.example.tfoodsapi.security.JwtUtil;
import com.example.tfoodsapi.security.authentication.CustomAuthenticationToken;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MinioService minioService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        // Kiểm tra username

        if (registrationRequest.getUsername().length() < 6) {
            return new ResponseEntity<>("Username must be at least 6 characters long", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra mật khẩu
        if (!isPasswordSecure(registrationRequest.getPassword())) {
            return new ResponseEntity<>(
                    "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character",
                    HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra email
        if (!isValidEmail(registrationRequest.getEmail())) {
            return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem mật khẩu có khớp không
        if (!registrationRequest.getPassword().equals(registrationRequest.getRepassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword()); // Mã hóa mật khẩu (nên mã hóa ở UserService)
        user.setRole(Role.BUYER);

        // Gọi phương thức đăng ký người dùng
        userService.createUser(user);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        // Gọi phương thức xác thực người dùng
        User user = userService.authenticateUser(loginRequest);
        Map<String, String> response = new HashMap<>();
        if (user != null) {
            String token = jwtUtil.generateToken(user.getUserId());

            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("imgUrl", user.getAvatarUrl());
            // Tạo đối tượng AuthenticationToken
            CustomAuthenticationToken authentication = new CustomAuthenticationToken(
                    user.getUsername(), null, null, user.getUserId(), user.getRole());

            // Lưu vào SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid username or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // Lấy thông tin người dùng từ SecurityContextHolder
            CustomAuthenticationToken authentication = (CustomAuthenticationToken) SecurityContextHolder.getContext()
                    .getAuthentication();
            Integer userId = authentication.getUserId(); // Lấy ID người dùng từ token

            // Upload file lên MinIO và nhận URL của ảnh đại diện
            String avatarUrl = minioService.uploadFile(file);

            // Cập nhật avatar URL trong model User
            userService.setNewAvatar(userId, avatarUrl);
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading avatar: " + e.getMessage());
        }
    }

    // Phương thức kiểm tra mật khẩu an toàn
    private boolean isPasswordSecure(String password) {
        return password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() && // Có ít nhất một chữ cái in hoa
                Pattern.compile("[a-z]").matcher(password).find() && // Có ít nhất một chữ cái thường
                Pattern.compile("[0-9]").matcher(password).find() && // Có ít nhất một chữ số
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find(); // Có ít nhất một ký tự đặc biệt
    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
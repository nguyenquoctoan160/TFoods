package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.DTOModel.UserLoginRequest;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder; // Đảm bảo rằng đây là biến đú
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Thực hiện kiểm tra và mã hóa mật khẩu nếu cần
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User authenticateUser(UserLoginRequest loginRequest) {
        // Tìm người dùng theo username
        User user = userRepository.findByUsername(loginRequest.getUsername());

        // Kiểm tra xem người dùng có tồn tại và mật khẩu có khớp không
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return user; // Đăng nhập thành công
        }
        return null; // Đăng nhập không thành công
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);

    }

    public void setNewAvatar(Integer id, String avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
        user.setAvatarUrl(avatar);
        userRepository.save(user);
    }

}
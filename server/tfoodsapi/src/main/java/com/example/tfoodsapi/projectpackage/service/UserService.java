package com.example.tfoodsapi.projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.DTOModel.UserLoginRequest;
import com.example.tfoodsapi.projectpackage.model.User;
import com.example.tfoodsapi.projectpackage.repository.UserRepository;

import jakarta.annotation.Resource;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder; // Đảm bảo rằng đây là biến đú
    @Autowired
    private UserRepository userRepository;
    @Resource
    private RedisTemplate<String, User> redisTemplate;

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
        // Tìm người dùng trong Redis trước
        String redisKey = "user:" + id;
        User user = redisTemplate.opsForValue().get(redisKey);

        if (user != null) {
            System.out.println(user.toString());
            return user; // Trả về người dùng từ cache
        }

        // Nếu không tìm thấy trong cache, tìm từ repository
        user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));

        // Lưu người dùng vào cache
        redisTemplate.opsForValue().set(redisKey, user);

        return user;
    }

    public User findByUsername(String name) {
        // Tìm người dùng trong Redis trước
        User user = redisTemplate.opsForValue().get("user:" + name);

        if (user != null) {

            return user; // Trả về người dùng từ cache
        }

        // Nếu không tìm thấy trong cache, tìm từ repository
        user = userRepository.findByUsername(name);

        // Lưu người dùng vào cache
        if (user != null) {
            redisTemplate.opsForValue().set("user:" + name, user);
        }

        return user;
    }

    public void setNewAvatar(Integer id, String avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
        user.setAvatarUrl(avatar);
        userRepository.save(user);

        // Cập nhật cache
        redisTemplate.opsForValue().set("user:" + id, user);
        redisTemplate.opsForValue().set("user:" + user.getUsername(), user);
    }

}
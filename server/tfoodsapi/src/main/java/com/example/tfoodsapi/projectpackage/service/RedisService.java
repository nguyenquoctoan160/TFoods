package com.example.tfoodsapi.projectpackage.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.model.Category;
import com.example.tfoodsapi.projectpackage.model.User;

import jakarta.annotation.Resource;

@Service
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public User getUserFromIDKey(Integer key) {
        return (User) redisTemplate.opsForValue().get("user:" + key);
    }

    public void setUserFromIDKey(Integer key, User user) {
        redisTemplate.opsForValue().set("user:" + key, user, Duration.ofHours(1));
    }

    public Branch getBranchFromID(Integer key) {
        return (Branch) redisTemplate.opsForValue().get("branch:" + key);
    }

    public void setBranchFromID(Branch branch) {
        redisTemplate.opsForValue().set("branch:" + Integer.toString(branch.getId()), branch, Duration.ofHours(1));
    }

    public void deleteBranchById(Integer key) {
        redisTemplate.delete("branch:" + key);
    }

    public User getUserFromNameKey(String key) {
        return (User) redisTemplate.opsForValue().get("username:" + key);
    }

    public void setUserFromNameKey(String key, User user) {
        redisTemplate.opsForValue().set("username:" + key, user, Duration.ofHours(1));
    }

    public Category getCategoryFromID(Integer key) {
        return (Category) redisTemplate.opsForValue().get("category:" + key);
    }

    public void setCategoryFromID(Category category) {
        redisTemplate.opsForValue().set("category:" + category.getId(), category, Duration.ofHours(1));
    }

    public void deleteCategoryById(Integer key) {
        redisTemplate.delete("category:" + key);
    }
}

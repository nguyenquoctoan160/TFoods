package com.example.tfoodsapi.projectpackage.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.tfoodsapi.projectpackage.model.Branch;
import com.example.tfoodsapi.projectpackage.model.User;

import jakarta.annotation.Resource;

@Service
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> RedisTemplate;

    public User getUserFromIDKey(Integer key) {
        return (User) RedisTemplate.opsForValue().get("user:" + key);
    }

    public void setUserFromIDKey(Integer key, User user) {
        RedisTemplate.opsForValue().set("user:" + key, user, Duration.ofHours(1));
    }

    public Branch getBranchFromID(Integer key) {
        return (Branch) RedisTemplate.opsForValue().get("branch:" + key);
    }

    public void setBranchFromID(Branch branch) {
        RedisTemplate.opsForValue().set("branch:" + Integer.toString(branch.getId()), branch, Duration.ofHours(1));
    }

    public void deleteBranchById(Integer key) {
        RedisTemplate.delete("branch:" + key);
    }

    public User getUserFromNameKey(String key) {
        return (User) RedisTemplate.opsForValue().get("username:" + key);
    }

    public void setUserFromNameKey(String key, User user) {
        RedisTemplate.opsForValue().set("username:" + key, user, Duration.ofHours(1));
    }
}

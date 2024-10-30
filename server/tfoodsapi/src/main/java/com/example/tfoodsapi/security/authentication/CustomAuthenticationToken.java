package com.example.tfoodsapi.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;

import com.example.tfoodsapi.projectpackage.projectenum.Role;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final Integer userId;
    private final String token;

    public CustomAuthenticationToken(String token, Integer userId, Collection<? extends GrantedAuthority> authorities,
            Object object, Role none) {
        super(authorities);
        this.token = token;
        this.userId = userId;
        this.setAuthenticated(true);
    }

    public Integer getUserId() {
        return this.userId;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userId; // hoặc tên người dùng nếu bạn có
    }
}

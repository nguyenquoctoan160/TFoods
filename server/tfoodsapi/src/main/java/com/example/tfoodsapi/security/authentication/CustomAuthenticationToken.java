package com.example.tfoodsapi.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.tfoodsapi.projectpackage.projectenum.Role;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Integer userId;
    private Role role;

    public CustomAuthenticationToken(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities, Integer userId, Role role) {
        super(principal, credentials, authorities);
        this.userId = userId;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }
}

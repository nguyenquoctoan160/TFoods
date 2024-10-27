package com.example.tfoodsapi.security.filter;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tfoodsapi.projectpackage.projectenum.Role;
import com.example.tfoodsapi.security.JwtUtil;
import com.example.tfoodsapi.security.authentication.CustomAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = getToken(request);

        if (authorizationHeader != null) {
            String token = authorizationHeader;
            Integer userId = jwtUtil.extractToID(token);

            if (userId != null) {
                // Tạo CustomAuthenticationToken

                CustomAuthenticationToken authentication = new CustomAuthenticationToken(token, userId, null, null,
                        Role.NONE);
                // Lưu vào SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    public String getToken(HttpServletRequest request) {
        // Lấy tất cả các cookies từ request
        Cookie[] cookies = request.getCookies();

        // Kiểm tra nếu cookies không null
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên là "JWtoken" hay không
                if ("JWtoken".equals(cookie.getName())) {
                    // Trả về giá trị của cookie
                    return cookie.getValue();
                }
            }
        }

        return "JWtoken not found!";
    }
}

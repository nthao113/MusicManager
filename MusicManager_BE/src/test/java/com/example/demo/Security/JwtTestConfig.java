package com.example.demo.Security;

import com.example.demo.Security.Jwt.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JwtTestConfig {
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}

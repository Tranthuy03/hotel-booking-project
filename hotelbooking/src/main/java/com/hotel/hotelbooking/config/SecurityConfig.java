package com.hotel.hotelbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/uploads/**").permitAll() // Cho phép truy cập ảnh
                .anyRequest().permitAll() // Cho phép mọi request khác (tạm thời, để test)
                )
                .csrf(csrf -> csrf.disable()); // Tắt CSRF để test form upload nhanh

        return http.build();
    }
}

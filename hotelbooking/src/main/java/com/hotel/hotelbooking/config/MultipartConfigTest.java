package com.hotel.hotelbooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.MultipartConfigElement;

@Component
public class MultipartConfigTest {

    @Autowired
    private MultipartConfigElement multipartConfigElement;

    @PostConstruct
    public void check() {
        if (multipartConfigElement != null) {
            System.out.println("✅ MultipartConfigElement đã được Spring đọc: " + multipartConfigElement);
        } else {
            System.out.println("❌ MultipartConfigElement chưa được Spring đọc!");
        }
    }
}

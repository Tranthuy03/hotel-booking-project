package com.hotel.hotelbooking.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Giới hạn tối đa kích thước 1 file
        factory.setMaxFileSize(DataSize.ofMegabytes(50));

        // Giới hạn tổng kích thước request (tất cả file + field)
        factory.setMaxRequestSize(DataSize.ofMegabytes(200));

        // Tạo MultipartConfigElement
        return factory.createMultipartConfig();
    }

}

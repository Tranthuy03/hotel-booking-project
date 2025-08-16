// package com.hotel.hotelbooking.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.util.unit.DataSize;

// import jakarta.servlet.MultipartConfigElement;
// import org.springframework.boot.web.servlet.MultipartConfigFactory;

// @Configuration
// public class MultipartConfig {

//     @Bean
//     public MultipartConfigElement multipartConfigElement() {
//         MultipartConfigFactory factory = new MultipartConfigFactory();
//         factory.setMaxFileSize(DataSize.ofMegabytes(10));      // max file size
//         factory.setMaxRequestSize(DataSize.ofMegabytes(50));   // max total request
//         return factory.createMultipartConfig();
//     }
// }

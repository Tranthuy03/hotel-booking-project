package com.hotel.hotelbooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         Path uploadDir = Paths.get("uploads").toAbsolutePath();
//         String uploadPath = uploadDir.toUri().toString();
//         registry.addResourceHandler("/uploads/**")
//                 .addResourceLocations(uploadPath)
//                 .setCachePeriod(0);
//     }
// }
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối của thư mục uploads theo project hiện tại
        String uploadPath = System.getProperty("user.dir") + "/hotelbooking/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}

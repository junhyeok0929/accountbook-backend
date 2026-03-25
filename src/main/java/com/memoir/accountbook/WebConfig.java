package com.memoir.accountbook;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로(/api/** 뿐만 아니라 /uploads/** 포함)에 대해 CORS 허용
        registry.addMapping("/**") 
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://172.30.1.7:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // [중요] 반드시 file:/// 뒤에 경로가 정확해야 합니다. 
        // 윈도우 경로의 경우 /D:/... 형식이 더 정확할 수 있습니다.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/dev/uploads/");
    }
}

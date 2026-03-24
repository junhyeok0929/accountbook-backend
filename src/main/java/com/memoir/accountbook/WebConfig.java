package com.memoir.accountbook;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 이 메소드가 CORS 설정을 담당합니다.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 1. 우리 API의 /api/로 시작하는 모든 경로에 대해
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000") // 2. http://localhost:3000 (React 서버)의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 3. 허용할 HTTP 메소드
                .allowedHeaders("*") // 4. 모든 종류의 요청 헤더를 허용
                .allowCredentials(true); // 5. 쿠키/인증 정보(JWT 토큰 등)를 실어 나르는 것을 허용
    }
}
package com.main.gundam.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CorsConfig {
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Origin URL 등록
        configuration.setAllowedMethods(Arrays.asList("*")); // 사용할 CRUD 메소드 등록
        configuration.setAllowedHeaders(Arrays.asList("*")); // 사용할 Header 등록

        
        configuration.setExposedHeaders(Arrays.asList("authorization", "refreshToken")); // ExpoesdHeader에 클라이언트가 응답에 접근할 수 있는 header들을 추가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}

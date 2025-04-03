package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // ✅ Allow all endpoints
                        .allowedOrigins("http://localhost:5173", "http://localhost:5174", 
                                        "http://localhost:5175", "http://localhost:5176", 
                                        "https://quizit-9ahm.vercel.app/") // ✅ Allow React frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // ✅ Allow all HTTP methods
                        .allowCredentials(true) // ✅ Allow cookies/auth headers
                        .allowedHeaders("*") // ✅ Allow all headers
                        .exposedHeaders("Authorization"); // ✅ Expose JWT token if needed
            }
        };
    }
}

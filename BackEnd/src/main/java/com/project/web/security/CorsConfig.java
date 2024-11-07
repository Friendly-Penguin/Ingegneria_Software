package com.project.web.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // Defines a bean that configures the CORS settings for the web application
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {  // Anonymous class implementation of WebMvcConfigurer
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Adds CORS mappings for all endpoints ("/**")
                registry.addMapping("/**")  // The pattern "/**" means all paths in the application
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowed HTTP methods for cross-origin requests
                        .allowedOrigins("*");  // Allows requests from any origin (*), i.e., all domains are allowed
            }
        };
    }
}

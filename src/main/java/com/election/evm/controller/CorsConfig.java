package com.election.evm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS CONFIG BASED ON YOUR PROJECT
 *
 * FRONTEND:
 * https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app
 *
 * BACKEND:
 * https://evmbackend-n3qk.onrender.com
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed Frontend Origins
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app"
        ));

        // Allowed HTTP Methods
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Allow All Headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow JWT / Cookies
        configuration.setAllowCredentials(true);

        // Apply to all APIs
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
package com.election.evm.config;

import com.election.evm.security.JwtAuthenticationFilter;
import com.election.evm.security.GoogleOAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

    // Uses FRONTEND_URL from application.properties / Render env
    @Value("${app.oauth2.frontend-redirect-url}")
    private String frontendUrl;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for JWT APIs
                .csrf(csrf -> csrf.disable())

                // Centralized CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // JWT = stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // PRE-FLIGHT
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // PUBLIC AUTH
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/otp/send",
                                "/api/auth/otp/verify",
                                "/api/auth/refresh"
                        ).permitAll()

                        // GOOGLE OAUTH
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // USER PROFILE
                        .requestMatchers(HttpMethod.GET, "/api/auth/me")
                        .authenticated()

                        // ELECTION RESULTS
                        .requestMatchers(HttpMethod.GET, "/api/election-results")
                        .hasAnyRole("ADMIN", "CITIZEN", "OBSERVER", "ANALYST")

                        .requestMatchers(HttpMethod.POST, "/api/election-results")
                        .hasAnyRole("ADMIN", "ANALYST")

                        .requestMatchers(HttpMethod.POST, "/api/election-results/bulk-upload")
                        .hasRole("ANALYST")

                        .requestMatchers(HttpMethod.PUT, "/api/election-results/**")
                        .hasAnyRole("ADMIN", "ANALYST")

                        .requestMatchers(HttpMethod.DELETE, "/api/election-results/**")
                        .hasAnyRole("ADMIN", "ANALYST")

                        // ADMIN
                        .requestMatchers(
                                "/api/users/**",
                                "/api/dashboard/**",
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        // INCIDENTS
                        .requestMatchers("/api/incidents/**")
                        .hasAnyRole("ADMIN", "OBSERVER")

                        // FRAUD
                        .requestMatchers(HttpMethod.GET, "/api/fraud-reports")
                        .hasAnyRole("ADMIN", "CITIZEN", "OBSERVER")

                        .requestMatchers(HttpMethod.POST, "/api/fraud-reports")
                        .hasAnyRole("ADMIN", "CITIZEN")

                        .requestMatchers(HttpMethod.PUT, "/api/fraud-reports/**")
                        .hasAnyRole("ADMIN", "CITIZEN")

                        .requestMatchers(HttpMethod.DELETE, "/api/fraud-reports/**")
                        .hasRole("ADMIN")

                        // ANALYST
                        .requestMatchers("/api/analyst-reports/**")
                        .hasAnyRole("ADMIN", "ANALYST", "OBSERVER")

                        // ACTUATOR
                        .requestMatchers("/actuator/health").permitAll()

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                // GOOGLE LOGIN
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(googleOAuth2SuccessHandler)
                )

                // JWT FILTER
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    // SINGLE CORS CONFIG ONLY
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Supports current + old Vercel + localhost
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                frontendUrl,
                "https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app"
        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        config.setExposedHeaders(List.of(
                "Authorization"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
package com.ilisi.jobfinder.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)// Disables CSRF protection to bypass errors during developement
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**","/api/offre/**","/api/entreprise/**","/api/candidat/**","/socket.io/**", "/ws/**","/api/formation/**","/api/experience/**","/api/candidature/**","/api/notifications/**").permitAll() // Autoriser l'accès public/anonyme
                        .anyRequest().authenticated() // Authentication required for all requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // CORS configuration
        http.cors(cors -> {
            cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedOriginPattern("*");
                config.addAllowedMethod("*");
                config.addAllowedHeader("*");
                config.setAllowCredentials(true);
                return config;
            });
        });

        // Allow WebSocket handshake requests
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
        );
        return http.build();
    }
}

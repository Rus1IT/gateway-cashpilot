package com.rus1it.springgatewaycashpilot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// 1. Импортируйте эти классы
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays; // или java.util.List

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())

                // 2. ДОБАВЬТЕ ЭТУ СТРОКУ
                // Она "подключает" бин с настройками CORS (который мы создадим ниже)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**", "/actuator/health", "/actuator/info").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

    // 3. ДОБАВЬТЕ ЭТОТ БИН
    // Это и есть та самая конфигурация CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ❗ Разрешаем вашему Live Server (порт 8081)
        config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:8081"));

        // Разрешаем все методы
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Разрешаем все заголовки
        config.setAllowedHeaders(Arrays.asList("*"));

        // Разрешаем отправку credentials (включая токены)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Применяем эту конфигурацию ко всем путям "/**"
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
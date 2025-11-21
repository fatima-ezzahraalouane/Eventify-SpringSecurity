package com.eventify.springsecurity.config;

import com.eventify.springsecurity.security.CustomAccessDeniedHandler;
import com.eventify.springsecurity.security.CustomAuthenticationEntryPoint;
import com.eventify.springsecurity.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(@Lazy CustomAuthenticationProvider customAuthenticationProvider,
                         CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                         CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                
                .csrf(csrf -> csrf.disable())
                
                
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                
                
                .authorizeHttpRequests(auth -> auth
                        
                        .requestMatchers("/api/public/**").permitAll()                       
                        
                        .requestMatchers("/api/user/**").hasRole("USER")                       
                        
                        .requestMatchers("/api/organizer/**").hasRole("ORGANIZER")                       
                        
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        
                        .anyRequest().authenticated())
                
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }
}


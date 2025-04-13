package com.sensormanager.iot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sensormanager.iot.security.CustomAccessDeniedHandler;
import com.sensormanager.iot.security.CustomAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // aún funciona en Spring Boot 3.4.3
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                    ex.accessDeniedHandler(customAccessDeniedHandler);
                })
                .authorizeHttpRequests(auth -> {
                    // 🚨 Swagger libre:
                    auth.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",        // 👈 ESTA ES LA QUE FALTABA
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**"
                    ).permitAll();
                    // 👇 tus reglas de negocio:
                    auth.requestMatchers("/companies/**").hasAnyAuthority("ROOT");
                    auth.requestMatchers("/users/**").hasAnyAuthority("ROOT", "COMPANY_ADMIN");
                    auth.requestMatchers("/locations/**").hasAnyAuthority("ROOT", "COMPANY_ADMIN");
                    auth.requestMatchers("/sensors/**").hasAnyAuthority("ROOT", "COMPANY_ADMIN");
                    auth.requestMatchers("/user-role/**").hasAnyAuthority("ROOT", "COMPANY_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/sensordata/**").hasAnyAuthority("ROOT", "COMPANY_ADMIN", "COMPANY_USER");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/sensordata/**").permitAll();

                    auth.anyRequest().denyAll();
                });

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

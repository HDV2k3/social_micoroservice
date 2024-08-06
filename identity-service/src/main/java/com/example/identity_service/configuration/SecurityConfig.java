package com.example.identity_service.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.identity_service.service.CustomUserDetailsService;
import com.example.identity_service.validator.CustomAuthenticationProvider;
import com.example.identity_service.validator.DifferentLocationChecker;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
        "/users/register", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
    };

    private final CustomJwtDecoder customJwtDecoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Allow public access to specified endpoints for both GET and POST requests
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.GET, "/users/**")
                        .permitAll() // Permit GET requests to /verify-email
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                        .permitAll() // Permit POST requests to PUBLIC_ENDPOINTS
                        .anyRequest()
                        .authenticated()) // Require authentication for all other requests
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DifferentLocationChecker differentLocationChecker() {
        return new DifferentLocationChecker();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean(name = "GeoIPCountry")
    public DatabaseReader countryDatabaseReader() throws IOException, GeoIp2Exception {
        Resource resource = new ClassPathResource("maxmind/GeoLite2-Country.mmdb");
        try (InputStream inputStream = resource.getInputStream()) {
            return new DatabaseReader.Builder(inputStream).build();
        }
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}

//    @Bean(name = "GeoIPCountry")
//    public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
//        final File resource = new File(
//
// Objects.requireNonNull(this.getClass().getClassLoader().getResource("maxmind/GeoLite2-Country.mmdb"))
//                        .getFile());
//        return new DatabaseReader.Builder(resource).build();
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(request ->
//
//                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
//                .permitAll()
//                .anyRequest()
//                .authenticated());
//
//        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
//                        .decoder(customJwtDecoder)
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
//        httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return httpSecurity.build();
//    }

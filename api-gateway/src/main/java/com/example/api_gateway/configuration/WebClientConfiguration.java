package com.example.api_gateway.configuration;

import com.example.api_gateway.repository.IdentityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Configuration
public class WebClientConfiguration {
    // Dùng để thực hiện các yêu cầu HTTP không đồng bộ.
    // Nó cho phép ứng dụng của bạn gửi và nhận dữ liệu từ các dịch vụ khác mà không chặn luồng chính.
    @Bean
    WebClient webClient()
    {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/identity").
                build();
    }
    //Tạo một client proxy để giao tiếp với dịch vụ định danh (identity service) thông qua các yêu cầu HTTP.
    // IdentityClient là một interface mà bạn định nghĩa để chứa các phương thức giao tiếp với dịch vụ định danh.
    @Bean
    IdentityClient identityClient(WebClient webClient)
    {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
    //config de thuc hien tren multi-platform
    @Bean
    CorsWebFilter corsWebFilter()
    {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}

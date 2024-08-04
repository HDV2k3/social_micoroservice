package com.example.identity_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua_parser.Parser;

import java.io.IOException;

@Configuration
public class UserAgentParserConfig {

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }
}
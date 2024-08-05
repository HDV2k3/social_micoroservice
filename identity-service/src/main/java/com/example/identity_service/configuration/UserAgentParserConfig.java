package com.example.identity_service.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua_parser.Parser;

@Configuration
public class UserAgentParserConfig {

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }
}

package com.example.identity_service.configuration;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import ua_parser.Parser;

import java.io.File;
import java.io.IOException;

@Configuration
public class UserAgentParserConfig {

    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }

}
package com.example.identity_service.configuration;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class GeoIPConfig {
    @Bean
    @Qualifier("GeoIPCity")
    public DatabaseReader databaseReader() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File database = ResourceUtils.getFile("classpath:maxmind/GeoLite2-City.mmdb");
        return new DatabaseReader.Builder(database).build();
    }
}

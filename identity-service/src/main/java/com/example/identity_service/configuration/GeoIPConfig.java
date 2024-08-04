package com.example.identity_service.configuration;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GeoIPConfig {
    @Bean
    @Qualifier("GeoIPCity")
    public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
        final File resource = new File(this.getClass()
                .getClassLoader()
                .getResource("maxmind/GeoLite2-Country.mmdb")
                .getFile());
        return new DatabaseReader.Builder(resource).build();
    }
}

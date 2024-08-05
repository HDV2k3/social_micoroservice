// package com.example.identity_service.configuration;
//
// import java.io.File;
// import java.io.IOException;
//
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.util.ResourceUtils;
//
// import com.maxmind.geoip2.DatabaseReader;
//
// @Configuration
// public class GeoIPConfig {
//    @Bean
//    @Qualifier("GeoIPCity")
//    public DatabaseReader databaseReader() throws IOException {
//        ClassLoader classLoader = getClass().getClassLoader();
//        File database = ResourceUtils.getFile("classpath:maxmind/GeoLite2-City.mmdb");
//        return new DatabaseReader.Builder(database).build();
//    }
// }
package com.example.identity_service.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.maxmind.geoip2.DatabaseReader;

@Configuration
public class GeoIPConfig {
    @Bean
    @Qualifier("GeoIPCity")
    public DatabaseReader databaseReader() throws IOException {
        // Load the resource from the classpath
        Resource resource = new ClassPathResource("maxmind/GeoLite2-City.mmdb");
        // Convert the resource to an InputStream
        try (InputStream inputStream = resource.getInputStream()) {
            // Build the DatabaseReader using the InputStream
            return new DatabaseReader.Builder(inputStream).build();
        }
    }
}

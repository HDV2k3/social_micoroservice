package com.example.identity_service.service.ServiceImpl;

import java.net.InetAddress;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.example.identity_service.service.GeoLocationService;
import com.maxmind.geoip2.DatabaseReader;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeoLocationServiceImpl implements GeoLocationService {
     Environment env;
     DatabaseReader databaseReader;

    @Override
    public String getCountryByIP(String ip) throws Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        return databaseReader.country(ipAddress).getCountry().getName();
    }

    @Override
    public boolean isGeoIpLibEnabled() {
        return !Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
    }
}


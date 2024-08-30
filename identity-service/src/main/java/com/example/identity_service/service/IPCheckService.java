package com.example.identity_service.service;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;

//package com.example.identity_service.service;
//
//import java.net.InetAddress;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.env.Environment;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.stereotype.Service;
//
//import com.example.identity_service.entity.NewLocationToken;
//import com.example.identity_service.entity.User;
//import com.example.identity_service.entity.UserLocation;
//import com.example.identity_service.repository.NewLocationTokenRepository;
//import com.example.identity_service.repository.UserLocationRepository;
//import com.example.identity_service.repository.UserRepository;
//import com.maxmind.geoip2.DatabaseReader;
//
//@Service
//public class CheckIPService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserLocationRepository userLocationRepository;
//
//    @Autowired
//    private NewLocationTokenRepository newLocationTokenRepository;
//
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private SessionRegistry sessionRegistry;
//
//    @Autowired
//    @Qualifier("GeoIPCity")
//    private DatabaseReader databaseReader;
//
//    public NewLocationToken isNewLoginLocation(String email, String ip) {
//
//        if (isGeoIpLibEnabled()) {
//            return null;
//        }
//
//        try {
//            final InetAddress ipAddress = InetAddress.getByName(ip);
//            final String country =
//                    databaseReader.country(ipAddress).getCountry().getName();
//
//            final User user = userRepository.findByEmail(email).orElseThrow();
//            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
//            if ((loc == null) || !loc.isEnabled()) {
//                return createNewLocationToken(country, user);
//            }
//        } catch (final Exception e) {
//            return null;
//        }
//        return null;
//    }
//
//    public void addUserLocation(User user, String ip) {
//
//        if (isGeoIpLibEnabled()) {
//            return;
//        }
//
//        try {
//            // Check if the IP address is localhost (IPv4 or IPv6)
//            InetAddress ipAddress = InetAddress.getByName(ip);
//            final String country = databaseReader.country(ipAddress).getCountry().getName();
//            userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
//            UserLocation loc = new UserLocation(country, user);
//            loc.setEnabled(true);
//            userLocationRepository.save(loc);
//
//        } catch (final Exception e) {
//            e.printStackTrace(); // Log or handle exception
//            throw new RuntimeException("Error saving UserLocation", e);
//        }
//    }
//
//    private boolean isGeoIpLibEnabled() {
//        return !Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
//    }
//
//    private NewLocationToken createNewLocationToken(String country, User user) {
//        UserLocation loc = new UserLocation(country, user);
//        loc = userLocationRepository.save(loc);
//
//        final NewLocationToken token = new NewLocationToken(UUID.randomUUID().toString(), loc);
//        return newLocationTokenRepository.save(token);
//    }
//}
public interface IPCheckService {
    NewLocationToken isNewLoginLocation(String email, String ip);
    void addUserLocation(User user, String ip);
}
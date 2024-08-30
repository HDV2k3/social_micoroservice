package com.example.identity_service.service.ServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;
import com.example.identity_service.entity.UserLocation;
import com.example.identity_service.repository.UserLocationRepository;
import com.example.identity_service.repository.UserRepository;
import com.example.identity_service.service.GeoLocationService;
import com.example.identity_service.service.IPCheckService;
import com.example.identity_service.service.NewLocationTokenService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IPCheckServiceImpl implements IPCheckService {


     UserRepository userRepository;


     UserLocationRepository userLocationRepository;


     NewLocationTokenService newLocationTokenService;


     GeoLocationService geoLocationService;

    @Override
    public NewLocationToken isNewLoginLocation(String email, String ip) {

        if (geoLocationService.isGeoIpLibEnabled()) {
            return null;
        }

        try {
            final String country = geoLocationService.getCountryByIP(ip);

            final User user = userRepository.findByEmail(email).orElseThrow();
            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
            if ((loc == null) || !loc.isEnabled()) {
                return newLocationTokenService.createNewLocationToken(country, user);
            }
        } catch (final Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public void addUserLocation(User user, String ip) {

        if (geoLocationService.isGeoIpLibEnabled()) {
            return;
        }

        try {
            final String country = geoLocationService.getCountryByIP(ip);
            userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            UserLocation loc = new UserLocation(country, user);
            loc.setEnabled(true);
            userLocationRepository.save(loc);

        } catch (final Exception e) {
            e.printStackTrace(); // Log or handle exception
            throw new RuntimeException("Error saving UserLocation", e);
        }
    }
}

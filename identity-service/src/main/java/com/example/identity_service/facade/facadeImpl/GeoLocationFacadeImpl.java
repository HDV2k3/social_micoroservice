package com.example.identity_service.facade.facadeImpl;
import com.example.identity_service.facade.GeoLocationFacade;
import com.example.identity_service.service.GeoLocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeoLocationFacadeImpl  implements GeoLocationFacade {
    GeoLocationService geoLocationService;

    @Override
    public String getCountryByIP(String ip) throws Exception {
        return geoLocationService.getCountryByIP(ip);
    }

    @Override
    public boolean isGeoIpLibEnabled() {
        return geoLocationService.isGeoIpLibEnabled();
    }
}

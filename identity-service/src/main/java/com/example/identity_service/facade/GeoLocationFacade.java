package com.example.identity_service.facade;

public interface GeoLocationFacade {
    String getCountryByIP(String ip) throws Exception;
    boolean isGeoIpLibEnabled();
}

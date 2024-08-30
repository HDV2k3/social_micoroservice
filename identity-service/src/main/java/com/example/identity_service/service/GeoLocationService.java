package com.example.identity_service.service;

public interface GeoLocationService {
    String getCountryByIP(String ip) throws Exception;
    boolean isGeoIpLibEnabled();
}
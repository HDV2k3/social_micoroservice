
package com.example.identity_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.identity_service.entity.User;
import com.example.identity_service.entity.UserLocation;
import com.example.identity_service.repository.UserLocationRepository;
import com.example.identity_service.service.CheckIPService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private DatabaseReader mockDatabaseReader;

    @Mock
    private UserLocationRepository mockUserLocationRepository;

    @InjectMocks
    private CheckIPService checkIPService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUserLocation_LocalhostIPv6() throws IOException, GeoIp2Exception {
        // Mocking the behavior for localhost IPv6 (::1)
        String localhostIPv6 = "::1";
        InetAddress mockAddress = mock(InetAddress.class);
        when(mockAddress.getHostAddress()).thenReturn(localhostIPv6);

        // Mock the exception for the country method
        when(mockDatabaseReader.country(any(InetAddress.class))).thenThrow(AddressNotFoundException.class);

        // Test method invocation
        User user = new User(); // Create a mock User object or use a real one for testing
        checkIPService.addUserLocation(user, localhostIPv6);

        // Verify that save method was not called
        verify(mockUserLocationRepository, never()).save(any(UserLocation.class));
    }
}

package com.example.identity_service.validator;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.identity_service.repository.UserRepository;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import lombok.Setter;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    // Setters
    @Setter
    private UserDetailsService userDetailsService;

    @Setter
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeviceValida deviceValida;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken authToken = (CustomAuthenticationToken) authentication;

        String username = authToken.getName();
        String password = authToken.getCredentials().toString();
        String ipAddress = authToken.getIpAddress();
        String userAgent = authToken.getUserAgent();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        try {
            deviceValida.verifyDevice(
                    user, userRepository.findByEmail(authToken.getName()).orElseThrow(), ipAddress, userAgent);
        } catch (IOException | GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
        // If everything is okay, create a new fully authenticated token
        return new CustomAuthenticationToken(user, null, ipAddress, userAgent);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

package com.example.identity_service.service.ServiceImpl;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;
import com.example.identity_service.entity.UserLocation;
import com.example.identity_service.repository.NewLocationTokenRepository;
import com.example.identity_service.repository.UserLocationRepository;
import com.example.identity_service.service.NewLocationTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NewLocationTokenServiceImpl implements NewLocationTokenService {


     UserLocationRepository userLocationRepository;


     NewLocationTokenRepository newLocationTokenRepository;

    @Override
    public NewLocationToken createNewLocationToken(String country, User user) {
        UserLocation loc = new UserLocation(country, user);
        loc = userLocationRepository.save(loc);

        final NewLocationToken token = new NewLocationToken(UUID.randomUUID().toString(), loc);
        return newLocationTokenRepository.save(token);
    }
}

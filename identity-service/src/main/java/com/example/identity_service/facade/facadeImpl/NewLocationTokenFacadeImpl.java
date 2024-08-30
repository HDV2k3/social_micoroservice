package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;
import com.example.identity_service.service.NewLocationTokenService;
import com.example.identity_service.facade.NewLocationTokenFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NewLocationTokenFacadeImpl implements NewLocationTokenFacade {

    NewLocationTokenService newLocationTokenService;

    @Override
    public NewLocationToken createNewLocationToken(String country, User user) {
        // Delegate the call to the service
        return newLocationTokenService.createNewLocationToken(country, user);
    }
}
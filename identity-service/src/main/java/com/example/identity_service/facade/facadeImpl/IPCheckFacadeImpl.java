package com.example.identity_service.facade.facadeImpl;
import com.example.identity_service.entity.NewLocationToken;
import com.example.identity_service.entity.User;
import com.example.identity_service.facade.IPCheckFacade;
import com.example.identity_service.service.IPCheckService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IPCheckFacadeImpl implements IPCheckFacade {
    IPCheckService ipCheckService;

    @Override
    public NewLocationToken isNewLoginLocation(String email, String ip) {
        return ipCheckService.isNewLoginLocation(email, ip);
    }

    @Override
    public void addUserLocation(User user, String ip) {
        ipCheckService.addUserLocation(user, ip);
    }
}

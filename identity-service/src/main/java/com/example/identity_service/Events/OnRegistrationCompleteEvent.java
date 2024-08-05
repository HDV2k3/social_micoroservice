package com.example.identity_service.Events;


import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final User user;



    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;


    }


}
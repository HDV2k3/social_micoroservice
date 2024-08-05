package com.example.identity_service.Events;

import org.springframework.context.ApplicationEvent;

import com.example.identity_service.entity.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}

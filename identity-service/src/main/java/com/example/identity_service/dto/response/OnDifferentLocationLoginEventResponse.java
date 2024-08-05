package com.example.identity_service.dto.response;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.example.identity_service.entity.NewLocationToken;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class OnDifferentLocationLoginEventResponse extends ApplicationEvent {
    //
    private final Locale locale;
    private final String email;
    private final String ip;
    private final NewLocationToken token;
    private final String appUrl;

    //

    public OnDifferentLocationLoginEventResponse(
            Locale locale, String email, String ip, NewLocationToken token, String appUrl) {
        super(token);
        this.locale = locale;
        this.email = email;
        this.ip = ip;
        this.token = token;
        this.appUrl = appUrl;
    }
}

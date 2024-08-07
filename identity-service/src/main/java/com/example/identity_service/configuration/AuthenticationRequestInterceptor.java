package com.example.identity_service.configuration;

import ch.qos.logback.core.util.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // lay header de lay duoc token va thuc hien post profile khi register
        assert servletRequestAttributes != null;
        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");
        log.info("Header: {}", authHeader );

        if (StringUtils.hasText(authHeader))
        {
            requestTemplate.header("Authorization",authHeader);
        }
    }
}
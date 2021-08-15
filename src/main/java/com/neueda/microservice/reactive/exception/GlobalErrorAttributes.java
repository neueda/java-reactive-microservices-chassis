package com.neueda.microservice.reactive.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        errorAttributes.put("appName", appName);

        return errorAttributes;
    }
}

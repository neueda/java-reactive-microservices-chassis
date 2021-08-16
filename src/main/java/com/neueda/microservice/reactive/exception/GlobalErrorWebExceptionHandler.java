package com.neueda.microservice.reactive.exception;

import com.neueda.microservice.reactive.model.ErrorResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Order(HIGHEST_PRECEDENCE)
public class GlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
                                          ErrorProperties errorProperties, ApplicationContext applicationContext,
                                          ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, errorProperties,  applicationContext);

        // https://github.com/spring-projects/spring-boot/issues/12414
        // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-webflux.error-handling
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        if (request.path().endsWith("chassisCustomErrorPath")) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ErrorResponse(
                            "Custom error path called",
                            request.path(),
                            "CustomErrorPathException"));
        }
        return super.renderErrorResponse(request);
    }
}
